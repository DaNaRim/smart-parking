package com.explorers.smartparking.registration.web.validator;

import com.explorers.smartparking.registration.web.dto.RegistrationDto;
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
