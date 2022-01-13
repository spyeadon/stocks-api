package com.springgradlesandbox.springgradlesandox.Validation;

import com.springgradlesandbox.springgradlesandox.DTO.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private UserValidator userValidator;

    @Mock
    private ConstraintValidatorContext context;

    private UserDTO user;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
        user = UserDTO.builder()
                .accountNumber(12345)
                .name("test client")
            .build();
    }

    @Test
    void isValid_whenUserHasMemberIDButNotMemberNumber_returnsTrue() {
        user.setMemberId("1234567890");
        user.setMemberNumber(null);

        boolean isValid = userValidator.isValid(user, context);

        assertTrue(isValid);
    }

    @Test
    void isValid_whenUserHasMemberNumberButNotMemberId_returnsTrue() {
        user.setMemberNumber("123456798");
        user.setMemberId(null);

        boolean isValid = userValidator.isValid(user, context);

        assertTrue(isValid);
    }

    @Test
    void isValid_whenUserHasNullMemberIdAndMemberNumber_returnsFalse() {
        user.setMemberNumber(null);
        user.setMemberId(null);

        boolean isValid = userValidator.isValid(user, context);

        assertFalse(isValid);
    }

    @Test
    void isValid_whenUserHasBlankMemberIdAndMemberNumber_returnsFalse() {
        user.setMemberNumber("");
        user.setMemberId("");

        boolean isValid = userValidator.isValid(user, context);

        assertFalse(isValid);
    }
}