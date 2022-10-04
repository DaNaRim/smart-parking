package com.explorers.smartparking.user.web.validator;

import com.explorers.smartparking.user.web.dto.PasswordEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    private final Log logger = LogFactory.getLog(PasswordMatchesValidator.class);

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            PasswordEntity passwordEntity = (PasswordEntity) obj;
            return StringUtils.equals(passwordEntity.getPassword(), passwordEntity.getMatchingPassword());
        } catch (ClassCastException e) {
            logger.error("obj is not a valid type", e);
            throw new RuntimeException("obj is not a valid type", e);
        }
    }
}
