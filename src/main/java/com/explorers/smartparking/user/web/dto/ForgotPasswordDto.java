package com.explorers.smartparking.user.web.dto;

import com.explorers.smartparking.user.web.validator.PasswordMatches;
import com.explorers.smartparking.user.web.validator.ValidPassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@PasswordMatches
public class ForgotPasswordDto extends PasswordEntity {

    @NotBlank(message = "{validation.user.required.password}")
    @ValidPassword
    private String newPassword;

    @NotBlank(message = "{validation.user.required.matchingPassword}")
    private String matchingPassword;

    @NotBlank(message = "{validation.user.required.token}")
    private String token;

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

    public void setToken(String token) {
        this.token = token;
    }
}
