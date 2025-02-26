package com.example.backendtracker.service;

import com.example.backendtracker.security.service.helper.student.dto.StudentResultDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ExcelExporter excelExporter;
    @Autowired
    ExcelGenerationService excelGenerationService;

    public void sendEmailWithAttachment(String toEmail, List<StudentResultDto> students) throws IOException, MessagingException {
        // Создаем письмо
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Student Results");
        helper.setText("Please find the attached Excel file with student results.");

        // Генерация Excel-файла в память
        ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
        excelExporter.exportToExcel(students, excelStream);

        // Создаем вложение
        InputStreamSource attachment = new ByteArrayResource(excelStream.toByteArray());
        helper.addAttachment("student_results.xlsx", attachment);

        // Отправляем письмо
        mailSender.send(message);
    }

    public void sendCourseInfo(String toEmail, Integer deanId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Course info Results");
            helper.setText("Please find the attached Excel file with course info results.");

            // Генерация Excel-файла в память
            List<Workbook> result = excelGenerationService.generateExcelFiles(deanId);
            List<ByteArrayOutputStream> excelStreams = result.stream().map(i -> new ByteArrayOutputStream()).toList();
            for (int i = 0; i < result.size(); i++) {
                excelExporter.loadToEmail(result.get(i), excelStreams.get(i));
            }

            // Создаем вложение
            for (int i = 0; i < result.size(); i++) {

                InputStreamSource attachment = new ByteArrayResource(excelStreams.get(i).toByteArray());
                helper.addAttachment("Course_" + i+1 + "_" + ".xlsx", attachment);

            }
            // Отправляем письмо
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
