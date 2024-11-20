package com.lms.controller;

import com.lms.dto.BorrowRequestDTO;
import com.lms.service.BorrowingHistoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrow")
public class BorrowingHistoryController {

    @Autowired
    private BorrowingHistoryService borrowingHistoryService;

    @GetMapping("/history/{userId}")
    public ResponseEntity<Object> findHistoryByUserId(@PathVariable Long userId) throws Exception {
        return borrowingHistoryService.findHistoryByUserId(userId);
    }

    @PostMapping()
    public ResponseEntity<Object> addBorrowingHistory(@RequestBody @Valid BorrowRequestDTO borrowRequestDTO) throws Exception {

        return borrowingHistoryService.addBorrowingHistory(borrowRequestDTO);
    }


}
