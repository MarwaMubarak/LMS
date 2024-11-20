package com.lms.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BorrowingHistoryResponseDTO {

    private Long id;
    private String bookTitle;
    private String bookIsbn;
    private Long bookId;
    private Date borrowDate;
    private Date returnDate;
}
