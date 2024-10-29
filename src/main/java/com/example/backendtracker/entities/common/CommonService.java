package com.example.backendtracker.entities.common;

import com.example.backendtracker.domain.models.Dean;
import com.example.backendtracker.domain.models.Student;
import com.example.backendtracker.domain.models.Subgroup;
import com.example.backendtracker.domain.models.Teacher;
import com.example.backendtracker.domain.repositories.DeanRepository;
import com.example.backendtracker.domain.repositories.StudentRepository;
import com.example.backendtracker.domain.repositories.SubgroupRepository;
import com.example.backendtracker.domain.repositories.TeacherRepository;
import com.example.backendtracker.entities.common.dto.DeanMemberDto;
import com.example.backendtracker.entities.common.dto.MemberOfSystem;
import com.example.backendtracker.entities.common.dto.SubGroupMember;
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


    public List<Dean> getListDeans(Integer idUniversity) {

        return deanRepository.findAllByIdUniversity(idUniversity);
    }


    public List<Teacher> getListTeachers(Integer idUniversity) {

        return teacherRepository.findAllByIdUniversity(idUniversity);
    }


    public List<Student> getListStudents(Integer idSubgroup) {

        return studentRepository.findAllByIdSubgroup(idSubgroup);
    }

    public List<Subgroup> getListSubgroups(Integer idDean) {
        return subgroupRepository.findAllByIdDean(idDean);
    }

    public MemberOfSystem getMemberSystem(Integer iduniversity) {
        MemberOfSystem memberOfSystem = new MemberOfSystem();
        memberOfSystem.setTeacherMemberDtos(getListTeachers(iduniversity));
        memberOfSystem.setDeanMemberDtos(getDeanMembers(iduniversity));
        return memberOfSystem;
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
}
