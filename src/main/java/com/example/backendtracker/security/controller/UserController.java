package com.example.backendtracker.security.controller;

import com.example.backendtracker.security.controller.dto.UserInfoDto;
import com.example.backendtracker.security.service.UserAccountService;
import com.example.backendtracker.util.AccountInformationRetriever;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {

    private final AccountInformationRetriever accountInformationRetriever;


    @GetMapping("info")
    public Object userInfo(@AuthenticationPrincipal UserDetails userDetails) {
        UserInfoDto userInfoDto = accountInformationRetriever.getAccountInfo(userDetails);
        return userInfoDto;

    }


}
