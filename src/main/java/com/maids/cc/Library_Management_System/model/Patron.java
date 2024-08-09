package com.maids.cc.Library_Management_System.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maids.cc.Library_Management_System.enums.Gender;
import com.maids.cc.Library_Management_System.validation.Phone;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Patron {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Name can't be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2-50 characters")
    private String name;

    //This implementation expects contact info to be a phone
    @Phone
    @NotBlank(message = "ContactInfo can't be blank")
    private String contactInfo;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    List<BorrowingRecord> borrowingRecords;




}
