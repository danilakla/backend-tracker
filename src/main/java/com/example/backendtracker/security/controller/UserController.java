package com.example.backendtracker.security.controller;

import com.example.backendtracker.security.controller.dto.LoginChangerDto;
import com.example.backendtracker.security.controller.dto.PasswordChangerDto;
import com.example.backendtracker.security.controller.dto.UpdateLoginDto;
import com.example.backendtracker.security.controller.dto.UserInfoDto;
import com.example.backendtracker.security.service.UserAccountService;
import com.example.backendtracker.util.AccountInformationRetriever;
import com.example.backendtracker.util.PersonAccountManager;
import com.example.backendtracker.util.UserInfo;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
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

    @PostMapping("recovery-password/{id}")
    public ResponseEntity<String> recoveryPassword(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(userAccountService.recoveryPassword(id));
    }

    @PostMapping("update-login")
    public ResponseEntity<UpdateLoginDto> updateLogin(@RequestBody LoginChangerDto loginChangerDto, @AuthenticationPrincipal UserDetails userDetails) {
        UserInfoDto userInfoDto = accountInformationRetriever.getAccountInfo(userDetails);
        String jwtToken = userAccountService.changeEmail(userDetails.getUsername(), loginChangerDto.newLogin(), userInfoDto.getRole());
        return ResponseEntity.status(HttpStatus.OK).body(UpdateLoginDto
                .builder()
                .jwt(jwtToken)
                .userInfoDto(userInfoDto)
                .build());
    }

    @PostMapping("update-user-info")
    public ResponseEntity<UserInfoDto> updateUserInfo(@RequestBody UserInfo userInfo, @AuthenticationPrincipal UserDetails userDetails) {
        personAccountManager.updateUserInfo(userDetails, userInfo);
        UserInfoDto userInfoDto = accountInformationRetriever.getAccountInfo(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(userInfoDto);

    }

    @DeleteMapping("delete/student/{id}")
    @Transactional
    public ResponseEntity<TypeResolutionContext.Empty> delete(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        userAccountService.deleteAccount(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
