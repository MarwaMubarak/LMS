package com.lms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class BorrowingHistoryDTO {
    private Long id;
    @NotBlank(message = "User Id is required!")
    private Long userId;
    @NotBlank(message = "Book Id is required!")
    private Long bookId;
    @NotBlank(message = "Borrow Date is required!")
    private Date borrowDate;
    @NotBlank(message = "Return Date is required!")
    private Date returnDate;
}
