package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.persistence.model.User;

public interface TokenEmailFacade {

    void createAndSendVerificationToken(User user);

    void updateAndSendVerificationToken(String userEmail);

    void createAndSendPasswordResetToken(String userEmail);
}
