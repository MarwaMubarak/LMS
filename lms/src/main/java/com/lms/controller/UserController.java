package com.lms.controller;

import com.lms.dto.RegisterUserDTO;
import com.lms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody @Valid RegisterUserDTO registerUserDTO) throws Exception {
        return userService.createUser(registerUserDTO);
    }

}
