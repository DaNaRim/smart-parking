package com.explorers.smartparking.user.web.validator;

import com.explorers.smartparking.user.web.dto.PasswordEntity;
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

        try {
            PasswordEntity passwordEntity = (PasswordEntity) obj;
            return passwordEntity.getPassword().equals(passwordEntity.getMatchingPassword());
        } catch (ClassCastException e) {
            logger.error("obj is not a valid type");
            throw new RuntimeException("obj is not a valid type");
        }
    }
}
