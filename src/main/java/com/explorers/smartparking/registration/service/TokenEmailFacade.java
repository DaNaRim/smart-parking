package com.explorers.smartparking.registration.service;

import com.explorers.smartparking.registration.persistence.model.User;

import javax.servlet.http.HttpServletRequest;

public interface TokenEmailFacade {

    void createAndSendVerificationToken(User user, HttpServletRequest request);

}
