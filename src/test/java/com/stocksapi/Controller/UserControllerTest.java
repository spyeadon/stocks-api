package com.stocksapi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocksapi.DTO.UserDTO;
import com.stocksapi.Exception.GlobalExceptionHandler;
import com.stocksapi.Service.UserService;
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

    private UserDTO userRequestBody;

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

        userRequestBody = UserDTO.builder()
                .firstName("tester")
                .lastName("testerson")
                .username("test123")
                .password("pa55w0rd098")
            .build();
    }

    @Test
    void createUser_callsUserService() throws Exception {
        UserDTO request = UserDTO.builder()
                .firstName("tester")
                .lastName("testerson")
                .username("test123")
                .password("pa55w0rd098")
                .build();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestBody)));

        Mockito.verify(userService, Mockito.times(1)).createUser(request);
    }

    @Test
    void createUser_withValidRequestBody_willRespondWithResourceAnd200OK() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(userRequestBody);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestBody)))
                .andExpect(content().json(objectMapper.writeValueAsString(userRequestBody)))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_withNullUsername_willThrowException400BadRequest() throws Exception {
        userRequestBody.setUsername(null);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_withNullFirstName_willThrowException400BadRequest() throws Exception {
        userRequestBody.setFirstName(null);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_withNullLastName_willThrowException400BadRequest() throws Exception {
        userRequestBody.setLastName(null);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_withNullPassword_willThrowException400BadRequest() throws Exception {
        userRequestBody.setPassword(null);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_withShortPassword_willThrowException400BadRequest() throws Exception {
        userRequestBody.setPassword("Pa55w0r");

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestBody)))
                .andExpect(status().isBadRequest());
    }
}