package com.explorers.smartparking.registration.service;

import com.explorers.smartparking.registration.persistence.model.Token;
import com.explorers.smartparking.registration.persistence.model.User;

public interface TokenService {

    Token createVerificationToken(User user);

    Token validateVerificationToken(String token);

}
