package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.persistence.model.User;

import javax.servlet.http.HttpServletRequest;

public interface TokenEmailFacade {

    void createAndSendVerificationToken(User user, HttpServletRequest request);

    void updateAndSendVerificationToken(String userEmail, HttpServletRequest request);

    void createAndSendPasswordResetToken(String userEmail, HttpServletRequest request);
}
