package com.example.backendtracker.entities.admin.service;

import com.example.backendtracker.domain.models.Dean;
import com.example.backendtracker.domain.models.Teacher;
import com.example.backendtracker.domain.models.University;
import com.example.backendtracker.domain.repositories.*;
import com.example.backendtracker.entities.admin.dto.UniversityCreateDto;
import com.example.backendtracker.entities.admin.dto.UniversityUpdateDto;
import com.example.backendtracker.entities.common.CommonService;
import com.example.backendtracker.entities.common.dto.MemberOfSystem;
import com.example.backendtracker.reliability.exception.BadRequestException;
import com.example.backendtracker.security.service.UserAccountService;
import com.example.backendtracker.security.util.SecretDataUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class AdminService {
    private final UniversityRepository universityRepository;
    private final SecretDataUtil secretDataUtil;
    private final CommonService commonService;
    private final UserAccountService userAccountService;
    private final DeanRepository deanRepository;
    private final SpecialtyRepository specialtyRepository;
    private final ClassFormatRepository classFormatRepository;
    private final SubgroupRepository subgroupRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final ClassGroupRepository classGroupRepository;

    public String generateKey(Integer adminId, String... data) throws Exception {
        University university = universityRepository.findByAdminId(adminId)
                .orElseThrow(() -> new BadRequestException("There's no university"));
        StringBuilder value = new StringBuilder();
        Arrays.stream(data).forEach(str -> value.append(str + "%"));

        return secretDataUtil.encrypt(university.getIdUniversity() + "%" + StringUtils.substring(value.toString(), 0, value.length() - 1));
    }


    public University updateUniversity(UniversityUpdateDto universityUpdateDto) {
        University university = universityRepository.findById(universityUpdateDto.getId()).orElseThrow(() -> new BadRequestException("There is no university, by id"));
        university.setName(universityUpdateDto.getName());
        university.setDescription(universityUpdateDto.getDescription());
        return universityRepository.save(university);

    }

    public University getUniversity(Integer adminId) {
        return universityRepository.findByAdminId(adminId).orElseThrow(() -> new BadRequestException("There is no university, by id"));
    }


    public MemberOfSystem getMembers(Integer adminId) {
        Integer universityId = getUniversity(adminId).getIdUniversity();
        return commonService.getMemberSystemForAdmin(universityId);
    }

    public void createUniversity(UniversityCreateDto universityCreateDto, Integer adminId) {


        universityRepository.findByAdminId(adminId).ifPresent(university -> {
            throw new BadRequestException("The university already exists");
        });
        universityRepository.save(University.builder()
                .idAdmin(adminId)
                .name(universityCreateDto.getName())
                .description(universityCreateDto.getDescription())
                .build());
    }

    public void reassignAndDeleteDean(Integer oldDeanId, Integer newDeanId) {
        specialtyRepository.updateDeanId(newDeanId, oldDeanId);
        classFormatRepository.updateDeanId(newDeanId, oldDeanId);
        subgroupRepository.updateDeanId(newDeanId, oldDeanId);
        subjectRepository.updateDeanId(newDeanId,oldDeanId);
        deanRepository.deleteById(oldDeanId);
    }

    @Transactional
    public void deleteDean(Integer deanId, Integer newDeanId) {
        Dean deanDeleted = deanRepository.findById(deanId).orElseThrow(() -> new BadRequestException("There is no dean, by id"));
        deanRepository.findById(newDeanId).orElseThrow(() -> new BadRequestException("There is no dean, by id"));
        reassignAndDeleteDean(deanId, newDeanId);
        userAccountService.deleteAccount(deanDeleted.getIdAccount());
    }

    @Transactional
    public void deleteTeacher(Integer teacherId, Integer newTeacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new BadRequestException("There is no teacher, by id"));
        teacherRepository.findById(newTeacherId).orElseThrow(() -> new BadRequestException("There is no teacher, by id"));
        reassignTeacher(teacherId, newTeacherId);
        userAccountService.deleteAccount(teacher.getIdAccount());

    }


    public void reassignTeacher(int oldTeacherId, int newTeacherId) {
        classGroupRepository.updateTeacherId(newTeacherId, oldTeacherId);
    }
}
