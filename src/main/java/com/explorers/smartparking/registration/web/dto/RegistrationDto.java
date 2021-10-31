package com.explorers.smartparking.registration.web.dto;

import com.explorers.smartparking.registration.web.validator.PasswordMatches;
import com.explorers.smartparking.registration.web.validator.ValidEmail;
import com.explorers.smartparking.registration.web.validator.ValidPassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@PasswordMatches
public class RegistrationDto {

    @NotBlank
    @Size(min = 2, max = 35)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 35)
    private String lastName;

    @ValidPassword
    @NotBlank
    private String password;

    @NotBlank
    private String matchingPassword;

    @ValidEmail
    @NotBlank
    private String email;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
