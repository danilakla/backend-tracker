package com.example.backendtracker.security.controller;

import com.example.backendtracker.security.controller.dto.LoginChangerDto;
import com.example.backendtracker.security.controller.dto.PasswordChangerDto;
import com.example.backendtracker.security.controller.dto.UserInfoDto;
import com.example.backendtracker.security.service.UserAccountService;
import com.example.backendtracker.util.AccountInformationRetriever;
import com.example.backendtracker.util.PersonAccountManager;
import com.example.backendtracker.util.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {

    private final AccountInformationRetriever accountInformationRetriever;
    private final UserAccountService userAccountService;
    private final PersonAccountManager personAccountManager;


    @GetMapping("info")
    public Object userInfo(@AuthenticationPrincipal UserDetails userDetails) {
        UserInfoDto userInfoDto = accountInformationRetriever.getAccountInfo(userDetails);
        return userInfoDto;

    }


    @PostMapping("update-password")
    public void updatePassword(@RequestBody PasswordChangerDto passwordChangerDto, @AuthenticationPrincipal UserDetails userDetails) {
        userAccountService.changePassword(userDetails.getUsername(), passwordChangerDto.currentPassword(), passwordChangerDto.newPassword());

    }

    @PostMapping("update-login")
    public void updateLogin(@RequestBody LoginChangerDto loginChangerDto, @AuthenticationPrincipal UserDetails userDetails) {
        userAccountService.changeEmail(userDetails.getUsername(), loginChangerDto.newLogin());

    }

    @PostMapping("update-user-info")
    public void updateUserInfo(@RequestBody UserInfo userInfo, @AuthenticationPrincipal UserDetails userDetails) {
        personAccountManager.updateUserInfo(userDetails, userInfo);

    }
}
