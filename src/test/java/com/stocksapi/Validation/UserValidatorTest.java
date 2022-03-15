package com.stocksapi.Validation;

import com.stocksapi.DTO.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserValidatorTest {

    private UserValidator userValidator;

    @Mock
    private ConstraintValidatorContext context;

    private UserDTO user;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
        user = UserDTO.builder()
                .firstName("tester")
                .lastName("testerson")
                .username("test123")
                .password("pa55w0rd098")
            .build();
    }

    @Test
    void isValid_whenUsernameAndPasswordAreAllNumbersAndLetters_returnsTrue() {
        boolean isValid = userValidator.isValid(user, context);

        assertTrue(isValid);
    }

    @Test
    void isValid_whenUsernameHasSpecialCharacters_returnsFalse() {
        user.setUsername("***InvalidUsername!!!!");

        boolean isValid = userValidator.isValid(user, context);

        assertFalse(isValid);
    }

    @Test
    void isValid_whenPasswordHasSpecialCharacters_returnsFalse() {
        user.setPassword("***InvalidPassword!!!!");

        boolean isValid = userValidator.isValid(user, context);

        assertFalse(isValid);
    }
}