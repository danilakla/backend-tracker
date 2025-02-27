package com.example.backendtracker.entities.common;

import com.example.backendtracker.domain.models.Student;
import com.example.backendtracker.domain.models.Subgroup;
import com.example.backendtracker.domain.models.Teacher;
import com.example.backendtracker.domain.models.University;
import com.example.backendtracker.entities.common.dto.ClassesTableDto;
import com.example.backendtracker.entities.common.dto.SubgroupsContainerOfId;
import com.example.backendtracker.entities.teacher.dto.ClassGroupClassHoldDTO;
import com.example.backendtracker.entities.teacher.dto.TableViewDto;
import com.example.backendtracker.util.AccountInformationRetriever;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("common")
@AllArgsConstructor
public class CommonController {


    private final CommonService commonService;

    private final AccountInformationRetriever accountInformationRetriever;

    @GetMapping("teachers")
    public ResponseEntity<List<Teacher>> getlistTeacher(@AuthenticationPrincipal UserDetails userDetails) {
        Integer universityId = accountInformationRetriever.getUniversityId(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(commonService.getListTeachers(universityId));
    }

    @PostMapping("subgroups-by-id")
    public ResponseEntity<ClassGroupClassHoldDTO> getListSubgroups(@RequestBody SubgroupsContainerOfId subgroupsContainerOfId) {
        return ResponseEntity.status(HttpStatus.OK).body(commonService.getListSubgroupsByIds(subgroupsContainerOfId.ClassGroup(), subgroupsContainerOfId.ids()));
    }

    @GetMapping("show/table")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TableViewDto> showTableInfoMore(@RequestBody ClassesTableDto classesTableDto) {
        return ResponseEntity.ok(commonService.showInfoTable(classesTableDto));
    }

    @GetMapping("show/table/{idHold}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TableViewDto> showTableInfoOneClass(@PathVariable("idHold") Integer idHold) {
        return ResponseEntity.ok(commonService.showInfoTableOne( idHold));
    }


    @GetMapping("students/groups/{id}")
    public ResponseEntity<List<Student>> getListStudentsBySubgroupId(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(commonService.getListStudents(id));
    }

    @GetMapping("get/university")
    public ResponseEntity<University> getUniversity(@AuthenticationPrincipal UserDetails userDetails) {
        Integer universityId = accountInformationRetriever.getUniversityId(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(commonService.getUniversity(universityId));
    }

    @GetMapping("get/teacher/{id}")
    public ResponseEntity<Teacher> getTeacher(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(commonService.getTeacher(id));
    }

}
