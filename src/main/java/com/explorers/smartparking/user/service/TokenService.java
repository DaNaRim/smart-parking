package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.persistence.model.Token;
import com.explorers.smartparking.user.persistence.model.User;

public interface TokenService {

    Token createVerificationToken(User user);

    Token updateVerificationToken(String userEmail);

    Token validateVerificationToken(String token);

    Token getVerificationToken(String verificationToken);

    Token createPasswordResetToken(User user);

    Token validatePasswordResetToken(String token);

}
