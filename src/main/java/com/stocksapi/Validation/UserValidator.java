package com.stocksapi.Validation;

import com.stocksapi.DTO.UserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserValidator implements ConstraintValidator<ValidUser, UserDTO> {

    public void initialize(ValidUser validUser) {}

    public boolean isValid(UserDTO user, ConstraintValidatorContext context) {
        boolean isValidMemberNumber = user.getMemberNumber() != null && !(user.getMemberNumber().isBlank());
        boolean isValidMemberId = user.getMemberId() != null && !(user.getMemberId().isBlank());
        return isValidMemberNumber || isValidMemberId;
    }
}
