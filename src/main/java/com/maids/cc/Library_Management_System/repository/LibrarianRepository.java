package com.maids.cc.Library_Management_System.repository;

import com.maids.cc.Library_Management_System.model.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long> {
    Optional<Librarian> findByUserName(String userName);

}
