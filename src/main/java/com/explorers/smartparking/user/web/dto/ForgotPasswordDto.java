package com.explorers.smartparking.user.web.dto;

import com.explorers.smartparking.user.web.validator.PasswordMatches;
import com.explorers.smartparking.user.web.validator.ValidPassword;

import javax.validation.constraints.NotBlank;

/**
 * Don't forget to update the message in src\main\resources\i18n\
 */
@PasswordMatches(message = "{validation.user.matching.password}")
public class ForgotPasswordDto extends PasswordEntity {

    @NotBlank(message = "{validation.user.required.newPassword}")
    @ValidPassword
    private String newPassword;

    @NotBlank(message = "{validation.user.required.matchingPassword}")
    private String matchingPassword;

    private final String token;

    public ForgotPasswordDto(String token) {
        this.token = token;
    }

    @Override
    public String getPassword() {
        return newPassword;
    }

    @Override
    public String getMatchingPassword() {
        return matchingPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getToken() {
        return token;
    }
}
