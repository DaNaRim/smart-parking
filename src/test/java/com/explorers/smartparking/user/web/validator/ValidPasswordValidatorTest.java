package com.explorers.smartparking.user.web.validator;

import com.explorers.smartparking.config.spring.MvcConfig;
import com.google.common.collect.Iterables;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidPasswordValidatorTest {

    private static Iterator<Locale> locales;
    private final ValidPasswordValidator validator = new ValidPasswordValidator();

    @Mock
    private ConstraintValidatorContext context;

    @BeforeAll
    static void beforeAll() {
        locales = Iterables.cycle(MvcConfig.SUPPORTED_LOCALES).iterator();
    }

    @BeforeEach
    void setUp() {
        Locale.setDefault(locales.next());

        //https://stackoverflow.com/a/42105139/14986564
        context = mock(ConstraintValidatorContext.class);

        ConstraintValidatorContext.ConstraintViolationBuilder builder
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);
    }

    /**
     * RepeatedTest param value is SUPPORTED_LOCALES size
     */
    @RepeatedTest(3)
    void isValid() {
        assertTrue(validator.isValid("12345678", context));
        assertTrue(validator.isValid("ASDEFGHI", context));
        assertTrue(validator.isValid("12345678ASDEFGHI", context));

        assertFalse(validator.isValid("1234567", context));
        verify(context, times(1)).buildConstraintViolationWithTemplate(anyString());

        assertFalse(validator.isValid("1234567890123456789012345678901", context));
        verify(context, times(2)).buildConstraintViolationWithTemplate(anyString());

        assertFalse(validator.isValid("12345678 ", context));
        verify(context, times(3)).buildConstraintViolationWithTemplate(anyString());

        assertFalse(validator.isValid("  ", context));
        verify(context, times(4)).buildConstraintViolationWithTemplate(anyString());
    }
}
