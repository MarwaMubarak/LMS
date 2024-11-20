package com.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(
        name = "books",
        indexes = {
                @Index(columnList = "title", name = "title_idx"),
                @Index(columnList = "author", name = "author_idx")
        }
)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    @Column(unique = true)
    private String isbn;
    private int copiesAvailable = 0;

}
