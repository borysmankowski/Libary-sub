package com.example.borys_mankowski_test_10.book;

import com.example.borys_mankowski_test_10.book.model.Book;
import com.example.borys_mankowski_test_10.book.model.BookDto;
import com.example.borys_mankowski_test_10.book.model.BookMapper;
import com.example.borys_mankowski_test_10.book.model.CreateBookCommand;
import com.example.borys_mankowski_test_10.email.EmailService;
import com.example.borys_mankowski_test_10.exception.DatabaseException;
import com.example.borys_mankowski_test_10.subscription.SubscriptionRepository;
import com.example.borys_mankowski_test_10.subscription.model.Subscription;
import jdk.jfr.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookMapper bookMapper;

    @Captor
    private ArgumentCaptor<Book> bookCaptor;

    @Test
    void testCreateBook() {
        CreateBookCommand createBookCommand = new CreateBookCommand();
        createBookCommand.setTitle("Book One");
        createBookCommand.setAuthor("Author One");
        createBookCommand.setCategory("Category");

        Book book = new Book();
        book.setTitle("Book One");
        book.setAuthor("Author One");
        book.setCategory("Category");
        book.setAvailable(true);

        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setTitle("Book One");
        expectedBookDto.setAuthor("Author One");
        expectedBookDto.setCategory("Category");
        expectedBookDto.setAvailable(true);

        when(bookRepository.save(bookCaptor.capture())).thenReturn(book);

        BookDto createdBookDto = bookService.createBook(createBookCommand);

        Mockito.verify(bookRepository, Mockito.times(1)).save(bookCaptor.getValue());

        Book savedBook = bookCaptor.getValue();
        Assertions.assertEquals("Book One", savedBook.getTitle());
        Assertions.assertEquals("Author One", savedBook.getAuthor());

        Assertions.assertEquals(expectedBookDto.getTitle(), createdBookDto.getTitle());
        Assertions.assertEquals(expectedBookDto.getAuthor(), createdBookDto.getAuthor());
        Assertions.assertEquals(expectedBookDto.getCategory(), createdBookDto.getCategory());
        Assertions.assertEquals(expectedBookDto.isAvailable(), createdBookDto.isAvailable());
    }
}
