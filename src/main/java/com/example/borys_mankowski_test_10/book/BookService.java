package com.example.borys_mankowski_test_10.book;

import com.example.borys_mankowski_test_10.book.model.Book;
import com.example.borys_mankowski_test_10.book.model.BookDto;
import com.example.borys_mankowski_test_10.book.model.BookMapper;
import com.example.borys_mankowski_test_10.book.model.CreateBookCommand;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BookService {

    private BookRepository bookRepository;
    private BookMapper bookMapper;

    @Transactional
    public BookDto createBook(CreateBookCommand createBookCommand) {
        Book newBook = bookMapper.fromDto(createBookCommand);
        return bookMapper.mapToDto(bookRepository.save(newBook));
    }
}


