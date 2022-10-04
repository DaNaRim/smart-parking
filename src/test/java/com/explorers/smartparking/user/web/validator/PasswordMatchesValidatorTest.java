package com.explorers.smartparking.user.web.validator;

import com.explorers.smartparking.user.web.dto.ForgotPasswordDto;
import com.explorers.smartparking.user.web.dto.RegistrationDto;
import com.explorers.smartparking.user.web.dto.UpdatePasswordDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PasswordMatchesValidatorTest {

    private static final MockedStatic<LogFactory> loggerFactoryMock = mockStatic(LogFactory.class);
    private static final Log logger = mock(Log.class);

    private final PasswordMatchesValidator validator = new PasswordMatchesValidator();

    @BeforeAll
    public static void beforeClass() {
        loggerFactoryMock.when(() -> LogFactory.getLog(any(Class.class))).thenReturn(logger);
    }

    @AfterAll
    static void afterAll() {
        loggerFactoryMock.close();
    }

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
        verify(logger).error(anyString(), any(ClassCastException.class));
    }
}
