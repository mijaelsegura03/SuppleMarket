package com.mijaelsegura.eCommerceSpring.controllers;

import com.mijaelsegura.eCommerceSpring.dto.user.ResultUser;
import com.mijaelsegura.eCommerceSpring.dto.user.ResultUserList;
import com.mijaelsegura.eCommerceSpring.dto.user.UserDto;
import com.mijaelsegura.eCommerceSpring.services.IUserService;
import com.mijaelsegura.eCommerceSpring.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{DNI}")
    public ResponseEntity<ResultUser> GetUserByDNI(@PathVariable long DNI) {
        ResultUser res = userService.GetUserByDNI(DNI);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResultUserList> GetAllUsers() {
        ResultUserList res = userService.GetAllUsers();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{DNI}")
    public ResponseEntity<ResultUser> PutUser(@PathVariable long DNI, @RequestBody UserDto userDto) {
        ResultUser res = userService.PutUser(DNI, userDto);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{DNI}")
    public ResponseEntity<ResultUser> DeleteUser(@PathVariable long DNI) {
        ResultUser res = userService.DeleteUser(DNI);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
