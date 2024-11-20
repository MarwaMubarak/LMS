package com.lms.service;

import com.lms.dto.BorrowRequestDTO;
import com.lms.dto.BorrowingHistoryDTO;
import com.lms.dto.BorrowingHistoryResponseDTO;
import com.lms.dto.UserInfo;
import com.lms.mapper.BorrowingHistoryMapper;
import com.lms.mapper.BorrowingHistoryResponseMapper;
import com.lms.model.Book;
import com.lms.model.BorrowingHistory;
import com.lms.model.User;
import com.lms.repository.BookRepository;
import com.lms.repository.BorrowingHistoryRepository;
import com.lms.repository.UserRepository;
import com.lms.utility.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BorrowingHistoryService {
    @Autowired
    private BorrowingHistoryRepository borrowingHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BorrowingHistoryResponseMapper borrowingHistoryResponseMapper;


    public ResponseEntity<Object> findHistoryByUserId(Long userId) throws Exception {

        try {
            List<BorrowingHistory> borrowingHistory = borrowingHistoryRepository.findByUserId(userId);
            if (borrowingHistory.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.unsuccessfulResponse("Not Found!", null));
            }
            List<BorrowingHistoryResponseDTO> borrowingHistoryResponseDTOS = borrowingHistoryResponseMapper.toBorrowingHistoryResponseDTOList(borrowingHistory);
            return ResponseEntity.status(HttpStatus.OK).body(Response.successfulResponse("Retrieved Successfully!", borrowingHistoryResponseDTOS));
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    public ResponseEntity<Object> addBorrowingHistory(BorrowRequestDTO borrowRequestDTO) throws Exception {
        try {

            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Optional<Book> book = bookRepository.findById(borrowRequestDTO.getBookId());
            Optional<User> user = userRepository.findById(userInfo.getUserId());
            if (book.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.unsuccessfulResponse("Book Not Found!", null));
            }

            if (book.get().getCopiesAvailable() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.unsuccessfulResponse("Copies Not Available!", null));
            }

            book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
            bookRepository.save(book.get());

            BorrowingHistory borrowingHistory = modelMapper.map(borrowRequestDTO, BorrowingHistory.class);
            borrowingHistory.setBook(book.get());
            borrowingHistory.setUser(user.get());
            borrowingHistory = borrowingHistoryRepository.save(borrowingHistory);
            BorrowingHistoryDTO borrowingHistoryDTO = BorrowingHistoryMapper.toBorrowingHistoryDTO(borrowingHistory);


            return ResponseEntity.status(HttpStatus.OK).body(Response.successfulResponse("Created Successfully!", borrowingHistoryDTO));

        } catch (Exception e) {
            throw new Exception(e);
        }
    }


}
