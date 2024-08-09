package com.maids.cc.Library_Management_System.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Serves as the user of the system
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "userName", name = "unique_name"))
@Builder
public class Librarian {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    @NotNull
    private String role;


}
