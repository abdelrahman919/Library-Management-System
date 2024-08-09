package com.maids.cc.Library_Management_System.controller;

import com.maids.cc.Library_Management_System.model.BorrowingRecord;
import com.maids.cc.Library_Management_System.model.Patron;
import com.maids.cc.Library_Management_System.service.PatronService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
@RequiredArgsConstructor
public class PatronController {

    private final PatronService patronService;


    @GetMapping
    public List<Patron> getAllPatrons() {
        return patronService.getAllPatrons();
    }

    @GetMapping("/{id}")
    public Patron findById(@PathVariable Long id) {
        return patronService.findPatronById(id);
    }


    @PostMapping
    public ResponseEntity<Patron> savePatron(@RequestBody @Valid Patron patron) {
        patronService.savePatron(patron);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(patron);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patron> updatePatron(@PathVariable Long id,
                                               @RequestBody @Valid Patron updatedPatron) {

        Patron patron = patronService.updatePatron(updatedPatron, id);
        return ResponseEntity.ok(patron);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        patronService.deletePatronById(id);
        return ResponseEntity
                .noContent()
                .build();
    }





}
