package com.explorers.smartparking.user.web.dto;

import com.explorers.smartparking.user.web.validator.PasswordMatches;
import com.explorers.smartparking.user.web.validator.ValidPassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@PasswordMatches
public class UpdatePasswordDto extends PasswordEntity {

    @NotBlank
    private String oldPassword;

    @ValidPassword
    @NotBlank
    @Size(min = 8, max = 30)
    private String newPassword;

    @NotBlank
    private String matchingPassword;

    @Override
    public String getPassword() {
        return newPassword;
    }

    @Override
    public String getMatchingPassword() {
        return matchingPassword;
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

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}
