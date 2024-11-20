package com.lms.dto;

import com.lms.utility.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    @NotBlank(message = "Name is required!")
    private String name;
    @NotBlank(message = "Email is required!")
    private String email;
    @NotBlank(message = "Role is required!")
    private UserRole role;
    @NotBlank(message = "Password is required!")
    private String password;
}
