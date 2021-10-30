package com.explorers.smartparking.web.validator;

import com.explorers.smartparking.web.dto.RegistrationDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    private final Log logger = LogFactory.getLog(PasswordMatchesValidator.class);

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        RegistrationDto user = (RegistrationDto) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
