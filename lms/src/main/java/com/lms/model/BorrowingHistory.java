package com.lms.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(
        name = "borrowing_histories",
        indexes = {
                @Index(columnList = "user_id", name = "idx_user_id"),
                @Index(columnList = "book_id", name = "idx_book_id")
        }
)
public class BorrowingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private Date borrowDate;
    private Date returnDate;
}
