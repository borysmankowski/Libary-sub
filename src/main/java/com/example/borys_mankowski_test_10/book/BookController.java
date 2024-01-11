package com.example.borys_mankowski_test_10.book;

import com.example.borys_mankowski_test_10.book.model.BookDto;
import com.example.borys_mankowski_test_10.book.model.CreateBookCommand;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BookDto> createBook(@RequestBody @Valid CreateBookCommand createBookCommand) {
        BookDto createdBook = bookService.createBook(createBookCommand);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

}
