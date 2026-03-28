package com.agms.authservice.service;

import com.agms.authservice.dto.UserDTO;
import com.agms.authservice.dto.UserRoleDTO;
import com.agms.authservice.util.APIResponse;

public interface AuthService {

    APIResponse register(UserDTO user);

    APIResponse login(UserDTO user);

    APIResponse generateNewAccessToken(UserRoleDTO user);
}
