package com.lms.dto;

import com.lms.utility.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoggedInUserDTO {
    @NotBlank(message = "User Id is required!")
    private Long id;
    @NotBlank(message = "Name is required!")
    private String name;
    @NotBlank(message = "Email is required!")
    @Email(message = "Email is not Correct!")
    private String email;
    @NotBlank(message = "Title is required!")
    private UserRole role;
    @NotBlank(message = "Token is required!")
    private String token;
}
