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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
    void createUser_withValidRequestBody_invokesServiceAndRespondSWithResourceAnd200OK() throws Exception {
        given(userService.createUser(any(UserDTO.class))).willReturn(userRequestBody);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestBody)))
                .andExpect(content().json(objectMapper.writeValueAsString(userRequestBody)))
                .andExpect(status().isOk());

        then(userService).should().createUser(userRequestBody);
    }

    @Test
    void createUser_withNullUsername_willThrowException400BadRequest() throws Exception {
        userRequestBody.setUsername(null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_withNullFirstName_willThrowException400BadRequest() throws Exception {
        userRequestBody.setFirstName(null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_withNullLastName_willThrowException400BadRequest() throws Exception {
        userRequestBody.setLastName(null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_withNullPassword_willThrowException400BadRequest() throws Exception {
        userRequestBody.setPassword(null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_withShortPassword_willThrowException400BadRequest() throws Exception {
        userRequestBody.setPassword("Pa55w0r");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestBody)))
                .andExpect(status().isBadRequest());
    }
}