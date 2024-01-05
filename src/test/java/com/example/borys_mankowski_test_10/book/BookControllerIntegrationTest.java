package com.example.borys_mankowski_test_10.book;

import com.example.borys_mankowski_test_10.book.model.Book;
import com.example.borys_mankowski_test_10.book.model.CreateBookCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableWebMvc
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerIntegrationTest {
    private static final String AUTHOR = "Andrzej";
    private static final String TITLE = "Nowakowie";
    private static final String CATEGORY = "Horror";
    private static final LocalDate ADDED_DATE = LocalDate.now();

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final Book book1 = new Book(1L, AUTHOR, TITLE, CATEGORY, ADDED_DATE, 1);


    @AfterEach
    void teardown() {
        bookRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateBook() throws Exception {

        CreateBookCommand command = CreateBookCommand.builder()
                .author(AUTHOR)
                .title(TITLE)
                .category(CATEGORY)
                .build();

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin").roles("ADMIN"))
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author").value(AUTHOR));

        List<Book> books = bookRepository.findAll();
        assertEquals(1, books.size());
        Book newBook = books.stream()
                .filter(book -> book.getId().equals(book1.getId()))
                .findFirst()
                .orElseThrow();
        assertEquals(AUTHOR, newBook.getAuthor());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBookFailureBlankTitle() throws Exception {
        String exceptionMsg = "Title cannot be blank";

        CreateBookCommand bookToSave = CreateBookCommand.builder()
                .title(" ")
                .author("Sample Author")
                .category("Sample Category")
                .build();

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin").roles("ADMIN"))
                        .content(objectMapper.writeValueAsString(bookToSave)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("validation errors"))
                .andExpect(jsonPath("$.violations[0].field").value("title"))
                .andExpect(jsonPath("$.violations[0].message").value(exceptionMsg));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBookFailureBlankAuthor() throws Exception {

        String exceptionMsg = "Author cannot be blank";

        CreateBookCommand bookToSave = CreateBookCommand.builder()
                .title("Sample Title")
                .author(" ")
                .category("Sample Category")
                .build();

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin").roles("ADMIN"))
                        .content(objectMapper.writeValueAsString(bookToSave)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("validation errors"))
                .andExpect(jsonPath("$.violations[0].field").value("author"))
                .andExpect(jsonPath("$.violations[0].message").value(exceptionMsg));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBookFailureBlankCategory() throws Exception {

        String exceptionMsg = "Category cannot be blank";

        CreateBookCommand bookToSave = CreateBookCommand.builder()
                .title("Sample Title")
                .author("Sample Author")
                .category(" ")
                .build();

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin").roles("ADMIN"))
                        .content(objectMapper.writeValueAsString(bookToSave)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("validation errors"))
                .andExpect(jsonPath("$.violations[0].field").value("category"))
                .andExpect(jsonPath("$.violations[0].message").value(exceptionMsg));
    }

    }
