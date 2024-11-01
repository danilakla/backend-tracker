package com.example.backendtracker.entities.dean.service;

import com.example.backendtracker.domain.models.Specialty;
import com.example.backendtracker.domain.models.Student;
import com.example.backendtracker.domain.models.UserAccount;
import com.example.backendtracker.domain.repositories.SpecialtyRepository;
import com.example.backendtracker.domain.repositories.StudentRepository;
import com.example.backendtracker.entities.common.CommonService;
import com.example.backendtracker.entities.common.dto.SubGroupMember;
import com.example.backendtracker.entities.dean.dto.CreateSpecialtyDto;
import com.example.backendtracker.entities.dean.dto.StudentAddDto;
import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import com.example.backendtracker.security.service.UserAccountService;
import com.example.backendtracker.security.util.LoginGenerator;
import com.example.backendtracker.security.util.PasswordGenerator;
import com.example.backendtracker.util.NameConverter;
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

    public void createSpecialty(CreateSpecialtyDto specialtyDto, Integer accountId) {
        checkExistenceOfSpecialty(specialtyDto.name());
        specialtyRepository.save(Specialty.builder()
                .name(specialtyDto.name())
                .idDean(accountId).build());

    }

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
            throw new RuntimeException("The specialty already exists");
        });

    }
}
