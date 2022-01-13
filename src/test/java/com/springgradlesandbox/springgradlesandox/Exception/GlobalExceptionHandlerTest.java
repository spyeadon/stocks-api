package com.springgradlesandbox.springgradlesandox.Exception;

import com.springgradlesandbox.springgradlesandox.Controller.UserController;
import com.springgradlesandbox.springgradlesandox.DTO.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleAPIException_respondsWith500InternalServerError() {
        APIException exception = new APIException("An Internal Server Error occurred!");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAPIException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(Objects.requireNonNull(response.getBody()).getMessage()).isEqualTo(exception.getMessage());
    }

    @Test
    void handleBadRequest_respondsWith400BadRequest() throws NoSuchMethodException {
        BindingResult result = new BeanPropertyBindingResult(new UserDTO(), "userDTO");
        result.addError(new FieldError("userDTO", "clientName", "Invalid argument for field: clientName"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(new MethodParameter(UserController.class.getMethod("createUser", UserDTO.class), 0), result);

        ResponseEntity<List<ErrorResponse>> response = globalExceptionHandler.handleBadRequest(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(response.getBody()).get(0).getFieldName()).isEqualTo("clientName");
        assertThat(Objects.requireNonNull(response.getBody()).get(0).getMessage()).isEqualTo("Invalid argument for field: clientName");
        assertThat(Objects.requireNonNull(response.getBody()).get(0).getExceptionType()).isEqualTo("FieldError");
    }
}