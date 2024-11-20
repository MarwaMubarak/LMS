package com.lms.mapper;

import com.lms.dto.BorrowingHistoryDTO;
import com.lms.model.BorrowingHistory;
import org.springframework.stereotype.Component;

@Component
public class BorrowingHistoryMapper {

    public static BorrowingHistoryDTO toBorrowingHistoryDTO(BorrowingHistory history) {
        BorrowingHistoryDTO dto = new BorrowingHistoryDTO();
        dto.setId(history.getId());
        dto.setUserId(history.getUser().getId());
        dto.setBookId(history.getBook().getId());
        dto.setBorrowDate(history.getBorrowDate());
        dto.setReturnDate(history.getReturnDate());
        return dto;
    }
}

