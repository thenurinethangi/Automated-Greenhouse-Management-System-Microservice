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
public class UserDTO {

    private int id;
    private String fullName;
    private String email;
    private String password;
    private Role role;
}
