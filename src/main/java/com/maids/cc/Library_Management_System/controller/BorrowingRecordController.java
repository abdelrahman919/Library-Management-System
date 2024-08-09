package com.maids.cc.Library_Management_System.controller;

import com.maids.cc.Library_Management_System.model.BorrowingRecord;
import com.maids.cc.Library_Management_System.service.BorrowingRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow/{bookId}/patron/{patronId}")
@RequiredArgsConstructor
public class BorrowingRecordController {

    private final BorrowingRecordService borrowingRecordService;

    @PostMapping
    public ResponseEntity<BorrowingRecord> borrowBook(@PathVariable Long bookId,
                                                      @PathVariable Long patronId) throws Exception {

        BorrowingRecord borrowingRecord = borrowingRecordService.borrowBook(bookId, patronId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(borrowingRecord);
    }

    @PutMapping
    public ResponseEntity<BorrowingRecord> returnBook(@PathVariable Long bookId,
                                                      @PathVariable Long patronId) {

        BorrowingRecord borrowingRecord = borrowingRecordService.returnBook(bookId, patronId);
        return  ResponseEntity.ok(borrowingRecord);
    }



}
