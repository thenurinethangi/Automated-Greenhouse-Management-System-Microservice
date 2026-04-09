package com.agms.authservice.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agms.authservice.dto.UserRegisterDTO;
import com.agms.authservice.dto.RefreshToken;
import com.agms.authservice.dto.UserLoginDTO;
import com.agms.authservice.service.AuthService;
import com.agms.authservice.util.APIResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public APIResponse register(@Valid @RequestBody UserRegisterDTO user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public APIResponse login(@Valid @RequestBody UserLoginDTO user) {
        return authService.login(user);
    }

    @PostMapping("/access-token")
    public APIResponse generateNewAccessToken(@Valid @RequestBody RefreshToken token) {
        return authService.generateNewAccessToken(token);
    }

}
