package com.maids.cc.Library_Management_System.repository;

import com.maids.cc.Library_Management_System.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    Book book = Book.builder()
            .title("title")
            .isbn("123")
            .build();

    @Test
    void shouldSaveBook() {
        Book savedBook = bookRepository.save(book);
        assertThat(savedBook).usingRecursiveAssertion()
                .ignoringFields("id")
                .isEqualTo(book);
    }

//    @Test
//    void findByTitle() {
//        bookRepository.save(book);
//        Optional<Book> resultOptional = bookRepository.findByTitle(book.getTitle());
//        assertThat(resultOptional).isPresent();
//        Book resultBook = resultOptional.get();
//        assertThat(resultBook).isEqualTo(book);
//    }
//
//    @Test
//    void shouldThrowExceptionIfBookNotFoundByTitle() {
//        Optional<Book> emptyOptional = bookRepository.findByTitle("NONE");
//        assertThat(emptyOptional).isEmpty();
//    }
//
//    @Test
//    void findByIsbn() {
//        bookRepository.save(book);
//        Optional<Book> resultOptional = bookRepository.findByIsbn(book.getIsbn());
//        assertThat(resultOptional).isPresent();
//        Book resultBook = resultOptional.get();
//        assertThat(resultBook).isEqualTo(book);
//    }
//
//    @Test
//    void shouldThrowExceptionIfBookNotFoundByIsbn() {
//        Optional<Book> emptyOptional = bookRepository.findByTitle("NONE");
//        assertThat(emptyOptional).isEmpty();
//    }


}