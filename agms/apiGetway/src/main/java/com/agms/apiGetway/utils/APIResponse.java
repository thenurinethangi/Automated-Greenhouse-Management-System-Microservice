package com.agms.apiGetway.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class APIResponse {

    private int status;
    private String message;
    private Object data;
}
