package com.maids.cc.Library_Management_System.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.BorrowValidationException;
import com.maids.cc.Library_Management_System.model.Book;
import com.maids.cc.Library_Management_System.model.BorrowingRecord;
import com.maids.cc.Library_Management_System.model.Patron;
import com.maids.cc.Library_Management_System.service.BorrowingRecordService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BorrowingRecordController.class)
class BorrowingRecordControllerTest extends BaseControllerTest {

    @MockBean
    BorrowingRecordService borrowingRecordService;

    private final static String API_ENDPOINT = "/api/borrow/{bookId}/patron/{patronId}";


    Patron patron;
    Book book;
    BorrowingRecord borrowingRecord;
    BorrowingRecord borrowingRecordWithBorrowDate;


    @BeforeEach
    void setup() {
        patron = Patron.builder()
                .id(1L)
                .build();
        book = Book.builder()
                .id(1L)
                .build();

        borrowingRecord = BorrowingRecord.builder()
                .id(1L)
                .book(book)
                .patron(patron)
                .build();
        borrowingRecordWithBorrowDate = BorrowingRecord.builder()
                .id(1L)
                .book(book)
                .patron(patron)
                .borrowDate(LocalDate.now())
                .build();
    }


    @Test
    void borrowBookSuccessfully() throws Exception {
        Mockito.when(borrowingRecordService.borrowBook(book.getId(), patron.getId()))
                .thenReturn(borrowingRecord);
        mockMvc.perform(post(API_ENDPOINT, book.getId(), patron.getId()))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(borrowingRecord)));
    }

    @Test
    void shouldReturnBadRequestWhenBorrowingIsInvalid() throws Exception {
        Mockito.when(borrowingRecordService
                        .borrowBook(book.getId(), patron.getId()))
                .thenThrow(BorrowValidationException.class);
        mockMvc.perform(post(API_ENDPOINT, book.getId(), patron.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundWhenBookOrPatronNotFound() throws Exception {
        Mockito.when(borrowingRecordService
                        .borrowBook(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenThrow(EntityNotFoundException.class);
        mockMvc.perform(post(API_ENDPOINT, INVALID_ID, INVALID_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnBookSuccessfully() throws Exception {
        Mockito.when(borrowingRecordService.returnBook(book.getId(), patron.getId()))
                .thenReturn(borrowingRecord);
        mockMvc.perform(put(API_ENDPOINT, book.getId(), patron.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(borrowingRecord)));
    }

    @Test
    void shouldThrowExceptionWhenReturnIsNotValid() throws Exception {
        Mockito.when(borrowingRecordService.returnBook(book.getId(), patron.getId()))
                .thenThrow(BorrowValidationException.class);
        mockMvc.perform(put(API_ENDPOINT, book.getId(), patron.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenBookOrPatronNotFound() throws Exception {
        Mockito.when(borrowingRecordService
                        .returnBook(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenThrow(EntityNotFoundException.class);
        mockMvc.perform(put(API_ENDPOINT, INVALID_ID, INVALID_ID))
                .andExpect(status().isNotFound());
    }






}