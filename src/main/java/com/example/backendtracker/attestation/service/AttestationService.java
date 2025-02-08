package com.example.backendtracker.attestation.service;

import com.example.backendtracker.domain.models.Teacher;
import com.example.backendtracker.domain.repositories.DeanRepository;
import com.example.backendtracker.domain.repositories.TeacherRepository;
import com.example.backendtracker.domain.repositories.mapper.HoldStudentProjection;
import com.example.backendtracker.entities.admin.dto.TeacherHolds;
import com.example.backendtracker.entities.teacher.service.TeacherService;
import com.example.backendtracker.reliability.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AttestationService {
    private final RedisTemplate<String, String> redisTemplate;
    private final DeanRepository deanRepository;
    private final TeacherService teacherService;
    private final TeacherRepository teacherRepository;


    @Transactional
    public void createStudentAttestationGrade(Integer deanId, int expirationTimeInSeconds) {
        List<TeacherHolds> holdIds = batchAddHoldsGroupsToTeachers(deanId, expirationTimeInSeconds);
        if (holdIds.isEmpty()) throw  new BadRequestException("There're no class that must be attesteted");
        Map<Integer, List<Integer>> holdStudentsMap = getStudentsGroupedByHold(holdIds);

        holdStudentsMap.forEach((holdId, studentIds) ->
                teacherService.generateStudentGrateForAttestation(holdId, studentIds)
        );
    }

    private Map<Integer, List<Integer>> getStudentsGroupedByHold(List<TeacherHolds> teacherHolds) {
        // Extract all unique hold IDs
        List<Integer> allHoldIds = teacherHolds.stream()
                .flatMap(th -> th.holdIds().stream())
                .distinct()
                .toList();

        // Get raw results from DB
        List<HoldStudentProjection> results = deanRepository.findStudentsByHoldIds(allHoldIds);

        // Group by hold ID
        return results.stream()
                .collect(Collectors.groupingBy(
                        HoldStudentProjection::getIdHold,
                        Collectors.mapping(
                                HoldStudentProjection::getIdStudentId,
                                Collectors.toList()
                        )
                ));
    }

    private List<TeacherHolds> batchAddHoldsGroupsToTeachers(Integer deanId, int expirationTimeInSeconds) {
        List<TeacherHolds> teacherHolds = deanRepository.findTeacherAndTheirHoldsId(deanId);

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            teacherHolds.forEach(teacherHold -> {
                String key = "teacher:" + teacherHold.teacherId() + ":holds";
                byte[][] holdBytes = teacherHold.holdIds().stream()
                        .map(Object::toString)
                        .map(String::getBytes)
                        .toArray(byte[][]::new);

                if (holdBytes.length > 0) {
                    connection.sAdd(key.getBytes(), holdBytes);
                    connection.expire(key.getBytes(), expirationTimeInSeconds);

                }
            });
            return null;
        });
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            String key = "dean:" + deanId + ":teachers";
            byte[][] holdBytes = teacherHolds.stream().map(TeacherHolds::teacherId)
                    .map(Object::toString)
                    .map(String::getBytes)
                    .toArray(byte[][]::new);

            if (holdBytes.length > 0) {
                connection.sAdd(key.getBytes(), holdBytes);
                connection.expire(key.getBytes(), expirationTimeInSeconds);

            }
            return null;
        });
        return teacherHolds;
    }

    public boolean isHoldIdInTeacher(Integer teacherId, Integer holdId) {
        return Boolean.TRUE.equals(
                redisTemplate.opsForSet().isMember("teacher:" + teacherId.toString() + ":holds", holdId.toString())
        );
    }

    public List<Teacher> notificationDean(Integer deanId) {
        String deanTeachersKey = "dean:" + deanId + ":teachers";

        // Step 1: Retrieve all teacher IDs associated with the dean
        Set<String> teacherIds = redisTemplate.opsForSet().members(deanTeachersKey);
        System.out.println("Dean Teachers Key: " + deanTeachersKey);
        System.out.println("Teacher IDs Retrieved: " + teacherIds);

        if (teacherIds == null || teacherIds.isEmpty()) {
            System.out.println("No teacher IDs found for dean ID: " + deanId);
            return Collections.emptyList(); // No teachers to process
        }

        // Step 2: Check which teachers still have holds (sequentially)
        List<String> teachersWithHolds = new ArrayList<>();
        for (String teacherId : teacherIds) {
            String teacherHoldsKey = "teacher:" + teacherId + ":holds";
            Boolean exists = redisTemplate.hasKey(teacherHoldsKey); // Check if the key exists
            if (Boolean.TRUE.equals(exists)) {
                teachersWithHolds.add(teacherId); // Add teacher ID if they have holds
            }
        }

        // Step 3: Convert teacher IDs to integers and fetch the corresponding teachers
        List<Integer> teacherIdList = teachersWithHolds.stream()
                .map(Integer::valueOf)
                .toList();

        return teacherRepository.findAllByIdTeacherIn(teacherIdList);
    }

    public void removeHoldIdFromTeacher(Integer teacherId, Integer holdId) {
        String key = "teacher:" + teacherId.toString() + ":holds";
        if (!redisTemplate.hasKey(key)) {
            System.out.println("Key does not exist: " + key);
            return;
        }
        Long removedCount = redisTemplate.opsForSet().remove(key, holdId.toString());
        if (removedCount == 0) {
            System.out.println("Hold ID not found in set: " + holdId);
        }
    }
    // Batch remove students from multiple teachers using pipeline
//    public void batchRemoveStudentsFromTeachers(List<TeacherHolds> teacherHolds) {
//        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
//            teacherHolds.forEach(teacherHold -> {
//                String key = "teacher:" + teacherHold.teacherId() + ":students";
//                Set<String> students = getStudentsForHolds(teacherHold.holdIds());
//                if (!students.isEmpty()) {
//                    connection.sRem(key.getBytes(), students.toArray(new byte[0][]));
//                }
//            });
//            return null;
//        });
//    }
//
//    // Get multiple membership status in one call
//    public Map<String, Boolean> batchCheckStudentsInTeacher(String teacherId, Set<String> studentIds) {
//        String key = "teacher:" + teacherId + ":students";
//        List<Object> results = redisTemplate.opsForSet()
//                .multiMembers(key, studentIds);
//
//        Map<String, Boolean> membership = new HashMap<>();
//        Iterator<String> studentIter = studentIds.iterator();
//        Iterator<Boolean> resultIter = results.stream()
//                .map(o -> (Boolean) o)
//                .collect(Collectors.toList())
//                .iterator();
//
//        while (studentIter.hasNext() && resultIter.hasNext()) {
//            membership.put(studentIter.next(), resultIter.next());
//        }
//        return membership;
//    }
//
//    // Batch get all students for multiple teachers
//    public Map<Integer, Set<String>> batchGetAllStudents(List<Integer> teacherIds) {
//        return teacherIds.stream()
//                .collect(Collectors.toMap(
//                        teacherId -> teacherId,
//                        teacherId -> redisTemplate.opsForSet()
//                                .members("teacher:" + teacherId + ":students")
//                ));
//    }
//

//
//    // Alternative: Bulk check without pipelining (for comparison)
//    public Map<Integer, Set<String>> bulkGetAllStudents(List<Integer> teacherIds) {
//        List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
//            teacherIds.forEach(teacherId ->
//                    connection.sMembers(("teacher:" + teacherId + ":students").getBytes())
//            );
//            return null;
//        });
//
//        return IntStream.range(0, teacherIds.size())
//                .boxed()
//                .collect(Collectors.toMap(
//                        teacherIds::get,
//                        i -> (Set<String>) results.get(i)
//                ));
//    }
}

