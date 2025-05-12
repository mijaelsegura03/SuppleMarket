package com.mijaelsegura.eCommerceSpring.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long DNI;
    private String name;
    private String lastName;
    private LocalDate birthDate;
    private String username;
    private String password;

    public String validateAllProperties(String method) {
        if (DNI == 0L && Objects.equals(method, "POST")) {
            return "User DNI is required.";
        } else if (name == null || name.isBlank()) {
            return "User first name is required.";
        } else if (lastName == null ||lastName.isBlank()) {
            return "User last name is required.";
        } else if (birthDate == null) {
            return "User birth date is required.";
        } else if (username == null || username.isBlank()) {
            return "Username is required.";
        } else if (Objects.equals(method, "POST") && (password == null || password.isBlank())) {
            return "User password is required.";
        }
        return "";
    }
}
