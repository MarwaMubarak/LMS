package com.lms.service;

import com.lms.dto.LoggedInUserDTO;
import com.lms.dto.LoginRequestDTO;
import com.lms.model.User;
import com.lms.security.SecretKeyReader;
import com.lms.utility.Response;
import com.lms.utility.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class LoginService {


    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private SecretKeyReader secretKeyReader;

    @Autowired
    private ModelMapper modelMapper;

    private String createLoginToken(String email, Long userId, UserRole userRole) {
        LocalDateTime expiryDateTime = LocalDateTime.now().plusMonths(1);
        Date expiryDate = Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .issuer("LMS")
                .subject("Token")
                .claim("email", email)
                .claim("userId", userId)
                .claim("role", userRole)
                .signWith(Keys.hmacShaKeyFor(secretKeyReader.tokenSecretKey.getBytes(StandardCharsets.UTF_8)))
                .issuedAt(new Date())
                .expiration(expiryDate)
                .compact();
    }

    public ResponseEntity<Object> login(LoginRequestDTO loginRequestDTO) throws Exception {
        try {
            Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

            User userDetails = (User) authentication.getPrincipal();

            String token = createLoginToken(userDetails.getEmail(), userDetails.getId(), userDetails.getRole());

            LoggedInUserDTO loggedInUserDTO = modelMapper.map(userDetails, LoggedInUserDTO.class);
            loggedInUserDTO.setToken(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok(Response.successfulResponse("Login successful", loggedInUserDTO));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.unsuccessfulResponse("Invalid credentials", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.unsuccessfulResponse("An unexpected error occurred", null));
        }
    }

}
