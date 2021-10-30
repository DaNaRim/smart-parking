package com.explorers.smartparking.service;

import com.explorers.smartparking.persistence.model.Token;
import com.explorers.smartparking.persistence.model.User;

public interface TokenService {

    Token createVerificationToken(User user);

    Token validateVerificationToken(String token);

}
