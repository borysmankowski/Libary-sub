package com.example.borys_mankowski_test_10.book;

import com.example.borys_mankowski_test_10.book.model.Book;
import com.example.borys_mankowski_test_10.book.model.BookDto;
import com.example.borys_mankowski_test_10.book.model.BookMapper;
import com.example.borys_mankowski_test_10.book.model.CreateBookCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;
    @Mock
    private BookMapper bookMapper;



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

        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setTitle("Book One");
        expectedBookDto.setAuthor("Author One");
        expectedBookDto.setCategory("Category");

        when(bookMapper.fromDto(createBookCommand)).thenReturn(book);

        when(bookRepository.save(any())).thenReturn(book);
        when(bookMapper.mapToDto(book)).thenReturn(expectedBookDto);

        BookDto result = bookService.createBook(createBookCommand);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        Book savedBook = bookCaptor.getValue();

        Assertions.assertEquals("Book One", savedBook.getTitle());
        Assertions.assertEquals("Author One", savedBook.getAuthor());

        Assertions.assertEquals(expectedBookDto.getTitle(), result.getTitle());
        Assertions.assertEquals(expectedBookDto.getAuthor(), result.getAuthor());
        Assertions.assertEquals(expectedBookDto.getCategory(), result.getCategory());
    }

}
