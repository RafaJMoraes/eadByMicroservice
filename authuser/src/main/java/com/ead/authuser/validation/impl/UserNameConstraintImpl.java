package com.ead.authuser.validation.impl;

import com.ead.authuser.validation.UserNameConstraint;
import org.apache.catalina.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class UserNameConstraintImpl implements ConstraintValidator<UserNameConstraint, String> {
    @Override
    public void initialize(UserNameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return username != null && !username.isEmpty() && !username.contains(" ");
    }
}
