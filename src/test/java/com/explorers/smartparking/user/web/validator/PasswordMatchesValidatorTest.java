package com.explorers.smartparking.user.web.validator;

import com.explorers.smartparking.user.web.dto.ForgotPasswordDto;
import com.explorers.smartparking.user.web.dto.RegistrationDto;
import com.explorers.smartparking.user.web.dto.UpdatePasswordDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordMatchesValidatorTest {

    private final PasswordMatchesValidator validator = new PasswordMatchesValidator();

    @Test
    void isValidWithValidRegistrationDto() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setPassword("password");
        registrationDto.setMatchingPassword("password");

        boolean isValidPassword = validator.isValid(registrationDto, null);
        assertTrue(isValidPassword);

        registrationDto.setPassword("invalid");

        boolean isValidPassword2 = validator.isValid(registrationDto, null);
        assertFalse(isValidPassword2);
    }

    @Test
    void isValidWithValidForgotPasswordDto() {
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto("token");
        forgotPasswordDto.setNewPassword("password");
        forgotPasswordDto.setMatchingPassword("password");

        boolean isValidForgotPassword = validator.isValid(forgotPasswordDto, null);
        assertTrue(isValidForgotPassword);

        forgotPasswordDto.setNewPassword("invalid");

        boolean isValidForgotPassword2 = validator.isValid(forgotPasswordDto, null);
        assertFalse(isValidForgotPassword2);
    }

    @Test
    void isValidWithValidUpdatePasswordDto() {
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        updatePasswordDto.setNewPassword("password");
        updatePasswordDto.setMatchingPassword("password");

        boolean isValidUpdatePassword = validator.isValid(updatePasswordDto, null);
        assertTrue(isValidUpdatePassword);

        updatePasswordDto.setNewPassword("invalid");

        boolean isValidUpdatePassword2 = validator.isValid(updatePasswordDto, null);
        assertFalse(isValidUpdatePassword2);
    }

    @Test
    void isValidWithInvalidClass() {
        assertThrows(RuntimeException.class, () -> validator.isValid(new Object(), null));
    }
}
