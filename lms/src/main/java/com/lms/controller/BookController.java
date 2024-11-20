package com.lms.controller;

import com.lms.dto.AddBookDTO;
import com.lms.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping()
    public ResponseEntity<Object> searchBookByTitleOrAuthor(@RequestParam(required = false, defaultValue = "") String title, @RequestParam(required = false, defaultValue = "") String author) throws Exception {
        return bookService.searchBookByTitleOrAuthor(title, author);
    }

    @PostMapping()
    public ResponseEntity<Object> addBook(@RequestBody AddBookDTO bookDTO) throws Exception {
        return bookService.addBook(bookDTO);
    }
}
