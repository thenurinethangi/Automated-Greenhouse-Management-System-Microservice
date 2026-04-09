package com.agms.authservice.service.impl;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.agms.authservice.dto.UserRegisterDTO;
import com.agms.authservice.dto.RefreshToken;
import com.agms.authservice.dto.UserLoginDTO;
import com.agms.authservice.entity.User;
import com.agms.authservice.exception.EmailAlreadyExistsException;
import com.agms.authservice.exception.EmailNotExistsException;
import com.agms.authservice.exception.PasswordIncorrectException;
import com.agms.authservice.exception.UnauthenticateException;
import com.agms.authservice.service.AuthService;
import com.agms.authservice.util.APIResponse;
import com.agms.authservice.util.JwtUtil;
import com.agms.authservice.util.Role;

import lombok.RequiredArgsConstructor;

import com.agms.authservice.repository.AuthRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public APIResponse register(UserRegisterDTO userDto) {

        if (authRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User(
                userDto.getFullName(),
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword()),
                Role.FARMER);
        User savedUser = authRepository.save(user);

        return new APIResponse(201, "Registration successful", savedUser.getId());
    }

    @Override
    public APIResponse login(UserLoginDTO userDto) {

        if (!authRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailNotExistsException("Email not exists, please register first");
        }

        User user = authRepository.findByEmail(userDto.getEmail());

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new PasswordIncorrectException("Invalid credentials");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getRole());

        LinkedHashMap<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("accessToken", accessToken);
        responseData.put("refreshToken", refreshToken);

        return new APIResponse(201, "Login successful", responseData);
    }

    @Override
    public APIResponse generateNewAccessToken(RefreshToken token) {

        if (!jwtUtil.validateRefreshToken(token.getRefreshToken())) {
            throw new UnauthenticateException("Invalid refresh token, please login again!");
        }

        String email = jwtUtil.extractEmailFromRefreshToken(token.getRefreshToken());

        User user = authRepository.findByEmail(email);
        if (user == null) {
            throw new EmailNotExistsException("User not exists, please register first!");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole());
        LinkedHashMap<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("accessToken", newAccessToken);

        return new APIResponse(200, "New access token generated", responseData);
    }

}
