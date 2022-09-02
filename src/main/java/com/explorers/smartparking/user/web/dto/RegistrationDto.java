package com.explorers.smartparking.user.web.dto;

import com.explorers.smartparking.user.web.validator.PasswordMatches;
import com.explorers.smartparking.user.web.validator.ValidEmail;
import com.explorers.smartparking.user.web.validator.ValidPassword;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Locale;

/**
 * Don't forget to update the message in src\main\resources\messages\
 */
@PasswordMatches(message = "{validation.user.matching.password}")
public class RegistrationDto extends PasswordEntity {

    @NotBlank(message = "{validation.user.required.firstName}")
    @Size(min = 2, max = 35, message = "{validation.user.size.firstName}")
    private String firstName;

    @NotBlank(message = "{validation.user.required.lastName}")
    @Size(min = 2, max = 35, message = "{validation.user.size.lastName}")
    private String lastName;

    @ValidPassword
    @NotBlank(message = "{validation.user.required.password}")
    private String password;

    @NotBlank(message = "{validation.user.required.confirmPassword}")
    private String matchingPassword;

    @ValidEmail(message = "{validation.user.valid.email}")
    @NotBlank(message = "{validation.user.required.email}")
    private String email;

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
