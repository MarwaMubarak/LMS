package com.lms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookDTO {
    private Long id;
    @NotBlank(message = "Title is required!")
    private String title;
    @NotBlank(message = "Author is required!")
    private String author;
    @NotBlank(message = "ISBN is required!")
    private String isbn;
    @NotBlank(message = "Copies Available is required!")
    private int copiesAvailable;
}
