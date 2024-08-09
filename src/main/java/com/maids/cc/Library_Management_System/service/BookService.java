package com.maids.cc.Library_Management_System.service;

import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.BookNotFoundException;
import com.maids.cc.Library_Management_System.model.Book;
import com.maids.cc.Library_Management_System.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Cacheable(value = "books", key = "#root.methodName")
    public List<Book> getAllBooks() {
        System.out.println("Fetched form DB");
        return bookRepository.findAll();
    }

    @Cacheable(value = "books", key = "#id")
    public Book findById(Long id) {
        System.out.println("Fetched form DB ID");
        return bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
    }

//        @CachePut(value = "books", key = "#book.id")
    @CacheEvict(value = "books", key = "#root.methodName",allEntries = true)
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    //    @CachePut(value = "books", key = "#newBook.id")
//    @CacheEvict(value = "books", key = "#id")
    @CacheEvict(value = "books", key = "#root.methodName",allEntries = true)
    public void updateBook(Book newBook, Long id) {
        Book oldBook = bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
        newBook.setId(oldBook.getId());
        bookRepository.save(newBook);
    }

    //    @CacheEvict(value = "books", key = "#id")
    @CacheEvict(value = "books", key = "#root.methodName",allEntries = true)
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }


}
