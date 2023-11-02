package com.topsquad.authservice.model;

import lombok.Data;

@Data
public class ChangePassRequest {
    private String token;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
