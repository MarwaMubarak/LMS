package com.lms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class BorrowRequestDTO {
    @NotNull(message = "Book Id is required!")
    private Long bookId;
    @NotNull(message = "Borrow Date is required!")
    @DateTimeFormat()
    private Date borrowDate;
    @NotNull(message = "Return Date is required!")
    @DateTimeFormat()
    private Date returnDate;
}
