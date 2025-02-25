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
    public Workbook createWorkbook() {
        return new XSSFWorkbook();
    }

    public void createSheet(Workbook workbook, String sheetName, List<String> headers, List<List<String>> data) {
        Sheet sheet = workbook.createSheet(sheetName);

        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }

        // Create data rows with formatted cells
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 1);
            List<String> rowData = data.get(i);
            int j;
            for (j = 0; j < rowData.size(); j++) {
                Cell cell = row.createCell(j);
                String cellContent = rowData.get(j);
                if (cellContent != null && !cellContent.isEmpty()) {
                    // Enable text wrapping and set the content
                    CellStyle style = workbook.createCellStyle();
                    style.setWrapText(true);
                    cell.setCellStyle(style);
                    cell.setCellValue(cellContent);
                }
            }
            // Auto-size columns for better visibility
        }
    }

    public void saveWorkbook(Workbook workbook, String fileName) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}
