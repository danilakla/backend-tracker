package com.example.backendtracker.entities.common;

import com.example.backendtracker.domain.models.*;
import com.example.backendtracker.domain.repositories.*;
import com.example.backendtracker.entities.admin.dto.ClassGroupDto;
import com.example.backendtracker.entities.admin.dto.ClassGroupInfo;
import com.example.backendtracker.entities.common.dto.DeanMemberDto;
import com.example.backendtracker.entities.common.dto.MemberOfSystem;
import com.example.backendtracker.entities.common.dto.SubGroupMember;
import com.example.backendtracker.entities.teacher.dto.TableViewDto;
import com.example.backendtracker.reliability.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public University getUniversity(Integer idUniversity) {

        return universityRepository.findById(idUniversity).orElseThrow(() -> new BadRequestException("there's no"));
    }


    public TableViewDto showInfoTable(Integer subgroupId, Integer idClassGroupToSubgroup) {
        List<Student> students = getListStudents(subgroupId);
        List<Classes> classes = classRepository.findAllByIdClassGroupToSubgroup(idClassGroupToSubgroup);
        List<StudentGrade> studentGrades = studentGradeRepository.findAllByIdClassInAndAndIdStudentIn(
                classes.stream().map(Classes::getIdClass).toList(),
                students.stream().map(Student::getIdStudent).toList());
        return TableViewDto.builder().classes(classes).students(students).studentGrades(studentGrades).build();
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

    public List<Subgroup> getListSubgroupsByIds(List<Integer> ids) {
        List<Subgroup> subgroups = subgroupRepository.findAllByIdSubgroupIn(ids);
        return subgroups;
    }

    public MemberOfSystem getMemberSystemForAdmin(Integer iduniversity) {
        MemberOfSystem memberOfSystem = new MemberOfSystem();
        memberOfSystem.setTeacherList(getListTeachers(iduniversity));
        memberOfSystem.setDeanList(getListDeans(iduniversity));
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
            List<Student> students = getListStudents(subgroup.getIdSubgroup());
            subgroupMember.setStudents(students);
            subgroupMembers.add(subgroupMember);
        }
        return subgroupMembers;
    }

    public List<Subgroup> getSubgroupList(Integer idDean) {
        return getListSubgroups(idDean);
    }
}
