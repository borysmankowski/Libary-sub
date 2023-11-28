package com.example.borys_mankowski_test_10.book;

import com.example.borys_mankowski_test_10.book.model.Book;
import com.example.borys_mankowski_test_10.book.model.BookDto;
import com.example.borys_mankowski_test_10.book.model.BookMapper;
import com.example.borys_mankowski_test_10.book.model.CreateBookCommand;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BookService {

    private BookRepository bookRepository;

    private BookMapper bookMapper;


    @Transactional
    public BookDto createBook(CreateBookCommand createBookCommand) {
        Book newBook;
        newBook = bookMapper.fromDto(createBookCommand);
        newBook.setAvailable(true);
        newBook.setAddedDate(LocalDate.now());
        return bookMapper.mapToDto(bookRepository.save(newBook));
    }

    public List<BookDto> findBooksByAuthor(String author) {
        List<Book> books = bookRepository.findBookByAuthorAddedToday(author);
        return books.stream().map(bookMapper::mapToDto).collect(Collectors.toList());
    }

    public List<BookDto> findBooksByCategory(String category) {
        List<Book> books = bookRepository.findBookByCategoryAddedToday(category);
        return books.stream().map(bookMapper::mapToDto).collect(Collectors.toList());
    }


}


