package com.example.borys_mankowski_test_10.appuser.model;

import com.example.borys_mankowski_test_10.book.model.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppUserEmailBooks {
    private String userEmail;
    private List<Book> matchedBooks;

}
