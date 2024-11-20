package com.lms.mapper;

import com.lms.dto.BorrowingHistoryResponseDTO;
import com.lms.model.BorrowingHistory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BorrowingHistoryResponseMapper {

    public static BorrowingHistoryResponseDTO toBorrowingHistoryResponseDTO(BorrowingHistory history) {
        BorrowingHistoryResponseDTO responseDTO = new BorrowingHistoryResponseDTO();
        responseDTO.setId(history.getId());
        responseDTO.setBookId(history.getBook().getId());
        responseDTO.setBookIsbn(history.getBook().getIsbn());
        responseDTO.setBookTitle(history.getBook().getTitle());
        responseDTO.setBorrowDate(history.getBorrowDate());
        responseDTO.setReturnDate(history.getReturnDate());
        return responseDTO;
    }


    public List<BorrowingHistoryResponseDTO> toBorrowingHistoryResponseDTOList(List<BorrowingHistory> histories) {
        return histories.stream()
                .map(BorrowingHistoryResponseMapper::toBorrowingHistoryResponseDTO)
                .collect(Collectors.toList());
    }
}
