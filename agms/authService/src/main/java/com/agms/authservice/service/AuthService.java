package com.agms.authservice.service;

import com.agms.authservice.dto.UserRegisterDTO;
import com.agms.authservice.dto.RefreshToken;
import com.agms.authservice.dto.UserLoginDTO;
import com.agms.authservice.util.APIResponse;

public interface AuthService {

    APIResponse register(UserRegisterDTO user);

    APIResponse login(UserLoginDTO user);

    APIResponse generateNewAccessToken(RefreshToken token);
}
