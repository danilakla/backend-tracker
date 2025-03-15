package com.example.backendtracker.service;

import com.example.backendtracker.security.service.helper.student.dto.StudentResultDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class EmailService {


    @Autowired
    private ExcelExporter excelExporter;
    @Autowired
    ExcelGenerationService excelGenerationService;

    public ResponseEntity<byte[]> getStudentResultsExcel(List<StudentResultDto> students) throws IOException {
        // Generate Excel file in memory
        ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
        excelExporter.exportToExcel(students, excelStream);

        // Prepare HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("student_results.xlsx")
                .build());

        // Return ResponseEntity with Excel file
        return new ResponseEntity<>(excelStream.toByteArray(), headers, HttpStatus.OK);
    }

    public ResponseEntity<byte[]> getCourseInfoZip(Integer deanId) {
        try {
            // Generate Excel files in memory
            List<Workbook> result = excelGenerationService.generateExcelFiles(deanId);
            List<ByteArrayOutputStream> excelStreams = result.stream()
                    .map(i -> new ByteArrayOutputStream())
                    .toList();

            for (int i = 0; i < result.size(); i++) {
                excelExporter.loadToEmail(result.get(i), excelStreams.get(i));
            }

            // Create ZIP file in memory
            ByteArrayOutputStream zipStream = new ByteArrayOutputStream();
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(zipStream)) {
                for (int i = 0; i < result.size(); i++) {
                    String fileName = "Course_" + (i + 1) + "_" + ".xlsx";
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    zipOutputStream.putNextEntry(zipEntry);

                    byte[] excelBytes = excelStreams.get(i).toByteArray();
                    zipOutputStream.write(excelBytes, 0, excelBytes.length);
                    zipOutputStream.closeEntry();
                }
            }

            // Prepare HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("course_info_results.zip")
                    .build());

            // Return ResponseEntity with ZIP file
            return new ResponseEntity<>(zipStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
