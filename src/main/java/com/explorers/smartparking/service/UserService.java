package com.explorers.smartparking.service;

import com.explorers.smartparking.persistence.model.User;
import com.explorers.smartparking.web.dto.RegistrationDto;

public interface UserService {

    User registerNewUserAccount(RegistrationDto registrationDto);

    void enableUser(String token);

}
