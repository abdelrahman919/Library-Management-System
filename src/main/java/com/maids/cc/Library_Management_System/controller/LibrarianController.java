package com.maids.cc.Library_Management_System.controller;

import com.maids.cc.Library_Management_System.model.Librarian;
import com.maids.cc.Library_Management_System.service.LibrarianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/librarian")
@RequiredArgsConstructor
public class LibrarianController {

    private final LibrarianService librarianService;

    @PostMapping("/register")
    public ResponseEntity<Librarian> saveLibrarian(@RequestBody Librarian librarian) {
        librarianService.save(librarian);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(librarian);
    }



}
