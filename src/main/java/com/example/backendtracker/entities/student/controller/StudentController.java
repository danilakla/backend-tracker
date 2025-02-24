package com.example.backendtracker.entities.student.controller;

import com.example.backendtracker.domain.models.Student;
import com.example.backendtracker.domain.repositories.mapper.ClassGroupMapForStudentDTO;
import com.example.backendtracker.entities.common.CommonService;
import com.example.backendtracker.entities.common.dto.ClassesTableDto;
import com.example.backendtracker.entities.student.service.StudentService;
import com.example.backendtracker.entities.teacher.dto.StudentAttendanceDto;
import com.example.backendtracker.entities.teacher.dto.TableViewDto;
import com.example.backendtracker.entities.teacher.service.TeacherService;
import com.example.backendtracker.util.AccountInformationRetriever;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CommonService commonService;
    private final AccountInformationRetriever accountInformationRetriever;

    @GetMapping("show/table")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TableViewDto> showTableInfoMore(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ClassesTableDto classesTableDto) {
        Student student = (Student) accountInformationRetriever.getPerson(userDetails);

        return ResponseEntity.ok(commonService.showInfoTableStudent(student, classesTableDto));
    }

    @GetMapping("show/table/{idHold}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TableViewDto> showTableInfoOneClass(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("idHold") Integer idHold) {
        Student student = (Student) accountInformationRetriever.getPerson(userDetails);

        return ResponseEntity.ok(commonService.showInfoTableOneStudent(student, idHold));
    }

    @GetMapping("get/class-groups")
    public ResponseEntity<List<ClassGroupMapForStudentDTO>> getListClassGroups(@AuthenticationPrincipal UserDetails userDetails) {
        Student student = (Student) accountInformationRetriever.getPerson(userDetails);
        return ResponseEntity.ok(studentService.getClassGroup(student.getIdSubgroup()));
    }

    @PostMapping("accept/attendance")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> acceptAttendance(@RequestBody StudentAttendanceDto studentAttendanceDto) throws BadPaddingException {
        teacherService.acceptAttendance(studentAttendanceDto.studentGrateId(), studentAttendanceDto.attendanceCode());
        return ResponseEntity.noContent().build();
    }

}
