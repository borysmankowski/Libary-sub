package com.example.borys_mankowski_test_10.book;

import com.example.borys_mankowski_test_10.book.model.Book;
import com.example.borys_mankowski_test_10.book.model.BookDto;
import com.example.borys_mankowski_test_10.book.model.BookMapper;
import com.example.borys_mankowski_test_10.book.model.CreateBookCommand;
import com.example.borys_mankowski_test_10.exception.DuplicateResourceException;
import com.example.borys_mankowski_test_10.exception.ResourceNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BookService {

    private BookRepository bookRepository;

    private BookMapper bookMapper;





    @Transactional
    public BookDto createBook(CreateBookCommand createBookCommand) {

        if (createBookCommand.getTitle() == null || createBookCommand.getTitle().trim().isEmpty()) {
            throw new ResourceNotFoundException("Title cannot be empty");
        }

        if (createBookCommand.getAuthor() == null || createBookCommand.getAuthor().trim().isEmpty()) {
            throw new ResourceNotFoundException("Author cannot be empty");
        }

        if (createBookCommand.getCategory() == null || createBookCommand.getCategory().trim().isEmpty()) {
            throw new ResourceNotFoundException("Category cannot be empty");
        }


        Book newBook;
        newBook = bookMapper.fromDto(createBookCommand);
        newBook.setAvailable(true);
        newBook.setAddedDate(LocalDate.now());


//        try {
           bookRepository.save(newBook);

//        } catch (DuplicateResourceException exception) {
//            throw new DuplicateResourceException("Duplicate book for title " + createBookCommand.getTitle());
//
//        } catch (OptimisticLockException ole) {
//            throw new ConcurrentModificationException("The book was modified by another transaction. Please try again.");
//        }

        return bookMapper.mapToDto(newBook);
    }

    public List<BookDto> findBooksByAuthor(String author){
        List<Book> books = bookRepository.findBookByAuthorAddedToday(author);
        return books.stream().map(bookMapper::mapToDto).collect(Collectors.toList());
    }
    public List<BookDto> findBooksByCategory(String category){
        List<Book> books = bookRepository.findBookByCategoryAddedToday(category);
        return books.stream().map(bookMapper::mapToDto).collect(Collectors.toList());
    }


}


