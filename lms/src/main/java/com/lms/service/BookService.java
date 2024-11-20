package com.lms.service;

import com.lms.dto.AddBookDTO;
import com.lms.dto.BookDTO;
import com.lms.dto.UserInfo;
import com.lms.model.Book;
import com.lms.repository.BookRepository;
import com.lms.utility.Response;

import com.lms.utility.UserRole;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<Object> searchBookByTitleOrAuthor(String title, String author) throws Exception {

        try {
            List<Book> books = bookRepository.findByTitleContainingAndAuthorContaining(title, author);

            if (books.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.unsuccessfulResponse("Not Found!", null));
            }

            List<BookDTO> bookDTOS = modelMapper.map(books, List.class);

            return ResponseEntity.status(HttpStatus.OK).body(Response.successfulResponse("Retrieved Successfully!", bookDTOS));
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    public ResponseEntity<Object> addBook(AddBookDTO bookDTO) throws Exception {
        try {
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!Objects.equals(userInfo.getRole(), UserRole.ADMIN.name())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.unsuccessfulResponse("Unauthorized! ADMIN Only..", null));
            }
            Optional<Book> existedBook = bookRepository.findByIsbn(bookDTO.getIsbn());
            if (existedBook.isPresent())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.unsuccessfulResponse("Book already exist!", null));

            Book book = modelMapper.map(bookDTO, Book.class);
            book = bookRepository.save(book);
            BookDTO addedBookDTO = modelMapper.map(book, BookDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(Response.successfulResponse("Added Successfully!", addedBookDTO));

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
