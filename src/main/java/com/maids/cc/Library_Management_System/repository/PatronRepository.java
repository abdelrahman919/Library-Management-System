package com.maids.cc.Library_Management_System.repository;

import com.maids.cc.Library_Management_System.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {


    Optional<Patron> findByContactInfo(String phone);



}
