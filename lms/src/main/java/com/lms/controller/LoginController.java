package com.lms.controller;

import com.lms.dto.LoginRequestDTO;
import com.lms.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping()
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) throws Exception {
        return loginService.login(loginRequestDTO);
    }

}
