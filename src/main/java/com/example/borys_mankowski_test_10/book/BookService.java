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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ConcurrentModificationException;

@AllArgsConstructor
@Service
public class BookService {

    private BookRepository bookRepository;

    private BookMapper bookMapper;

    private ApplicationEventPublisher notificationPublisher;



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


        if (bookRepository.existsByTitle(createBookCommand.getTitle())) {
            throw new DuplicateResourceException("Book with title '" + createBookCommand.getTitle() + "' already exists");
        }

        Book newBook;
        newBook = bookMapper.fromDto(createBookCommand);
        newBook.setAvailable(true);
        newBook.setAddedDate(LocalDate.now());


        try {
            bookRepository.save(newBook);


        } catch (DuplicateResourceException exception) {
            throw new DuplicateResourceException("Duplicate book for title " + createBookCommand.getTitle());

        } catch (OptimisticLockException ole) {
            throw new ConcurrentModificationException("The book was modified by another transaction. Please try again.");
        }
        notificationPublisher.publishEvent(newBook);
        return bookMapper.mapToDto(newBook);
    }

}


