package com.example.borys_mankowski_test_10.book.model;

import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookDto mapToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setCategory(book.getCategory());
        dto.setAvailable(book.isAvailable());
        return dto;
    }

    public Book fromDto(CreateBookCommand command) {
        Book book = new Book();
        book.setAuthor(command.getAuthor());
        book.setTitle(command.getTitle());
        book.setCategory(command.getCategory());
        return book;
    }
}
