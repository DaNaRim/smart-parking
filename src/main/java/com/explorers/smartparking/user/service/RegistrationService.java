package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.web.dto.ForgotPasswordDto;
import com.explorers.smartparking.user.web.dto.RegistrationDto;
import com.explorers.smartparking.user.web.dto.UpdatePasswordDto;

public interface RegistrationService {

    User registerNewUserAccount(RegistrationDto registrationDto);

    void enableUser(String token);

    void changeForgottenPassword(ForgotPasswordDto passwordDto);

}
