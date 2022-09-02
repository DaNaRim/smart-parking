package com.explorers.smartparking.user.web.validator;

import org.passay.*;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String PASSAY_MESSAGE_FILE_PATH = "src/main/resources/messages/%s.properties";

    /**
     * Don`t forget to update the message in src\main\resources\messages\
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(generateMessageResolver(), Arrays.asList(
                new LengthRule(8, 30),
//                new UppercaseCharacterRule(1),
//                new DigitCharacterRule(1),
//                new SpecialCharacterRule(1),
                new WhitespaceRule())
        );

        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) return true;

        context.buildConstraintViolationWithTemplate(validator.getMessages(result).stream().findFirst()
                        .orElse(context.getDefaultConstraintMessageTemplate()))
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }

    private MessageResolver generateMessageResolver() {
        String path = String.format(PASSAY_MESSAGE_FILE_PATH, LocaleContextHolder.getLocale().getLanguage());

        try (FileInputStream fis = new FileInputStream(path)) {
            Properties props = new Properties();

            props.load(new InputStreamReader(fis, StandardCharsets.UTF_8));
            return new PropertiesMessageResolver(props);

        } catch (IOException e) {
            throw new RuntimeException("Error while loading Passay messages", e);
        }
    }

}
