package com.example.backendtracker.service;

import com.example.backendtracker.security.service.helper.student.dto.StudentResultDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class ExcelExporter {

    // Метод для экспорта списка StudentResultDto в Excel (запись в OutputStream)
    public void exportToExcel(List<StudentResultDto> studentResults, OutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Student Results");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Name", "Lastname", "Surname", "Group", "Specialty", "Login", "Password", "Parent Key"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (StudentResultDto student : studentResults) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(student.name());
            row.createCell(1).setCellValue(student.lastname());
            row.createCell(2).setCellValue(student.surname());
            row.createCell(3).setCellValue(student.numberOfGroup());
            row.createCell(4).setCellValue(student.specialty());
            row.createCell(5).setCellValue(student.login());
            row.createCell(6).setCellValue(student.password());
            row.createCell(7).setCellValue(student.parentKey());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(outputStream);
        workbook.close();
    }




    public void loadToEmail(Workbook workbook, OutputStream outputStream) throws IOException {
        workbook.write(outputStream);
        workbook.close();
    }
}