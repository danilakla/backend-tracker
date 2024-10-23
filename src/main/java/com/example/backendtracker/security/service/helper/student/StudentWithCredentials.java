package com.example.backendtracker.security.service.helper.student;

import com.example.backendtracker.security.service.helper.student.dto.StudentExcelDto;
import lombok.Builder;

@Builder
record StudentWithCredentials(String login, String password, String parentKey, StudentExcelDto studentExcelDto) {


}