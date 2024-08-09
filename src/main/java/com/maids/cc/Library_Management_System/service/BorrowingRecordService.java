package com.maids.cc.Library_Management_System.service;

import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.BookNotFoundException;
import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.BorrowValidationException;
import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.PatronNotFoundException;
import com.maids.cc.Library_Management_System.model.Book;
import com.maids.cc.Library_Management_System.model.BorrowingRecord;
import com.maids.cc.Library_Management_System.model.Patron;
import com.maids.cc.Library_Management_System.repository.BookRepository;
import com.maids.cc.Library_Management_System.repository.BorrowingRecordRepository;
import com.maids.cc.Library_Management_System.repository.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowingRecordService {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;


    //Transactional to make sure copy numbers are consistent
    @Transactional
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(PatronNotFoundException::new);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(BookNotFoundException::new);

        validateBorrowing(book, patron);

        BorrowingRecord borrowingRecord = BorrowingRecord.builder()
                .book(book)
                .patron(patron)
                .borrowDate(LocalDate.now())
                .build();
        borrowingRecordRepository.save(borrowingRecord);

        book.setBorrowedCopies(book.getBorrowedCopies() + 1);
        bookRepository.save(book);

        return borrowingRecord;
    }

    @Transactional
    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(PatronNotFoundException::new);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(BookNotFoundException::new);
        BorrowingRecord borrowingRecord = validateReturn(book, patron);
        borrowingRecord.setReturnDate(LocalDate.now());
        borrowingRecordRepository.save(borrowingRecord);
        book.setBorrowedCopies(book.getBorrowedCopies() - 1);
        bookRepository.save(book);
        return borrowingRecord;
    }

    private void validateBorrowing(Book book, Patron patron) {
        if (book.getAvailableCopies() <= 0) {
            throw new BorrowValidationException("Book is out of stock" +
                    ", and not available for borrowing at the moment");
        }
        List<BorrowingRecord> borrowingRecords = borrowingRecordRepository.findAllByBookAndPatron(book, patron);
        //If list is empty then this book wasn't borrowed before
        //So no need for further validation
        if (borrowingRecords.isEmpty()) {
            return;
        }
        //Means if last time this book was borrowed it wasn't returned
        if (!borrowingRecords.getLast().isReturned()) {
            throw new BorrowValidationException("Book is already borrowed," +
                    "Can't borrow the same book again");
        }
    }

    private BorrowingRecord validateReturn(Book book, Patron patron) {
        List<BorrowingRecord> borrowingRecords = borrowingRecordRepository.findAllByBookAndPatron(book, patron);
        //IF we try to return a book that hasn't been borrowed
        if (borrowingRecords.isEmpty()) {
            throw new BorrowValidationException("No borrowing records were found for this book");
        }
        BorrowingRecord borrowingRecord = borrowingRecords.getLast();
        //If it's already been returned
        if (borrowingRecord.isReturned()) {
            throw new BorrowValidationException("Book already returned");
        }
        return borrowingRecord;
    }


}
