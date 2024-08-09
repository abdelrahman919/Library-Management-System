package com.maids.cc.Library_Management_System.repository;

import com.maids.cc.Library_Management_System.model.Book;
import com.maids.cc.Library_Management_System.model.BorrowingRecord;
import com.maids.cc.Library_Management_System.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    List<BorrowingRecord> findAllByBookAndPatron(Book book, Patron patron);

}
