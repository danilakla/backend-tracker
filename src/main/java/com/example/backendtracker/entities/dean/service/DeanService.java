package com.example.backendtracker.entities.dean.service;

import com.example.backendtracker.domain.models.*;
import com.example.backendtracker.domain.repositories.*;
import com.example.backendtracker.domain.repositories.mapper.ClassGroupMapDTO;
import com.example.backendtracker.domain.repositories.mapper.SubgroupWithClassGroupsExtractor;
import com.example.backendtracker.entities.admin.dto.*;
import com.example.backendtracker.entities.common.CommonService;
import com.example.backendtracker.entities.common.dto.SubGroupMember;
import com.example.backendtracker.entities.dean.dto.*;
import com.example.backendtracker.entities.teacher.service.TeacherService;
import com.example.backendtracker.reliability.exception.BadRequestException;
import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import com.example.backendtracker.security.service.UserAccountService;
import com.example.backendtracker.security.service.helper.student.dto.StudentResultDto;
import com.example.backendtracker.security.util.LoginGenerator;
import com.example.backendtracker.security.util.PasswordGenerator;
import com.example.backendtracker.util.NameConverter;
import com.example.backendtracker.util.PersonAccountManager;
import com.example.backendtracker.util.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.Name;
import java.lang.Class;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeanService {
    private final SpecialtyRepository specialtyRepository;
    private final CommonService commonService;
    private final StudentRepository studentRepository;
    private final UserAccountService userAccountService;
    private final ClassFormatRepository classFormatRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final ClassGroupRepository classGroupRepository;
    private final ClassGroupsToSubgroupsRepository classGroupsToSubgroupsRepository;
    private final SubgroupRepository subgroupRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ClassGroupsHoldRepository classGroupsHoldRepository;
    private final ClassRepository classRepository;
    private final DeanRepository deanRepository;
    private final TeacherService teacherService;

    public List<SubGroupMember> getSubGroupMembers(Integer deanId) {

        return commonService.getMemberSystemForDean(deanId);
    }

    public Subgroup deleteByIdSubgroup(Integer subgroupId, Integer deanId) {
        Subgroup subgroup = subgroupRepository.findById(subgroupId).orElseThrow(() -> new BadRequestException("Subgroup not found"));
        hasBelongToDean(subgroup.getIdDean(), deanId);
        subgroupRepository.delete(subgroup);
        return subgroup;
    }

    public List<Subgroup> getListSubgroups(Integer deanId) {
        return commonService.getSubgroupList(deanId);
    }

    @Transactional
    public Student addStudent(StudentAddDto studentAddDto) {
        Integer studentAccountId = userAccountService.createUserAccount(UserRegistrationRequestDTO
                .builder()
                .login(LoginGenerator.generateLogin(studentAddDto))
                .password(PasswordGenerator.generatePassword())
                .role("STUDENT")
                .build());
        Student student = Student
                .builder()
                .idAccount(studentAccountId)
                .keyStudentParents(PasswordGenerator.generateParentPassword())
                .flpName(NameConverter.convertNameToDb(studentAddDto.lastname(), studentAddDto.name(), studentAddDto.surname()))
                .idSubgroup(studentAddDto.numberOfGroupId()).build();
        Student created = studentRepository.save(student);
        createStudentGrades(List.of(created.getIdStudent()), created.getIdSubgroup());
        return created;

    }

    public void checkExistenceOfSpecialty(String name, Integer deanId) {
        specialtyRepository.findByNameAndIdDean(name, deanId).ifPresent(specialty -> {
            throw new BadRequestException("The specialty already exists");
        });

    }


    public Specialty getSpecialty(Integer specialtyId, Integer deanId) {
        Specialty specialty = specialtyRepository.findById(specialtyId).orElseThrow(() -> new BadRequestException("there's no specialty"));
        hasBelongToDean(specialty.getIdDean(), deanId);
        return specialty;
    }

    public Specialty deleteSpecialty(Integer specialtyId, Integer deanId) {
        Specialty specialty = getSpecialty(specialtyId, deanId);
        specialtyRepository.delete(specialty);
        return specialty;
    }

    public Specialty updateSpecialty(UpdateSpecialty specialty, Integer deanId) {
        checkExistenceOfSpecialty(specialty.name(), deanId);
        Specialty specialtyToUpdate = getSpecialty(specialty.id(), deanId);
        hasBelongToDean(specialtyToUpdate.getIdDean(), deanId);
        specialtyToUpdate.setName(specialty.name());
        return specialtyRepository.save(specialtyToUpdate);
    }

    public Specialty createSpecialty(CreateSpecialtyDto specialtyDto, Integer accountId) {
        checkExistenceOfSpecialty(specialtyDto.name(), accountId);
        return specialtyRepository.save(Specialty.builder()
                .name(specialtyDto.name())
                .idDean(accountId).build());

    }

    //    public List<GroupedResultDTO> findStudentsByDeanWithAttestations(Long deanId) {
//        List<StudentDTO> students = deanRepository.findAllNotAttestedStudentWhoHasMoreThen2NotAttestationByDeanId(deanId);
//
//        // Group students by subgroup
//        Map<Long, GroupedResultDTO> groupedResults = new HashMap<>();
//        for (StudentDTO student : students) {
//            Long subgroupId = student.getSubgroup().getId();
//            GroupedResultDTO group = groupedResults.computeIfAbsent(subgroupId, k -> {
//                GroupedResultDTO g = new GroupedResultDTO();
//                g.setSubgroup(student.getSubgroup());
//                g.setStudents(new ArrayList<>());
//                return g;
//            });
//
//            // Add student to subgroup if not already present
//            if (group.getStudents().stream().noneMatch(s -> s.getId().equals(student.getId()))) {
//                group.getStudents().add(student);
//            }
//        }
//
//        return new ArrayList<>(groupedResults.values());
//    }
    public List<GroupedResultDTO> findStudentsByDeanWithAttestations(Integer deanId) {
        List<StudentDTO> students = deanRepository.findAllNotAttestedStudentWhoHasMoreThen2NotAttestationByDeanId(deanId);

        Map<Integer, GroupedResultDTO> groupedResults = new HashMap<>();
        for (StudentDTO incomingStudent : students) {
            Integer subgroupId = incomingStudent.getSubgroup().getId();
            GroupedResultDTO group = groupedResults.computeIfAbsent(subgroupId, k ->
                    new GroupedResultDTO(incomingStudent.getSubgroup(), new ArrayList<>())
            );

            List<ClassGroupDTO> incomingClassGroups = incomingStudent.getClassGroups() != null
                    ? incomingStudent.getClassGroups()
                    : new ArrayList<>();

            group.getStudents().stream()
                    .filter(s -> s.getId().equals(incomingStudent.getId()))
                    .findFirst()
                    .ifPresentOrElse(
                            existingStudent -> {
                                // Merge class groups
                                incomingClassGroups.forEach(newCg -> {
                                    if (!existingStudent.getClassGroups().contains(newCg)) {
                                        existingStudent.getClassGroups().add(newCg);
                                    }
                                });

                                // Sum unattested counts
                                existingStudent.setUnattestedCount(
                                        existingStudent.getUnattestedCount() +
                                                incomingStudent.getUnattestedCount()
                                );
                            },
                            () -> {
                                // Add new student with initialized data
                                StudentDTO newStudent = new StudentDTO();
                                newStudent.setId(incomingStudent.getId());
                                newStudent.setName(incomingStudent.getName());
                                newStudent.setSubgroup(incomingStudent.getSubgroup());
                                newStudent.getClassGroups().addAll(incomingClassGroups);
                                newStudent.setUnattestedCount(incomingStudent.getUnattestedCount());
                                group.getStudents().add(newStudent);
                            }
                    );
        }
        return new ArrayList<>(groupedResults.values());
    }
//
//    public List<GroupedResultDTO> findStudentsByDeanWithAttestations(Long deanId) {
//        // Fetch all students who meet the criteria
//        List<StudentDTO> students = deanRepository.findAllNotAttestedStudentWhoHasMoreThen2NotAttestationByDeanId(deanId);
//
//        // Group students by subgroup using Stream API
//        return students.stream()
//                .collect(Collectors.groupingBy(
//                        student -> student.getSubgroup().getId(), // Group by subgroup ID
//                        Collectors.collectingAndThen(
//                                Collectors.toList(),
//                                groupedStudents -> {
//                                    // Create a GroupedResultDTO for each subgroup
//                                    GroupedResultDTO group = new GroupedResultDTO();
//                                    group.setSubgroup(groupedStudents.get(0).getSubgroup()); // Set subgroup
//                                    group.setStudents(groupedStudents.stream()
//                                            .filter(distinctByKey(StudentDTO::getId)) // Ensure unique students
//                                            .collect(Collectors.toList()));
//                                    return group;
//                                }
//                        )
//                ))
//                .values() // Get the grouped results as a collection
//                .stream()
//                .collect(Collectors.toList()); // Convert to a list
//    }
//
//    // Helper method to ensure uniqueness based on a key
//    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
//        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
//        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
//    }

    public List<Specialty> getSetSpecialty(Integer accountId) {
        return specialtyRepository.findByDeanId(accountId);
    }

    public ClassFormat createClassFormat(CreateClassFormatRequestDTO createClassFormatRequestDTO, Integer deanId) {
        classFormatRepository.findClassFormatByFormatNameAndIdDean(createClassFormatRequestDTO.formatName(), deanId).ifPresent((e) -> {
            throw new BadRequestException("there's classFormat");
        });
        return classFormatRepository.save(ClassFormat
                .builder()
                .idDean(deanId)
                .formatName(createClassFormatRequestDTO.formatName())
                .description(createClassFormatRequestDTO.description())
                .build());
    }

    public List<ClassFormat> getListClassFormat(Integer deanId) {
        return classFormatRepository.findAllByIdDean(deanId);
    }

    public ClassFormat getClassFormat(Integer classFormatId, Integer deanId) {

        ClassFormat classFormat = classFormatRepository.findById(classFormatId).orElseThrow(() -> new BadRequestException("there's no classFormat"));
        hasBelongToDean(classFormat.getIdDean(), deanId);
        return classFormat;
    }


    public ClassFormat deleteClassFormat(Integer classFormatId, Integer deanId) {
        ClassFormat classFormat = getClassFormat(classFormatId, deanId);
        classFormatRepository.delete(classFormat);
        return classFormat;
    }

    public ClassFormat updateClassFormat(UpdateClassFormatRequestDTO updateClassFormatRequestDTO, Integer deanId) {
        classFormatRepository.findClassFormatByFormatNameAndIdDean(updateClassFormatRequestDTO.formatName(), deanId).ifPresent((e) -> {
            throw new BadRequestException("there's classFormat");
        });
        ClassFormat classFormat = getClassFormat(updateClassFormatRequestDTO.id(), deanId);

        hasBelongToDean(classFormat.getIdDean(), deanId);
        classFormat.setFormatName(updateClassFormatRequestDTO.formatName());
        classFormat.setDescription(updateClassFormatRequestDTO.description());
        return classFormatRepository.save(classFormat);
    }


    public List<Subject> getListSubjects(Integer deanId) {
        return subjectRepository.findAllByIdDean(deanId);
    }

    public Subject createSubject(CreateSubjectDto createSubjectDto, Integer deanId) {
        subjectRepository.findByNameAndIdDean(createSubjectDto.name(), deanId).ifPresent((e) -> {
            throw new BadRequestException("there's subject");
        });
        return subjectRepository.save(Subject
                .builder()
                .idDean(deanId)
                .name(createSubjectDto.name())
                .description(createSubjectDto.description())
                .build());
    }

    public Subject getSubject(Integer subjectId, Integer deanId) {

        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new BadRequestException("there's no subject"));
        hasBelongToDean(subject.getIdDean(), deanId);
        return subject;
    }


    public Subject deleteSubject(Integer subjectId, Integer deanId) {
        Subject subject = getSubject(subjectId, deanId);
        subjectRepository.delete(subject);
        return subject;
    }

    public Subject updateSubject(UpdateSubjectDto updateSubjectDto, Integer deanId) {
        subjectRepository.findByNameAndIdDean(updateSubjectDto.name(), deanId).ifPresent((e) -> {
            throw new BadRequestException("there's subject");
        });
        Subject subject = getSubject(updateSubjectDto.id(), deanId);
        hasBelongToDean(subject.getIdDean(), deanId);
        subject.setName(updateSubjectDto.name());
        subject.setDescription(updateSubjectDto.description());
        return subjectRepository.save(subject);
    }


    public Student updateStudentInfo(UserInfo userInfo, Integer id) {
        Student studentAccount = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Can't retrieve the student"));
        studentAccount.setFlpName(NameConverter.convertNameToDb(userInfo.lastname(), userInfo.name(), userInfo.surname()));

        return studentRepository.save(studentAccount);
    }

    public List<ClassGroupMapDTO> getListClassGroup(Integer deanId) {
        return classGroupRepository.findAllByIdDean(deanId);
    }

    public ClassGroupDto getClassGroup(Integer classGroupId, Integer deanId) {
        ClassGroupInfo classGroupInfo = commonService.getClassGroup(classGroupId);
        hasBelongToDean(classGroupInfo.classGroup().getIdDean(), deanId);
        List<ClassGroupsToSubgroups> classGroupsToSubgroups = classGroupsToSubgroupsRepository.findAllByIdClassGroup(classGroupInfo.classGroup().getIdClassGroup());
        List<Boolean> listAttested = classGroupsHoldRepository.findByIdClassHoldIn(classGroupsToSubgroups.stream().map(ClassGroupsToSubgroups::getIdClassHold).toList()).stream().map(ClassGroupsHold::getHasApplyAttestation).toList();
        Long count = classGroupsToSubgroups.stream().map(e -> e.getIdClassHold()).distinct().count();
        Boolean isMany = false;
        if (count == 1 && classGroupsToSubgroups.size() > 1) {
            isMany = true;
        }
        return ClassGroupDto.builder().hasApplyAttestation(listAttested).isMany(isMany).classGroup(classGroupInfo).subgroupsId(classGroupsToSubgroups).build();
    }

    public List<ClassGroup> getListClassGroupByIdSubject(Integer subjectId, Integer deanId) {
        return classGroupRepository.findAllByIdSubjectAndIdDean(subjectId, deanId);
    }

    @Transactional
    public ClassGroup deleteClassGroup(Integer classGroupId, Integer deanId) {
        ClassGroupDto classGroup = getClassGroup(classGroupId, deanId);
        try {
            classGroupsToSubgroupsRepository.deleteAllByIdClassGroup(classGroupId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        classGroupRepository.delete(classGroup.classGroup().classGroup());

        return classGroup.classGroup().classGroup();
    }

    public ClassGroup updateClassGroup(UpdateClassGroupDto updateClassGroupDto, Integer deanId) {
        ClassGroup classGroup = classGroupRepository.findById(updateClassGroupDto.classGroupId()).orElseThrow(() -> new BadRequestException("there's no class-group"));
        hasBelongToDean(classGroup.getIdDean(), deanId);
        classGroup.setIdTeacher(updateClassGroupDto.teacherId());
        classGroup.setDescription(updateClassGroupDto.description());
        classGroup.setIdSubject(updateClassGroupDto.subjectId());
        classGroup.setIdClassFormat(updateClassGroupDto.classFormatId());

        jdbcTemplate.batchUpdate("INSERT INTO classgroupshold (id_class_hold, has_apply_attestation) VALUES (?,?) ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Integer idClassHold = updateClassGroupDto.idClassId().get(i);
                        ps.setInt(1, idClassHold);
                        ps.setBoolean(2, updateClassGroupDto.hasApplyAttestation());
                    }

                    @Override
                    public int getBatchSize() {
                        return updateClassGroupDto.idClassId().size();
                    }
                }
        );

        return classGroupRepository.save(classGroup);
    }

    public void addSubGroupsToClassGroup(AssignGroupsToClass assignGroupsToClass) {

        try {
            if (assignGroupsToClass.isMany()) {


                List<ClassGroupsToSubgroups> classGroups = classGroupsToSubgroupsRepository.findAllByIdClassGroup(assignGroupsToClass.classGroupId());
                Integer classGroupsHoldId;
                if (classGroups.isEmpty()) {
                    classGroupsHoldId = classGroupsHoldRepository.save(ClassGroupsHold.builder().hasApplyAttestation(assignGroupsToClass.hasApplyAttestation()).build()).getIdClassHold();

                } else {
                    classGroupsHoldId = classGroups.get(0).getIdClassHold();
                }
                manageStudentGroupsToAssign(classGroupsHoldId, assignGroupsToClass.studentGroupIds(), assignGroupsToClass.classGroupId(), "INSERT INTO ClassGroupsToSubgroups (id_subgroup, id_class_group, id_class_hold ) VALUES (?, ?, ?)");
                updateStudentsGrateAfterAddToNewClassGroup(classGroupsHoldId, assignGroupsToClass.studentGroupIds());

            } else {
                manageStudentGroupsToAssignOne(assignGroupsToClass.studentGroupIds(), assignGroupsToClass.hasApplyAttestation(), assignGroupsToClass.classGroupId(), "INSERT INTO ClassGroupsToSubgroups (id_subgroup, id_class_group, id_class_hold ) VALUES (?, ?, ?)");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStudentsGrateAfterAddToNewClassGroup(Integer holdId, List<Integer> subgroupsId) {
        List<Integer> studensIds = studentRepository.selectByAllSubgrIds(subgroupsId);
        generateStudentGrate(holdId, studensIds);

    }

    public void generateStudentGrate(Integer holdId, List<Integer> studentsId) {
        List<Classes> classesEntity = classRepository.findAllByIdClassHold(holdId);
        if (!classesEntity.isEmpty()) {
            for (Classes cl : classesEntity
            ) {
                teacherService.createStudentGrade(studentsId, cl.getIdClass());
            }
        }
    }

    public void removeSubGroupsToClassGroup(RemoveGroupsToClass removeGroupsToClass) {
        manageStudentGroupsToClassGroup(removeGroupsToClass.studentGroupIds(), removeGroupsToClass.classGroupId(), "DELETE FROM ClassGroupsToSubgroups WHERE id_subgroup = ? AND id_class_group = ?");
    }

    public ClassGroup createClassGroup(CreateSubjectToTeacherWithFormat createSubjectToTeacherWithFormat, Integer deanId, Integer universityId) {
        Subject subject = getSubject(createSubjectToTeacherWithFormat.subjectId(), deanId);
        ClassFormat classFormat = getClassFormat(createSubjectToTeacherWithFormat.formatClassId(), deanId);
        Teacher teacher = teacherRepository.findByIdUniversityAndIdTeacher(universityId, createSubjectToTeacherWithFormat.teacherId()).orElseThrow(() -> new BadRequestException("there's no teacher with the following id"));
//        classGroupRepository.findByIdTeacherAndIdClassFormatAndAndIdSubject(teacher.getIdTeacher(), classFormat.getIdClassFormat(), subject.getIdSubject()).ifPresent((e) -> new BadRequestException("the subject already assing to the following teacher with the class format"));
        return classGroupRepository.save(ClassGroup
                .builder()
                .idTeacher(teacher.getIdTeacher())
                .idSubject(subject.getIdSubject())
                .idClassFormat(classFormat.getIdClassFormat())
                .idDean(deanId)
                .description(createSubjectToTeacherWithFormat.description())
                .build());
    }

    private void hasBelongToDean(Integer entityIdDean, Integer requestedDeanId) {
        if (!Objects.equals(entityIdDean, requestedDeanId))
            throw new BadRequestException("The dean does not belong to the requested dean");
    }

    @Transactional
    public void assignGroupsToClass(AssignGroupsToClass assignGroupsToClass) {
        try {
            if (assignGroupsToClass.isMany()) {


                List<ClassGroupsToSubgroups> classGroups = classGroupsToSubgroupsRepository.findAllByIdClassGroup(assignGroupsToClass.classGroupId());
                Integer classGroupsHoldId;
                if (classGroups.isEmpty()) {
                    classGroupsHoldId = classGroupsHoldRepository.save(ClassGroupsHold.builder().hasApplyAttestation(assignGroupsToClass.hasApplyAttestation()).build()).getIdClassHold();

                } else {
                    classGroupsHoldId = classGroups.get(0).getIdClassHold();
                }
                manageStudentGroupsToAssign(classGroupsHoldId, assignGroupsToClass.studentGroupIds(), assignGroupsToClass.classGroupId(), "INSERT INTO ClassGroupsToSubgroups (id_subgroup, id_class_group, id_class_hold ) VALUES (?, ?, ?)");

            } else {
                manageStudentGroupsToAssignOne(assignGroupsToClass.studentGroupIds(), assignGroupsToClass.hasApplyAttestation(), assignGroupsToClass.classGroupId(), "INSERT INTO ClassGroupsToSubgroups (id_subgroup, id_class_group, id_class_hold ) VALUES (?, ?, ?)");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void manageStudentGroupsToAssign(Integer idHolder, List<Integer> ids, Integer classGroupId, String sql) {

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Integer idStudentGroup = ids.get(i);
                        ps.setInt(1, idStudentGroup);
                        ps.setInt(2, classGroupId);
                        ps.setInt(3, idHolder);
                    }

                    @Override
                    public int getBatchSize() {
                        return ids.size();
                    }
                }
        );

    }

    public void manageStudentGroupsToAssignOne(List<Integer> ids, Boolean hasApplyAttestation, Integer classGroupId, String sql) {

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Integer idStudentGroup = ids.get(i);
                        ps.setInt(1, idStudentGroup);
                        ps.setInt(2, classGroupId);
                        ClassGroupsHold classGroupsHold = classGroupsHoldRepository.save(ClassGroupsHold.builder().hasApplyAttestation(hasApplyAttestation).build());

                        ps.setInt(3, classGroupsHold.getIdClassHold());
                    }

                    @Override
                    public int getBatchSize() {
                        return ids.size();
                    }
                }
        );

    }

    public void manageStudentGroupsToClassGroup(List<Integer> ids, Integer classGroupId, String sql) {

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Integer idStudentGroup = ids.get(i);
                        ps.setInt(1, idStudentGroup);
                        ps.setInt(2, classGroupId);
                    }

                    @Override
                    public int getBatchSize() {
                        return ids.size();
                    }
                }
        );

    }

    @Transactional
    public void reassignStudents(ReassignStudentsToNewGroup reassignStudentsToNewGroup) {
        studentRepository.reassignStudents(reassignStudentsToNewGroup.getSubgroupId(), reassignStudentsToNewGroup.getStudentsId().stream().toList());

        createStudentGrades(reassignStudentsToNewGroup.getStudentsId().stream().toList(), reassignStudentsToNewGroup.getSubgroupId());
    }

    public List<SubgroupWithClassGroups> findSubgroupsWithClassGroupsByDeanId(Integer deanId) {
        String sql = """
                SELECT 
                    s.id_subgroup,
                    s.subgroup_number,
                    s.admission_date,
                    cgts.id_class_group,
                    cgts.id_class_hold,
                    cg.description,
                    sub.name AS subject_name,
                    cf.format_name,
                    t.flp_name AS teacher_name
                FROM 
                    Subgroups s
                LEFT JOIN 
                    ClassGroupsToSubgroups cgts ON s.id_subgroup = cgts.id_subgroup
                LEFT JOIN 
                    ClassGroups cg ON cgts.id_class_group = cg.id_class_group
                LEFT JOIN 
                    Subjects sub ON cg.id_subject = sub.id_subject
                LEFT JOIN 
                    ClassFormats cf ON cg.id_class_format = cf.id_class_format
                LEFT JOIN 
                    Teachers t ON cg.id_teacher = t.id_teacher
                WHERE 
                    s.id_dean = ?
                ORDER BY 
                    s.id_subgroup, cgts.id_class_group
                """;

        return jdbcTemplate.query(sql, new SubgroupWithClassGroupsExtractor(), deanId);
    }

    @Transactional
    public void createStudentGrades(List<Integer> studentIds, Integer subgroupId) {
        // Step 1: Fetch all class IDs and their attestation flags
        List<ClassDto> classDtos = classRepository.findClassIdsBySubgroupId(subgroupId);

        if (classDtos.isEmpty() || studentIds.isEmpty()) {
            return; // No classes or students to process
        }

        // Step 2: Split classes into attestation and regular classes
        Map<Boolean, List<Integer>> classIdsByAttestation = classDtos.stream()
                .collect(Collectors.partitioningBy(
                        ClassDto::getIsAttestation,
                        Collectors.mapping(ClassDto::getIdClass, Collectors.toList())
                ));

        List<Integer> regularClassIds = classIdsByAttestation.get(false);
        List<Integer> attestationClassIds = classIdsByAttestation.get(true);

        if (!regularClassIds.isEmpty()) {
            String sql = "DELETE FROM StudentGrades WHERE id_student = ? and id_class =?";
            jdbcTemplate.batchUpdate(sql, studentIds, regularClassIds.size(), (PreparedStatement ps, Integer studentId) -> {
                for (int i = 0; i < regularClassIds.size(); i++) {
                    ps.setInt(1, studentId);
                    ps.setInt(2, regularClassIds.get(i));
                    ps.addBatch();
                }
            });
        }
        if (!regularClassIds.isEmpty()) {
            String sql = "DELETE FROM attestationstudentgrades WHERE id_student = ? and id_class =?";
            jdbcTemplate.batchUpdate(sql, studentIds, regularClassIds.size(), (PreparedStatement ps, Integer studentId) -> {
                for (int i = 0; i < regularClassIds.size(); i++) {
                    ps.setInt(1, studentId);
                    ps.setInt(2, regularClassIds.get(i));
                    ps.addBatch();
                }
            });
        }
        // Step 3: Batch insert into StudentGrades for regular classes
        if (!regularClassIds.isEmpty()) {
            String sql = "INSERT INTO StudentGrades (id_student, id_class, attendance) VALUES (?, ?, 0)";
            jdbcTemplate.batchUpdate(sql, studentIds, regularClassIds.size(), (PreparedStatement ps, Integer studentId) -> {
                for (int i = 0; i < regularClassIds.size(); i++) {
                    ps.setInt(1, studentId);
                    ps.setInt(2, regularClassIds.get(i));
                    ps.addBatch();
                }
            });
        }

        // Step 4: Batch insert into AttestationStudentGrades for attestation classes
        if (!attestationClassIds.isEmpty()) {
            String sql = "INSERT INTO AttestationStudentGrades (id_student, id_class) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(sql, studentIds, attestationClassIds.size(), (PreparedStatement ps, Integer studentId) -> {
                for (int i = 0; i < attestationClassIds.size(); i++) {
                    ps.setInt(1, studentId);
                    ps.setInt(2, attestationClassIds.get(i));
                    ps.addBatch();
                }
            });
        }
    }
}
