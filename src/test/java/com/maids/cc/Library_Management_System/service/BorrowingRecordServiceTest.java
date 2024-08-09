package com.maids.cc.Library_Management_System.service;

import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.BorrowValidationException;
import com.maids.cc.Library_Management_System.model.Book;
import com.maids.cc.Library_Management_System.model.BorrowingRecord;
import com.maids.cc.Library_Management_System.model.Patron;
import com.maids.cc.Library_Management_System.repository.BookRepository;
import com.maids.cc.Library_Management_System.repository.BorrowingRecordRepository;
import com.maids.cc.Library_Management_System.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BorrowingRecordServiceTest {

    @Mock
    BorrowingRecordRepository borrowingRecordRepository;
    @Mock
    PatronRepository patronRepository;
    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BorrowingRecordService borrowingRecordService;

    Book bookCanBorrow;
    Book bookInvalidForBorrow;
    Patron patron;
    BorrowingRecord validRecordWithBookBorrowed;

    @Captor
    ArgumentCaptor<BorrowingRecord> argumentCaptor;
    @Captor
    ArgumentCaptor<Book> argumentBookCaptor;



    @BeforeEach
    void setup() {

        bookCanBorrow = Book.builder()
                .id(1L)
                .totalCopies(5)
                .borrowedCopies(1)
                .build();
        bookInvalidForBorrow = Book.builder()
                .id(2L)
                .totalCopies(1)
                .borrowedCopies(1)
                .build();
        patron = Patron.builder()
                .id(1L)
                .build();
        validRecordWithBookBorrowed = BorrowingRecord.builder()
//                .id(1L)
                .borrowDate(LocalDate.now())
                .book(bookCanBorrow)
                .patron(patron)
                .build();
    }


    @Test
    void shouldSaveBorrowingRecord() {
        int originalAvailableNumOfCopies = bookCanBorrow.getAvailableCopies();
        Mockito.when(patronRepository.findById(patron.getId()))
                .thenReturn(Optional.ofNullable(patron));
        Mockito.when(bookRepository.findById(bookCanBorrow.getId()))
                .thenReturn(Optional.ofNullable(bookCanBorrow));
        Mockito.when(borrowingRecordRepository.findAllByBookAndPatron(bookCanBorrow, patron))
                .thenReturn(new ArrayList<>());
        BorrowingRecord borrowingRecord = borrowingRecordService.borrowBook(bookCanBorrow.getId(), patron.getId());

        assertThat(borrowingRecord).isEqualTo(validRecordWithBookBorrowed);
        Mockito.verify(borrowingRecordRepository, Mockito.times(1))
                .save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(borrowingRecord);
        Mockito.verify(bookRepository, Mockito.times(1))
                .save(argumentBookCaptor.capture());
        assertThat(argumentBookCaptor.getValue().getAvailableCopies())
                .isEqualTo(originalAvailableNumOfCopies - 1);
    }

    @Test
    void shouldThrowBorrowExceptionWhenAvailableCopiesLessThanZero() {
        Mockito.when(bookRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.ofNullable(bookInvalidForBorrow));
        Mockito.when(patronRepository.findById(patron.getId()))
                .thenReturn(Optional.ofNullable(patron));

        assertThatExceptionOfType(BorrowValidationException.class)
                .isThrownBy(() -> borrowingRecordService.
                        borrowBook(bookInvalidForBorrow.getId(), patron.getId()));
    }

    @Test
    void shouldThrowBorrowExceptionWhenBookAlreadyBorrowed() {
        Mockito.when(bookRepository.findById(bookCanBorrow.getId()))
                .thenReturn(Optional.ofNullable(bookCanBorrow));
        Mockito.when(patronRepository.findById(patron.getId()))
                .thenReturn(Optional.ofNullable(patron));
        Mockito.when(borrowingRecordRepository.findAllByBookAndPatron(bookCanBorrow, patron))
                .thenReturn(List.of(validRecordWithBookBorrowed));
        assertThatExceptionOfType(BorrowValidationException.class)
                .isThrownBy(() ->  borrowingRecordService
                        .borrowBook(bookCanBorrow.getId(), patron.getId()));
    }



    @Test
    void returnBookSuccessfully() {
        int originalAvailableCopies = bookCanBorrow.getAvailableCopies();
        Mockito.when(bookRepository.findById(bookCanBorrow.getId()))
                .thenReturn(Optional.ofNullable(bookCanBorrow));
        Mockito.when(patronRepository.findById(patron.getId()))
                .thenReturn(Optional.ofNullable(patron));
        Mockito.when(borrowingRecordRepository
                        .findAllByBookAndPatron(bookCanBorrow, patron))
                .thenReturn(List.of(validRecordWithBookBorrowed));

        BorrowingRecord borrowingRecord = borrowingRecordService.returnBook(bookCanBorrow.getId(), patron.getId());

        Mockito.verify(borrowingRecordRepository, Mockito.times(1))
                .save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getReturnDate()).isEqualTo(LocalDate.now());
        Mockito.verify(bookRepository, Mockito.times(1))
                .save(argumentBookCaptor.capture());
        assertThat(argumentBookCaptor.getValue().getAvailableCopies())
                .isEqualTo(originalAvailableCopies + 1);
    }

    @Test
    void returnBookShouldThrowExceptionWhenBookNotBorrowedBefore() {
        Mockito.when(bookRepository.findById(bookCanBorrow.getId()))
                .thenReturn(Optional.ofNullable(bookCanBorrow));
        Mockito.when(patronRepository.findById(patron.getId()))
                .thenReturn(Optional.ofNullable(patron));
        Mockito.when(borrowingRecordRepository
                        .findAllByBookAndPatron(bookCanBorrow, patron))
                .thenReturn(new ArrayList<>());
        assertThatExceptionOfType(BorrowValidationException.class)
                .isThrownBy(() -> borrowingRecordService
                        .returnBook(bookCanBorrow.getId(), patron.getId()));
    }

    @Test
    void returnBookShouldThrowExceptionWhenBookAlreadyReturned() {
        Mockito.when(bookRepository.findById(bookCanBorrow.getId()))
                .thenReturn(Optional.ofNullable(bookCanBorrow));
        Mockito.when(patronRepository.findById(patron.getId()))
                .thenReturn(Optional.ofNullable(patron));
        BorrowingRecord invalidRecord =  validRecordWithBookBorrowed;
        invalidRecord.setReturnDate(LocalDate.now());
        Mockito.when(borrowingRecordRepository
                        .findAllByBookAndPatron(bookCanBorrow, patron))
                .thenReturn(List.of(invalidRecord));

        assertThatExceptionOfType(BorrowValidationException.class)
                .isThrownBy(() -> borrowingRecordService
                        .returnBook(bookCanBorrow.getId(), patron.getId()));
    }


}