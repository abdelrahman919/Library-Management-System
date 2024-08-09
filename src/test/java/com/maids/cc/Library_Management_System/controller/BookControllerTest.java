package com.maids.cc.Library_Management_System.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maids.cc.Library_Management_System.config.TestSecurityConfig;
import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.BookNotFoundException;
import com.maids.cc.Library_Management_System.model.Book;
import com.maids.cc.Library_Management_System.service.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(controllers = BookController.class)
public class BookControllerTest extends BaseControllerTest {

    @MockBean
    private BookService bookService;

    private static final String API_ENDPOINT = "/api/books";
    private static final String ID_PARAM = "/{id}";

    Book book;


    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        book = Book.builder()
                .id(1L)
                .title("title")
                .isbn("0123456789")
                .author("hmada")
                .genre("comedy")
                .publicationYear(LocalDate.of(2024, 1,20))
                .totalCopies(1)
                .build();

    }

    private String getBookString(Book book) throws JsonProcessingException {
        return objectMapper.writeValueAsString(book);
    }


    @Test
    void shouldListAllBooks() throws Exception {
        List<Book> books = Arrays.asList(book, new Book());
        Mockito.when(bookService.getAllBooks()).thenReturn(books);
        String booksAsString = objectMapper.writeValueAsString(books);
        mockMvc.perform(get(API_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().json(booksAsString));
    }

    @Test
    void shouldReturnEmptyListWhenNoBooksAreSaved() throws Exception {
        Mockito.when(bookService.getAllBooks()).thenReturn(new ArrayList<>());
        mockMvc.perform(get(API_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().json(new ArrayList<>().toString()));
    }

    @Test
    void shouldFindBookById() throws Exception {
        Mockito.when(bookService.findById(book.getId()))
                .thenReturn(book);
        String bookAsString = getBookString(book);
        mockMvc.perform(get(API_ENDPOINT + ID_PARAM, book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(bookAsString));
    }

    @Test
    void shouldReturnNotFoundWhenSearchByInvalidId() throws Exception {
        Mockito.when(bookService.findById(ArgumentMatchers.anyLong()))
                .thenThrow(BookNotFoundException.class);
        mockMvc.perform(get(API_ENDPOINT + ID_PARAM, INVALID_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveBook() throws Exception {
        String bookAsString = getBookString(book);
        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookAsString))
                .andExpect(status().isCreated())
                .andExpect(content().json(bookAsString));
        Mockito.verify(bookService, Mockito.times(1))
                .saveBook(ArgumentMatchers.any(Book.class));
    }

    @Test
    void updateBook() throws Exception {
        Book updatedBook = book;
        updatedBook.setTitle("hbd");

        String updatedBookString = getBookString(updatedBook);
        mockMvc.perform(put(API_ENDPOINT + ID_PARAM, book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookString))
                .andExpect(status().isOk())
                .andExpect(content().json(updatedBookString));
        Mockito.verify(bookService, Mockito.times(1))
                .updateBook(updatedBook, book.getId());
    }

    @Test
    void shouldDeleteBookById() throws Exception {
        mockMvc.perform(delete(API_ENDPOINT + ID_PARAM, book.getId()))
                .andExpect(status().isNoContent());
        Mockito.verify(bookService, Mockito.times(1))
                .deleteById(book.getId());
    }

    @Test
    void shouldReturnBadRequestOnValidationExceptions()throws Exception {
        String invalidBookString = objectMapper.writeValueAsString(new Book());
        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBookString))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put(API_ENDPOINT + ID_PARAM, book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBookString))
                .andExpect(status().isBadRequest());

    }


}