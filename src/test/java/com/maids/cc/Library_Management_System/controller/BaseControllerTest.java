package com.maids.cc.Library_Management_System.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maids.cc.Library_Management_System.config.TestSecurityConfig;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
//To disable security
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected static final Long INVALID_ID = 99L;

}
