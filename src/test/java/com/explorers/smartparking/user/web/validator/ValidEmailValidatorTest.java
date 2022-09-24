package com.explorers.smartparking.user.web.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidEmailValidatorTest {

    private final ValidEmailValidator validEmailValidator = new ValidEmailValidator();

    @Test
    void isValid() {
        assertTrue(validEmailValidator.isValid("aa@bb.cc", null));
        assertTrue(validEmailValidator.isValid("aa.bb@cc.dd", null));

        assertFalse(validEmailValidator.isValid("aa@bb", null));
        assertFalse(validEmailValidator.isValid("aa@bb.", null));
        assertFalse(validEmailValidator.isValid("a", null));
    }
}
