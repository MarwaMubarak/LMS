package com.lms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "staffs")
public class Staff extends User {

    private String position;
    private Double salary;
}
