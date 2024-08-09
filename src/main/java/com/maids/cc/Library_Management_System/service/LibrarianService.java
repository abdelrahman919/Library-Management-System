package com.maids.cc.Library_Management_System.service;

import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.UserNotFoundException;
import com.maids.cc.Library_Management_System.model.Librarian;
import com.maids.cc.Library_Management_System.repository.LibrarianRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibrarianService {

    private final LibrarianRepository librarianRepository;
    private final PasswordEncoder passwordEncoder;

    public Librarian findByUserName(String userName) {
        return librarianRepository.findByUserName(userName)
                .orElseThrow(UserNotFoundException::new);
    }

    public void save(Librarian librarian) {
        String encodedPassword = passwordEncoder.encode(librarian.getPassword());
        librarian.setPassword(encodedPassword);

        librarianRepository.save(librarian);


    }


}
