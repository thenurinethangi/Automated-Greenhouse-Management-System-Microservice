package com.agms.apiGetway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.agms.apiGetway.utils.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(401);
        response.setContentType("application/json");

        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatus(401);
        apiResponse.setMessage("Unauthorized" );
        apiResponse.setData(authException.getMessage());

        new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
    }
}
