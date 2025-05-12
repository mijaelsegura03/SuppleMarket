package com.mijaelsegura.eCommerceSpring.auth.controller;

import com.mijaelsegura.eCommerceSpring.auth.service.AuthService;
import com.mijaelsegura.eCommerceSpring.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(authService.register(userDto), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<TokenResponse> registerAdministrator(@RequestBody UserDto userAdmin) {
        return new ResponseEntity<>(authService.registerAdministrator(userAdmin), HttpStatus.OK);
    }

    //LAS EXCEPCIONES LAS SIGUE MANEJANDO MI CONTROLLERADVICE
}
