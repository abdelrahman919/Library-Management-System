package com.maids.cc.Library_Management_System.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    //Apparently ISBNs are 10-13 digits
    @NotBlank(message = "ISBN can't be blank")
    @Size(min = 10, max = 13, message = "ISBN must be between 10 - 13 digits")
    private String isbn;

    @NotBlank(message = "Title can't be blank")
    private String title;
    @NotBlank(message = "Author can't be blank")
    private String author;
    @NotBlank(message = "genre can't be blank")
    private String genre;
    @Positive(message = "Total copies can't be less than or equal to zero")
    private int totalCopies;
    @NotNull
//    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate publicationYear;
    private int borrowedCopies;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    List<BorrowingRecord> borrowingRecords;

    public int getAvailableCopies() {
        return totalCopies - borrowedCopies;
    }











}

