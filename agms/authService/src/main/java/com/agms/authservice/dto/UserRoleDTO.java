package com.agms.authservice.dto;

import com.agms.authservice.util.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRoleDTO {
    private String email;
    private Role role;
}
