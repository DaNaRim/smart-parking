package com.explorers.smartparking.registration.service;

import com.explorers.smartparking.registration.persistence.model.User;
import com.explorers.smartparking.registration.web.dto.RegistrationDto;

public interface UserService {

    User registerNewUserAccount(RegistrationDto registrationDto);

    void enableUser(String token);

}
