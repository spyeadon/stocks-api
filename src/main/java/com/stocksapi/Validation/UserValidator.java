package com.stocksapi.Validation;

import com.stocksapi.DTO.UserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserValidator implements ConstraintValidator<ValidUser, UserDTO> {

    public void initialize(ValidUser validUser) {}

    // TODO modify validation to allow 1 special character somewhere in the password
    public boolean isValid(UserDTO user, ConstraintValidatorContext context) {
        if (user.getUsername() == null) return false;
        if (user.getPassword() == null) return false;
        return
            user.getUsername().matches("^[0-9a-zA-Z]*$") &&
            user.getPassword().matches("^[0-9a-zA-Z]*$");
    }
}
