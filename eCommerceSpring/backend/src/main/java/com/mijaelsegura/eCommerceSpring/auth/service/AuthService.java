package com.mijaelsegura.eCommerceSpring.auth.service;

import com.mijaelsegura.eCommerceSpring.auth.controller.LoginRequest;
import com.mijaelsegura.eCommerceSpring.auth.controller.TokenResponse;
import com.mijaelsegura.eCommerceSpring.dto.user.UserDto;
import com.mijaelsegura.eCommerceSpring.enums.Role;
import com.mijaelsegura.eCommerceSpring.exceptions.UserExistingException;
import com.mijaelsegura.eCommerceSpring.exceptions.PropertyValidationException;
import com.mijaelsegura.eCommerceSpring.exceptions.ResourceNotFoundException;
import com.mijaelsegura.eCommerceSpring.models.User;
import com.mijaelsegura.eCommerceSpring.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager; //se delega toda la tarea de autenticacion al authenticationmanager

    public TokenResponse register(UserDto userDto) {
        String validation = userDto.validateAllProperties("POST");
        if (!Objects.equals(validation, "")) {
            throw new PropertyValidationException(validation);
        }
        Optional<User> dniValidation= userRepository.findById(userDto.getDNI());
        if (dniValidation.isPresent()) {
            throw new UserExistingException("A user with DNI: " + userDto.getDNI() + " already exists.");
        }
        User user = User.builder()
                .DNI(userDto.getDNI())
                .name(userDto.getName())
                .lastName(userDto.getLastName())
                .birthDate(userDto.getBirthDate())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new TokenResponse(token);
    }

    public TokenResponse registerAdministrator(UserDto userAdmin) {
        String validation = userAdmin.validateAllProperties("POST");
        if (!Objects.equals(validation, "")) {
            throw new PropertyValidationException(validation);
        }
        User user = User.builder()
                .DNI(userAdmin.getDNI())
                .name(userAdmin.getName())
                .lastName(userAdmin.getLastName())
                .birthDate(userAdmin.getBirthDate())
                .username(userAdmin.getUsername())
                .password(passwordEncoder.encode(userAdmin.getPassword()))
                .role(Role.ROLE_ADMIN)
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new TokenResponse(token);
    }

    public TokenResponse login(LoginRequest loginRequest) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getDni(), loginRequest.getPassword()));
        User user = userRepository.findById(loginRequest.getDni())
                .orElseThrow(() -> new ResourceNotFoundException("Not found any user with that DNI."));
        String token = jwtService.generateToken(user);
        return new TokenResponse(token);
    }

}
