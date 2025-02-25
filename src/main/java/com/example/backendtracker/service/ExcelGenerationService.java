package com.example.backendtracker.service;

import com.example.backendtracker.domain.models.*;
import com.example.backendtracker.service.ExcelExporter;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelGenerationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ExcelExporter excelExporter;

    public void generateExcelFiles(Long deanId) throws Exception {
        // Get distinct years for subgroups associated with the dean
        String sqlYears = "SELECT DISTINCT EXTRACT(YEAR FROM admission_date) " +
                "FROM Subgroups " +
                "WHERE id_dean = ?";
        List<Integer> years = jdbcTemplate.queryForList(sqlYears, Integer.class, deanId);

        for (Integer year : years) {
            Workbook workbook = excelExporter.createWorkbook();

            // Find all id_class_hold for subgroups from this year, filtered by dean
            String sqlClassHolds = "SELECT DISTINCT cgts.id_class_hold " +
                    "FROM ClassGroupsToSubgroups cgts " +
                    "JOIN Subgroups s ON cgts.id_subgroup = s.id_subgroup " +
                    "WHERE EXTRACT(YEAR FROM s.admission_date) = ? AND s.id_dean = ?";
            List<Long> classHolds = jdbcTemplate.queryForList(sqlClassHolds, Long.class, year, deanId);

            for (Long classHold : classHolds) {
                // Retrieve class group details for sheet name, filtered by dean
                String sqlClassGroup = "SELECT cg.*, s.name AS subject_name, t.flp_name AS teacher_name " +
                        "FROM ClassGroupsToSubgroups cgts " +
                        "JOIN ClassGroups cg ON cgts.id_class_group = cg.id_class_group " +
                        "JOIN Subjects s ON cg.id_subject = s.id_subject " +
                        "JOIN Deans d ON s.id_dean = d.id_dean " + // Link through Subjects to Deans
                        "JOIN Teachers t ON cg.id_teacher = t.id_teacher " +
                        "WHERE cgts.id_class_hold = ? AND s.id_dean = ? LIMIT 1";
                Map<String, Object> classGroupMap = jdbcTemplate.queryForMap(sqlClassGroup, classHold, deanId);
                String subjectName = (String) classGroupMap.get("subject_name");
                String sheetName = sanitizeSheetName("Subject_" + subjectName + "_Hold_" + classHold);

                // Find subgroups and students, filtered by dean
                String sqlSubgroups = "SELECT s.* FROM Subgroups s " +
                        "JOIN ClassGroupsToSubgroups cgts ON s.id_subgroup = cgts.id_subgroup " +
                        "WHERE cgts.id_class_hold = ? AND EXTRACT(YEAR FROM s.admission_date) = ? AND s.id_dean = ?";
                List<Subgroup> subgroups = jdbcTemplate.query(
                        sqlSubgroups, new BeanPropertyRowMapper<>(Subgroup.class), classHold, year, deanId
                );
                List<Integer> subgroupIds = subgroups.stream()
                        .map(Subgroup::getIdSubgroup)
                        .collect(Collectors.toList());

                NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
                Map<String, Object> params = new HashMap<>();
                params.put("subgroupIds", subgroupIds);
                params.put("deanId", deanId);
                String sqlStudents = "SELECT st.* FROM Students st " +
                        "JOIN Subgroups s ON st.id_subgroup = s.id_subgroup " +
                        "WHERE st.id_subgroup IN (:subgroupIds) AND s.id_dean = :deanId";
                List<Student> students = namedJdbcTemplate.query(
                        sqlStudents, params, new BeanPropertyRowMapper<>(Student.class)
                );

                // Find classes, filtered by dean through ClassGroups and ClassGroupsHold
                String sqlClasses = "SELECT c.* FROM Classes c " +
                        "JOIN ClassGroupsHold cgh ON c.id_class_hold = cgh.id_class_hold " +
                        "JOIN ClassGroupsToSubgroups cgts ON cgh.id_class_hold = cgts.id_class_hold " +
                        "JOIN ClassGroups cg ON cgts.id_class_group = cg.id_class_group " +
                        "JOIN Subjects s ON cg.id_subject = s.id_subject " +
                        "JOIN Deans d ON s.id_dean = d.id_dean " +
                        "WHERE c.id_class_hold = ? AND s.id_dean = ?";
                List<Classes> classes = jdbcTemplate.query(
                        sqlClasses, new BeanPropertyRowMapper<>(Classes.class), classHold, deanId
                );

                // Build headers (matching your structure: Classes 1, Classes 2, Classes 3, Attestation1, etc.)
                List<String> headers = new ArrayList<>();
                headers.add("Имя студента"); // "Student Name" in Russian
                for (Classes cls : classes) {
                    String className = "Classes " + cls.getIdClass();
                    if (cls.getIsAttestation()) {
                        className = "Attestation" + cls.getIdClass();
                    }
                    headers.add(className);
                }

                // Retrieve grades and attestation grades, filtered by dean
                List<Long> studentIds = students.stream()
                        .map(Student::getIdStudent)
                        .map(Long::valueOf) // Convert Integer to Long for consistency
                        .collect(Collectors.toList());
                List<Integer> classIds = classes.stream()
                        .map(Classes::getIdClass)
                        .collect(Collectors.toList());

                params = new HashMap<>();
                params.put("studentIds", studentIds);
                params.put("deanId", deanId);

                // Handle empty classIds to avoid SQL syntax error
                List<StudentGrade> studentGrades = new ArrayList<>();
                if (!classIds.isEmpty()) {
                    params.put("classIds", classIds);
                    String sqlStudentGrades = "SELECT sg.* FROM StudentGrades sg " +
                            "JOIN Classes c ON sg.id_class = c.id_class " +
                            "JOIN ClassGroupsHold cgh ON c.id_class_hold = cgh.id_class_hold " +
                            "JOIN ClassGroupsToSubgroups cgts ON cgh.id_class_hold = cgts.id_class_hold " +
                            "JOIN ClassGroups cg ON cgts.id_class_group = cg.id_class_group " +
                            "JOIN Subjects s ON cg.id_subject = s.id_subject " +
                            "JOIN Deans d ON s.id_dean = d.id_dean " +
                            "WHERE sg.id_student IN (:studentIds) AND sg.id_class IN (:classIds) AND s.id_dean = :deanId";
                    studentGrades = namedJdbcTemplate.query(
                            sqlStudentGrades, params, new BeanPropertyRowMapper<>(StudentGrade.class)
                    );
                }

                // Deduplicate StudentGrade records based on idStudent and idClass
                Map<String, StudentGrade> studentGradesMap = studentGrades.stream()
                        .collect(Collectors.toMap(
                                sg -> sg.getIdStudent() + "_" + sg.getIdClass(),
                                sg -> sg,
                                (existing, replacement) -> existing // Keep the first occurrence to handle duplicates
                        ));

                List<Integer> attestationClassIds = classes.stream()
                        .filter(Classes::getIsAttestation)
                        .map(Classes::getIdClass)
                        .collect(Collectors.toList());

                Map<String, AttestationStudentGrade> attestationGradesMap = new HashMap<>();
                if (!attestationClassIds.isEmpty()) {
                    params.put("attestationClassIds", attestationClassIds);
                    String sqlAttestationGrades = "SELECT asg.* FROM AttestationStudentGrades asg " +
                            "JOIN Classes c ON asg.id_class = c.id_class " +
                            "JOIN ClassGroupsHold cgh ON c.id_class_hold = cgh.id_class_hold " +
                            "JOIN ClassGroupsToSubgroups cgts ON cgh.id_class_hold = cgts.id_class_hold " +
                            "JOIN ClassGroups cg ON cgts.id_class_group = cg.id_class_group " +
                            "JOIN Subjects s ON cg.id_subject = s.id_subject " +
                            "JOIN Deans d ON s.id_dean = d.id_dean " +
                            "WHERE asg.id_student IN (:studentIds) AND asg.id_class IN (:attestationClassIds) AND s.id_dean = :deanId";
                    List<AttestationStudentGrade> attestationGrades = namedJdbcTemplate.query(
                            sqlAttestationGrades, params, new BeanPropertyRowMapper<>(AttestationStudentGrade.class)
                    );

                    // Deduplicate AttestationStudentGrade records based on idStudent and idClass
                    attestationGradesMap = attestationGrades.stream()
                            .collect(Collectors.toMap(
                                    asg -> asg.getIdStudent() + "_" + asg.getIdClass(),
                                    asg -> asg,
                                    (existing, replacement) -> existing // Keep the first occurrence to handle duplicates
                            ));
                }

                // Build data rows with detailed cell content
                List<List<String>> data = new ArrayList<>();
                for (Student student : students) {
                    List<String> row = new ArrayList<>();
                    row.add(student.getFlpName()); // Student name

                    for (Classes cls : classes) {
                        Integer idClass = cls.getIdClass();
                        String key = student.getIdStudent() + "_" + idClass;
                        String cellContent = "";

                        if (!cls.getIsAttestation()) {
                            // Non-attestation class: Show attendance, grade, pass status, and notes
                            StudentGrade sg = studentGradesMap.get(key);
                            if (sg != null) {
                                cellContent = "Оценка: " + (sg.getGrade() != null ? sg.getGrade() : "—") + "\n" +
                                        "Посещаемость: " + (sg.getAttendance() != null ? sg.getAttendance() + " ч." : "—") + "\n" +
                                        "Пройдено: " + (sg.getIsPassLab() != null ? (sg.getIsPassLab() ? "Пройдено" : "Не пройдено") : "—") + "\n" +
                                        "Примечание: " + (sg.getDescription() != null ? sg.getDescription() : "—");
                            }
                        } else {
                            // Attestation class: Show all attestation details
                            AttestationStudentGrade asg = attestationGradesMap.get(key);
                            StudentGrade sg = studentGradesMap.get(key);
                            if (asg != null || sg != null) {
                                cellContent = "Средняя оценка: " + (asg != null && asg.getAvgGrade() != null ? asg.getAvgGrade() : "—") + "\n" +
                                        "Посещаемость: " + (asg != null && asg.getHour() != null ? asg.getHour() + " ч." : (sg != null && sg.getAttendance() != null ? sg.getAttendance() + " ч." : "—")) + "\n" +
                                        "Лабы: " + (asg != null ? asg.getCurrentCountLab() + "/" + asg.getMaxCountLab() + " макс." : "—") + "\n" +
                                        "Аттестован: " + (asg != null && asg.getIsAttested() != null ? (asg.getIsAttested() ? "Да" : "Нет") : "—") + "\n";
                            }
                        }
                        row.add(cellContent);
                    }
                    data.add(row);
                }

                // Create sheet in workbook
                excelExporter.createSheet(workbook, sheetName, headers, data);
            }

            // Save the workbook
            excelExporter.saveWorkbook(workbook, "Course1_" + year + ".xlsx");
        }
    }

    private String sanitizeSheetName(String name) {
        // Replace invalid characters (:, \, /, ?, *, [, ]) with underscores
        String sanitized = name.replaceAll("[:\\\\/*?\\[\\]]", "_");
        // Truncate to 31 characters (Apache POI maximum sheet name length)
        if (sanitized.length() > 31) {
            sanitized = sanitized.substring(0, 31);
        }
        // Remove leading/trailing whitespace and ensure not empty
        sanitized = sanitized.trim();
        if (sanitized.isEmpty()) {
            sanitized = "Sheet1";
        }
        return sanitized;
    }
}
