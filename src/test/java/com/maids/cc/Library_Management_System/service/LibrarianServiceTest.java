package com.maids.cc.Library_Management_System.service;

import com.maids.cc.Library_Management_System.config.TestSecurityConfig;
import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.UserNotFoundException;
import com.maids.cc.Library_Management_System.model.Librarian;
import com.maids.cc.Library_Management_System.repository.LibrarianRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibrarianServiceTest {

    @Mock
    LibrarianRepository librarianRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    LibrarianService librarianService;
    Librarian librarian;

    @BeforeEach
    void setup() {
        librarian = Librarian.builder()
                .id(1L)
                .userName("A")
                .password("A")
                .role("A")
                .build();
    }

    @Test
    public void testFindByUserName_Success() {
        String userName = librarian.getUserName();
        librarian.setUserName(userName);
        when(librarianRepository.findByUserName(userName)).thenReturn(Optional.of(librarian));
        Librarian result = librarianService.findByUserName(userName);
        assertThat(userName).isEqualTo(result.getUserName());
        verify(librarianRepository).findByUserName(userName);
    }

    @Test
    public void testFindByUserName_NotFound() {
        String userName = "INVALID";
        when(librarianRepository.findByUserName(userName)).thenReturn(Optional.empty());
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> librarianService.findByUserName(userName));
        verify(librarianRepository).findByUserName(userName);
    }

    @Test
    public void testSave() {
        librarian.setPassword("plainPassword");
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(librarian.getPassword())).thenReturn(encodedPassword);
        librarianService.save(librarian);
        verify(passwordEncoder).encode("plainPassword");
        verify(librarianRepository).save(librarian);
        assertEquals(encodedPassword, librarian.getPassword());
    }
}