package com.example.borys_mankowski_test_10.book.model;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BookMapper {

    public BookDto mapToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setCategory(book.getCategory());
        dto.setAddedDate(book.getAddedDate());
        return dto;
    }

    public Book fromDto(CreateBookCommand command) {
        Book book = new Book();
        book.setAuthor(command.getAuthor());
        book.setTitle(command.getTitle());
        book.setCategory(command.getCategory());
        book.setAddedDate(LocalDate.now());
        return book;
    }
}
