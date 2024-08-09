package com.maids.cc.Library_Management_System.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.PatronNotFoundException;
import com.maids.cc.Library_Management_System.model.Patron;
import com.maids.cc.Library_Management_System.service.PatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatronController.class)
class PatronControllerTest extends BaseControllerTest{

    @MockBean
    PatronService patronService;

    private static final String API_ENDPOINT = "/api/patrons";
    private static final String ID_PARAM = "/{id}";

    private Patron patron;

    @BeforeEach
    void setup() {
        patron = Patron.builder()
                .id(1L)
                .name("hamada")
                .contactInfo("01111111111")
                .build();
    }

    private String getPatronString() throws JsonProcessingException {
        return objectMapper.writeValueAsString(patron);
    }


    @Test
    void getAllPatronsSuccessfully() throws Exception {
        List<Patron> patrons = new ArrayList<>(List.of(patron, new Patron()));
        Mockito.when(patronService.getAllPatrons())
                .thenReturn(patrons);
        mockMvc.perform(get(API_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(patrons)));
    }

    @Test
    void returnEmptyListWhenNoPatronsSaved() throws Exception {
        Mockito.when(patronService.getAllPatrons())
                .thenReturn(new ArrayList<>());
        mockMvc.perform(get(API_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void findByIdSuccessfully() throws Exception {
        Mockito.when(patronService.findPatronById(patron.getId()))
                .thenReturn(patron);
        mockMvc.perform(get(API_ENDPOINT + ID_PARAM, patron.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(getPatronString()));
    }

    @Test
    void shouldReturnNotFoundWhenIdIsInvalid() throws Exception {
        Mockito.when(patronService.findPatronById(ArgumentMatchers.anyLong()))
                .thenThrow(PatronNotFoundException.class);
        mockMvc.perform(get(API_ENDPOINT + ID_PARAM, INVALID_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void savePatronSuccessfully() throws Exception {
        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getPatronString()))
                .andExpect(status().isCreated())
                .andExpect(content().json(getPatronString()));
                Mockito.verify(patronService, Mockito.times(1))
                .savePatron(patron);
    }

    @Test
    void updatePatron() throws Exception {
        Patron updatedPatron = patron;
        patron.setName("newName");
        String updatePatronString = objectMapper.writeValueAsString(updatedPatron);
        Mockito.when(patronService.updatePatron(updatedPatron, patron.getId()))
                .thenReturn(updatedPatron);
        mockMvc.perform(put(API_ENDPOINT + ID_PARAM, patron.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePatronString))
                .andExpect(status().isOk())
                .andExpect(content().json(updatePatronString));
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete(API_ENDPOINT + ID_PARAM, patron.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestOnValidationExceptions() throws Exception{
        String invalidPatronString = objectMapper.writeValueAsString(new Patron());
        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPatronString))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put(API_ENDPOINT + ID_PARAM, patron.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPatronString))
                .andExpect(status().isBadRequest());

    }


}