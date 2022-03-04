package com.springgradlesandbox.springgradlesandox.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springgradlesandbox.springgradlesandox.DTO.UserDTO;
import com.springgradlesandbox.springgradlesandox.Exception.GlobalExceptionHandler;
import com.springgradlesandbox.springgradlesandox.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                    .standaloneSetup(userController)
                    .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createUser_callsUserService() throws Exception {
        UserDTO request = UserDTO.builder()
                .accountNumber(12345678)
                .name("Test Client")
                .memberNumber("12345678")
                .memberId("12345")
                .build();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        Mockito.verify(userService, Mockito.times(1)).createUser(request);
    }

    @Test
    void createUser_withValidRequestBody_willRespondWithResourceAnd200OK() throws Exception {
        UserDTO request = UserDTO.builder()
                .accountNumber(12345678)
                .name("Test Client")
                .memberNumber("12345678")
                .memberId("12345")
                .build();

        when(userService.createUser(any(UserDTO.class))).thenReturn(request);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(content().json(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_withNullAccountNumber_willThrowException400BadRequest() throws Exception {
        UserDTO request = UserDTO.builder()
                .accountNumber(null)
                .name("Test Client")
                .build();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_withNullClientName_willThrowException400BadRequest() throws Exception {
        UserDTO request = UserDTO.builder()
                .accountNumber(12345678)
                .name(null)
                .build();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}