package com.maids.cc.Library_Management_System.service;

import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.BookNotFoundException;
import com.maids.cc.Library_Management_System.model.Book;
import com.maids.cc.Library_Management_System.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;
    BookService bookService;

    @Captor
    ArgumentCaptor<Book> argumentCaptor;

    Book book;

    private static final Long INVALID_ID = 99L;

    @BeforeEach
    public void setup() {
        bookService = new BookService(bookRepository);
        book = Book.builder()
                .id(1L)
                .title("title")
                .build();
    }


    @Test
    void shouldReturnAllBooks() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        Mockito.when(bookRepository.findAll()).thenReturn(books);
        List<Book> result = bookService.getAllBooks();
        assertThat(books).isEqualTo(result);
    }

    @Test
    void shouldReturnEmptyListWhenNoBooksSaved() {
        List<Book> books = new ArrayList<>();
        Mockito.when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        List<Book> result = bookService.getAllBooks();
        assertThat(books).isEqualTo(result);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindExistingBookById() {
        Mockito.when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.ofNullable(book));
        Book resultBook = bookService.findById(book.getId());
        assertThat(book).isEqualTo(resultBook);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        Mockito.when(bookRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> bookService.findById(INVALID_ID));
    }

    @Test
    void saveBook() {
        bookService.saveBook(book);
        Mockito.verify(bookRepository, Mockito.times(1))
                .save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue())
                .isEqualTo(book);
    }

    @Test
    void shouldUpdateBook() {
        String newTitle = "newTitle";
        Book updatedBook = Book.builder()
                .title(newTitle)
                .build();

        Mockito.when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.ofNullable(book));
        bookService.updateBook(updatedBook, book.getId());
        Mockito.verify(bookRepository, Mockito.times(1))
                .save(argumentCaptor.capture());
        //Asserting the [ID] of newBook is same as original book to insure update is performed
        assertThat(argumentCaptor.getValue().getId())
                .isEqualTo(book.getId());
        assertThat(argumentCaptor.getValue().getTitle())
                .isEqualTo(newTitle);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenBookNotFound() {
        Mockito.when(bookRepository.findById(ArgumentMatchers.anyLong()))
                .thenThrow(BookNotFoundException.class);
        assertThatExceptionOfType(BookNotFoundException.class)
                .isThrownBy(() -> bookService.updateBook(new Book(), INVALID_ID));
    }

    @Test
    void shouldDeleteBook() {
        Long id = book.getId();
        bookService.deleteById(id);
        Mockito.verify(bookRepository, Mockito.times(1))
                .deleteById(id);
        assertThat(book.getId()).isEqualTo(id);
    }

    @Test
    void shouldThrowExceptionWhenBookToDeleteIDISNull() {
        book.setId(null);
        Mockito.doThrow(IllegalArgumentException.class)
                .when(bookRepository)
                .deleteById(ArgumentMatchers.anyLong());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> bookService.deleteById(INVALID_ID));
        Mockito.verify(bookRepository, Mockito.times(1))
                .deleteById(INVALID_ID);
    }


}