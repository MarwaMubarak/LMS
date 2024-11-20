package com.lms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StaffDTO extends UserDTO {
    @NotBlank(message = "Position is required!")
    private String position;
    @NotBlank(message = "Salary is required!")
    private Double salary;
}
