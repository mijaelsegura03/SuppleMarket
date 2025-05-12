package com.mijaelsegura.eCommerceSpring.auth.controller;

import lombok.Data;

@Data
public class LoginRequest {
    private long dni;
    private String password;
}
