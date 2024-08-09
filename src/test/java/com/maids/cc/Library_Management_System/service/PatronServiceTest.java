package com.maids.cc.Library_Management_System.service;

import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.PatronNotFoundException;
import com.maids.cc.Library_Management_System.model.Patron;
import com.maids.cc.Library_Management_System.repository.PatronRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PatronServiceTest {

    @Mock
    PatronRepository patronRepository;

    PatronService patronService;

    @Captor
    ArgumentCaptor<Patron> argumentCaptor;

    Patron patron;

    private static final Long INVALID_ID = 99L;

    @BeforeEach
    void setup() {
        patronService = new PatronService(patronRepository);
        patron = Patron.builder()
                .id(1L)
                .name("hamada")
                .build();
    }



    @Test
    void shouldReturnAllPatrons() {
        List<Patron> patrons = new ArrayList<>(List.of(patron, new Patron()));
        Mockito.when(patronRepository.findAll())
                .thenReturn(patrons);
        List<Patron> result = patronService.getAllPatrons();
        assertThat(patrons).isEqualTo(result);
    }

    @Test
    void shouldReturnEmptyListWhenNoPatronsSaved() {
        Mockito.when(patronRepository.findAll())
                .thenReturn(new ArrayList<>());
        List<Patron> result = patronService.getAllPatrons();
        assertThat(result).isEqualTo(new ArrayList<>());
    }

    @Test
    void findExistingPatronById() {
        Mockito.when(patronRepository.findById(patron.getId()))
                .thenReturn(Optional.ofNullable(patron));

        Patron patronById = patronService.findPatronById(patron.getId());
        assertThat(patronById).isEqualTo(patron);
    }

    @Test
    void shouldThrowExceptionWhenPatronNotFound() {
        Mockito.when(patronRepository.findById(ArgumentMatchers.anyLong()))
                .thenThrow(EntityNotFoundException.class);
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> patronService.findPatronById(INVALID_ID));
    }

    @Test
    void savePatron() {
        patronService.savePatron(patron);
        Mockito.verify(patronRepository, Mockito.times(1))
                .save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(patron);
    }

    @Test
    void updatePatron() {
        Patron updatedPatron = Patron.builder()
                .name("newName")
                .build();
        Mockito.when(patronRepository.findById(patron.getId()))
                .thenReturn(Optional.ofNullable(patron));
        patronService.updatePatron(updatedPatron, patron.getId());
        Mockito.verify(patronRepository, Mockito.times(1))
                .save(argumentCaptor.capture());
        assertThat(updatedPatron).isEqualTo(argumentCaptor.getValue());
    }

    @Test
    void shouldThrowExceptionWhenPatronToUpdateNotFound() {
        Mockito.when(patronRepository.findById(ArgumentMatchers.anyLong()))
                .thenThrow(PatronNotFoundException.class);
        assertThatExceptionOfType(PatronNotFoundException.class)
                .isThrownBy(() -> patronService.updatePatron(new Patron(), INVALID_ID));
    }

    @Test
    void deleteExistingPatronById() {
        patronService.deletePatronById(patron.getId());
        Mockito.verify(patronRepository,Mockito.times(1))
                .deleteById(patron.getId());
    }

    @Test
    void shouldThrowExceptionWhenPatronToDeleteNotFound() {
        Mockito.doThrow(IllegalArgumentException.class)
                .when(patronRepository)
                .deleteById(ArgumentMatchers.anyLong());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> patronService.deletePatronById(INVALID_ID));
        Mockito.verify(patronRepository,Mockito.times(1))
                        .deleteById(INVALID_ID);
    }


}