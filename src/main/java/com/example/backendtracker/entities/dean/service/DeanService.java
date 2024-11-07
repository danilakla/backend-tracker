package com.example.backendtracker.entities.dean.service;

import com.example.backendtracker.domain.models.*;
import com.example.backendtracker.domain.repositories.*;
import com.example.backendtracker.entities.common.CommonService;
import com.example.backendtracker.entities.common.dto.SubGroupMember;
import com.example.backendtracker.entities.dean.dto.*;
import com.example.backendtracker.reliability.exception.BadRequestException;
import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import com.example.backendtracker.security.service.UserAccountService;
import com.example.backendtracker.security.util.LoginGenerator;
import com.example.backendtracker.security.util.PasswordGenerator;
import com.example.backendtracker.util.NameConverter;
import com.example.backendtracker.util.PersonAccountManager;
import com.example.backendtracker.util.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.Name;
import java.util.List;

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


    public List<SubGroupMember> getSubGroupMembers(Integer deanId) {

        return commonService.getMemberSystemForDean(deanId);
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
                .keyStudentParents(LoginGenerator.generateLogin(studentAddDto) + PasswordGenerator.generatePassword())
                .flpName(NameConverter.convertNameToDb(studentAddDto.lastname(), studentAddDto.name(), studentAddDto.surname()))
                .idSubgroup(studentAddDto.numberOfGroupId()).build();

        return studentRepository.save(student);

    }

    public void checkExistenceOfSpecialty(String name) {
        specialtyRepository.findByName(name).ifPresent(specialty -> {
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
        Specialty specialtyToUpdate = getSpecialty(specialty.id(), deanId);
        hasBelongToDean(specialtyToUpdate.getIdDean(), deanId);
        specialtyToUpdate.setName(specialty.name());
        return specialtyRepository.save(specialtyToUpdate);
    }

    public Specialty createSpecialty(CreateSpecialtyDto specialtyDto, Integer accountId) {
        checkExistenceOfSpecialty(specialtyDto.name());
        return specialtyRepository.save(Specialty.builder()
                .name(specialtyDto.name())
                .idDean(accountId).build());

    }

    public List<Specialty> getSetSpecialty(Integer accountId) {
        return specialtyRepository.findByDeanId(accountId);
    }

    public ClassFormat createClassFormat(CreateClassFormatRequestDTO createClassFormatRequestDTO, Integer deanId) {
        classFormatRepository.findClassFormatByFormatName(createClassFormatRequestDTO.formatName()).ifPresent((e) -> {
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
        subjectRepository.findByName(createSubjectDto.name()).ifPresent((e) -> {
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


    public ClassGroup createClassGroup(CreateSubjectToTeacherWithFormat createSubjectToTeacherWithFormat, Integer deanId, Integer universityId) {
        Subject subject = getSubject(createSubjectToTeacherWithFormat.subjectId(), deanId);
        ClassFormat classFormat = getClassFormat(createSubjectToTeacherWithFormat.formatClassId(), deanId);
        Teacher teacher = teacherRepository.findByIdUniversityAndIdTeacher(universityId, createSubjectToTeacherWithFormat.teacherId()).orElseThrow(() -> new BadRequestException("there's no teacher with the following id"));
     classGroupRepository.findByIdTeacherAndIdClassFormatAndAndIdSubject(teacher.getIdTeacher(), classFormat.getIdClassFormat(),subject.getIdSubject()).ifPresent((e)->new BadRequestException("the subject already assing to the following teacher with the class format"));
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
        if (entityIdDean != requestedDeanId)
            throw new BadRequestException("The dean does not belong to the requested dean");
    }
}
