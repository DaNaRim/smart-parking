package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.web.dto.ForgotPasswordDto;
import com.explorers.smartparking.user.web.dto.RegistrationDto;
import com.explorers.smartparking.user.web.dto.UpdatePasswordDto;

public interface RegistrationService {

    User registerNewUserAccount(RegistrationDto registrationDto);

    void enableUser(String token);

    User findById(long id);

    User findByEmail(String email);

    void changeForgottenPassword(ForgotPasswordDto passwordDto);

    void changeUserPassword(long userId, UpdatePasswordDto passwordDto);

}
