package com.example.backendtracker.service;

import com.example.backendtracker.security.service.helper.student.dto.StudentResultDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ExcelExporter excelExporter;

    public void sendEmailWithAttachment(String toEmail, List<StudentResultDto> students)throws IOException, MessagingException {
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
}
