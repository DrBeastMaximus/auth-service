package com.topsquad.authservice.model;

import lombok.Data;

@Data
public class AuthResponse {
    private String username;
    private String token;
}
