package com.maids.cc.Library_Management_System.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BorrowingRecord {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Borrow date can't be null")
    private LocalDate borrowDate;

    private LocalDate returnDate;

    @NotNull(message = "Patron can't be null")
    @ManyToOne
    private Patron patron;

    @NotNull(message = "Book can't be null")
    @ManyToOne
    private Book book;

    public boolean isReturned() {
        return returnDate != null;
    }

}
