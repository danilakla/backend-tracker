package com.example.backendtracker.entities.common;

import com.example.backendtracker.domain.models.*;
import com.example.backendtracker.domain.repositories.*;
import com.example.backendtracker.domain.repositories.mapper.StudentWithLogin;
import com.example.backendtracker.entities.admin.dto.ClassGroupDto;
import com.example.backendtracker.entities.admin.dto.ClassGroupInfo;
import com.example.backendtracker.entities.common.dto.ClassesTableDto;
import com.example.backendtracker.entities.common.dto.DeanMemberDto;
import com.example.backendtracker.entities.common.dto.MemberOfSystem;
import com.example.backendtracker.entities.common.dto.SubGroupMember;
import com.example.backendtracker.entities.teacher.dto.ClassGroupClassHoldDTO;
import com.example.backendtracker.entities.teacher.dto.ClassHoldSubgroupDTO;
import com.example.backendtracker.entities.teacher.dto.TableViewDto;
import com.example.backendtracker.reliability.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommonService {

    private final DeanRepository deanRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final SubgroupRepository subgroupRepository;
    private final UniversityRepository universityRepository;
    private final ClassGroupRepository classGroupRepository;
    private final SubjectRepository subjectRepository;
    private final ClassFormatRepository classFormatRepository;
    private final ClassRepository classRepository;
    private final StudentGradeRepository studentGradeRepository;
    private final ClassGroupsToSubgroupsRepository classGroupsToSubgroupsRepository;
    private final AttestationStudentGradeRepository attestationStudentGradeRepository;

    public University getUniversity(Integer idUniversity) {

        return universityRepository.findById(idUniversity).orElseThrow(() -> new BadRequestException("there's no"));
    }


    public TableViewDto showInfoTable(ClassesTableDto classesTableDto) {
        List<Student> students = getListStudentsByListIdGroups(classesTableDto.groupsId());
        List<Classes> classes = classRepository.findAllByIdClassHold(classesTableDto.holdId());
        List<StudentGrade> studentGrades = studentGradeRepository.findAllByIdClassInAndAndIdStudentIn(
                classes.stream().map(Classes::getIdClass).toList(),
                students.stream().map(Student::getIdStudent).toList());

        List<Classes> classForAttestation = classes.stream().filter(Classes::getIsAttestation).collect(Collectors.toList());
        List<AttestationStudentGrade> attestationStudentGrades = new ArrayList<>();


        if (!classes.isEmpty() && !classForAttestation.isEmpty()) {
            try {
                attestationStudentGrades = attestationStudentGradeRepository.findAllByIdClassInAndAndIdStudentIn(
                        classForAttestation.stream().map(Classes::getIdClass).toList(),
                        students.stream().map(Student::getIdStudent).toList()
                );
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("d");
            }

        }
        return TableViewDto.builder().classes(classes).students(students).attestationStudentGrades(attestationStudentGrades).studentGrades(studentGrades).build();
    }

    public List<Student> getListStudentsByListIdGroups(List<Integer> idSubgroups) {

        return studentRepository.findAllByIdSubgroupIn(idSubgroups);
    }

    public TableViewDto showInfoTableOne(Integer idHold) {

        List<ClassGroupsToSubgroups> classGroupsToSubgroups = classGroupsToSubgroupsRepository.findAllByIdClassHold(idHold);
        List<Student> students = getListStudentsByListIdGroups(classGroupsToSubgroups.stream().map(ClassGroupsToSubgroups::getIdSubgroup).toList());
        List<Classes> classes = classRepository.findAllByIdClassHold(idHold);
        List<StudentGrade> studentGrades = studentGradeRepository.findAllByIdClassInAndAndIdStudentIn(
                classes.stream().map(Classes::getIdClass).toList(),
                students.stream().map(Student::getIdStudent).toList());
        List<Classes> classForAttestation = classes.stream().filter(Classes::getIsAttestation).collect(Collectors.toList());
        List<AttestationStudentGrade> attestationStudentGrades = new ArrayList<>();


        if (!classes.isEmpty() && !classForAttestation.isEmpty()) {
            try {
                attestationStudentGrades = attestationStudentGradeRepository.findAllByIdClassInAndAndIdStudentIn(
                        classForAttestation.stream().map(Classes::getIdClass).toList(),
                        students.stream().map(Student::getIdStudent).toList()
                );
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("d");
            }

        }
        return TableViewDto.builder().classes(classes).students(students).attestationStudentGrades(attestationStudentGrades).studentGrades(studentGrades).build();
    }

    public ClassGroupInfo getClassGroup(Integer idClassGroup) {
        ClassGroup classGroup = classGroupRepository.findById(idClassGroup).orElseThrow(() -> new BadRequestException("there's no class-group"));
        Subject subject = subjectRepository.findById(classGroup.getIdSubject()).orElseThrow(() -> new BadRequestException("there's no subject"));
        ClassFormat classFormat = classFormatRepository.findById(classGroup.getIdClassFormat()).orElseThrow(() -> new BadRequestException("there's no class-format"));
        return ClassGroupInfo.builder().classGroup(classGroup).subjectName(subject.getName()).nameClassFormat(classFormat.getFormatName()).build();

    }

    public List<Dean> getListDeans(Integer idUniversity) {

        return deanRepository.findAllByIdUniversity(idUniversity);
    }


    public List<Teacher> getListTeachers(Integer idUniversity) {

        return teacherRepository.findAllByIdUniversity(idUniversity);
    }

    public Teacher getTeacher(Integer teacherId) {
        return teacherRepository.findById(teacherId).get();
    }


    public List<Student> getListStudents(Integer idSubgroup) {

        return studentRepository.findAllByIdSubgroup(idSubgroup);
    }

    public List<Subgroup> getListSubgroups(Integer idDean) {
        return subgroupRepository.findAllByIdDean(idDean);
    }

    public ClassGroupClassHoldDTO getListSubgroupsByIds(Integer id, List<Integer> ids) {
        List<Subgroup> subgroups = subgroupRepository.findAllByIdSubgroupIn(ids);
        List<ClassGroupsToSubgroups> classGroups = classGroupsToSubgroupsRepository.findAllByIdClassGroupAndAndIdSubgroupIn(id, ids);

        Map<Integer, Subgroup> subgroupMap = subgroups.stream()
                .collect(Collectors.toMap(Subgroup::getIdSubgroup, subgroup -> subgroup));

        List<ClassHoldSubgroupDTO> result = classGroups.stream()
                .map(classGroup -> {
                    Subgroup subgroup = subgroupMap.get(classGroup.getIdSubgroup());
                    return ClassHoldSubgroupDTO.builder().idHold(classGroup.getIdClassHold()).subgroup(subgroup).build();
                })
                .toList();

        boolean allIdHoldsSame = result.stream()
                .map(ClassHoldSubgroupDTO::getIdHold) // Извлекаем idHold
                .distinct()                          // Оставляем только уникальные значения
                .count() == 1;                       // Если одно уникальное значение, то true

        return ClassGroupClassHoldDTO.builder().isOneClass(allIdHoldsSame).classHoldSubgroupDTO(result).build();

    }

    public MemberOfSystem getMemberSystemForAdmin(Integer iduniversity) {
        MemberOfSystem memberOfSystem = new MemberOfSystem();
        memberOfSystem.setTeacherList(teacherRepository.findAllByIdUniversityLogin(iduniversity));
        memberOfSystem.setDeanList(deanRepository.findAllByIdUniversityWithLogin(iduniversity));
        return memberOfSystem;
    }

    public List<SubGroupMember> getMemberSystemForDean(Integer idDean) {

        return getSubgroupMember(idDean);
    }


    public List<DeanMemberDto> getDeanMembers(Integer idUniversity) {
        List<DeanMemberDto> deanMemberDtos = new ArrayList<>();
        List<Dean> deans = getListDeans(idUniversity);

        for (Dean dean : deans) {
            DeanMemberDto deanMemberDto = new DeanMemberDto();
            deanMemberDto.setDean(dean);
            List<SubGroupMember> subGroupMemberList = getSubgroupMember(dean.getIdDean());
            deanMemberDto.setSubGroupMemberList(subGroupMemberList);
            deanMemberDtos.add(deanMemberDto);
        }
        return deanMemberDtos;
    }


    public List<SubGroupMember> getSubgroupMember(Integer idDean) {
        List<SubGroupMember> subgroupMembers = new ArrayList<>();
        List<Subgroup> subgroups = getListSubgroups(idDean);
        for (Subgroup subgroup : subgroups) {
            SubGroupMember subgroupMember = new SubGroupMember();
            subgroupMember.setSubgroup(subgroup);
            List<StudentWithLogin> students = studentRepository.findAllByIdSubgroupWithLogin(subgroup.getIdSubgroup());
            subgroupMember.setStudents(students);
            subgroupMembers.add(subgroupMember);
        }
        return subgroupMembers;
    }

    public List<Subgroup> getSubgroupList(Integer idDean) {
        return getListSubgroups(idDean);
    }
}
