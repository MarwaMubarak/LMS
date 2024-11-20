package com.lms.dto;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddBookDTO {
    @NotBlank(message = "Title is required!")
    private String title;
    @NotBlank(message = "Author is required!")
    private String author;
    @NotBlank(message = "ISBN is required!")
    private String isbn;
    @NotBlank(message = "Copies Available is required!")
    private int copiesAvailable;
}
