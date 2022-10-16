package com.explorers.smartparking.user.web.dto;

import com.explorers.smartparking.user.web.validator.PasswordMatches;
import com.explorers.smartparking.user.web.validator.ValidPassword;

import javax.validation.constraints.NotBlank;

/**
 * Don't forget to update the message in src\main\resources\i18n\
 */
@PasswordMatches(message = "{validation.user.matching.password}")
public class UpdatePasswordDto extends PasswordEntity {

    @NotBlank(message = "{validation.user.required.oldPassword}")
    private String oldPassword;

    @ValidPassword
    @NotBlank(message = "{validation.user.required.newPassword}")
    private String newPassword;

    @NotBlank(message = "{validation.user.required.matchingPassword}")
    private String matchingPassword;

    @Override
    public String getPassword() {
        return newPassword;
    }

    @Override
    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
