package com.maids.cc.Library_Management_System.controller;

import com.maids.cc.Library_Management_System.model.Librarian;
import com.maids.cc.Library_Management_System.service.LibrarianService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = LibrarianController.class)
class LibrarianControllerTest extends BaseControllerTest {

    @MockBean
    LibrarianService librarianService;
    Librarian librarian;

    @BeforeEach
    void setup() {
        librarian = Librarian.builder()
                .password("A")
                .role("A")
                .userName("A")
                .build();
    }

    @Test
    void saveLibrarian() throws Exception {
        String libStr = objectMapper.writeValueAsString(librarian);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/librarian/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(libStr))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(libStr));
    }


}