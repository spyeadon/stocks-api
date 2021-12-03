package com.springgradlesandbox.springgradlesandox.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springgradlesandbox.springgradlesandox.DTO.UserDTO;
import com.springgradlesandbox.springgradlesandox.Exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                    .standaloneSetup(UserController.class)
                    .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testValidation_withValidRequestBody_willResponseWith200OK() throws Exception {
        UserDTO request = UserDTO.builder()
                .accountNumber(12345678)
                .clientName("Test Client")
                .memberNumber("12345678")
                .memberId("12345")
                .build();

        mockMvc.perform(post("/testValidation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(content().json(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testValidation_withNullAccountNumber_willThrowException400BadRequest() throws Exception {
        UserDTO request = UserDTO.builder()
                .accountNumber(null)
                .clientName("Test Client")
                .build();

        mockMvc.perform(post("/testValidation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testValidation_withNullClientName_willThrowException400BadRequest() throws Exception {
        UserDTO request = UserDTO.builder()
                .accountNumber(12345678)
                .clientName(null)
                .build();

        mockMvc.perform(post("/testValidation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}