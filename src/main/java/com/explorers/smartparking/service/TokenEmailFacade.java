package com.explorers.smartparking.service;

import com.explorers.smartparking.persistence.model.User;

import javax.servlet.http.HttpServletRequest;

public interface TokenEmailFacade {

    void createAndSendVerificationToken(User user, HttpServletRequest request);

}
