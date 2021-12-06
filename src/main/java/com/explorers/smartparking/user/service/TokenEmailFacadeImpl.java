package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.persistence.model.Token;
import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.web.error.InvalidTokenException;
import com.explorers.smartparking.user.web.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional
public class TokenEmailFacadeImpl implements TokenEmailFacade {

    private final RegistrationService registrationService;
    private final TokenService tokenService;
    private final MailUtil mailUtil;

    @Autowired
    public TokenEmailFacadeImpl(RegistrationService registrationService,
                                TokenService tokenService,
                                MailUtil mailUtil) {
        this.registrationService = registrationService;
        this.tokenService = tokenService;
        this.mailUtil = mailUtil;
    }

    @Override
    public void createAndSendVerificationToken(User user, HttpServletRequest request) {
        Token token = tokenService.createVerificationToken(user);

        mailUtil.sendVerificationTokenEmail(
                getAppUrl(request),
                request.getLocale(),
                token.getToken(),
                user.getEmail());
    }

    @Override
    public void updateAndSendVerificationToken(String userEmail, HttpServletRequest request) {
        User user = registrationService.findByEmail(userEmail);

        if (user.isEnabled()) {
            throw new InvalidTokenException("userAlreadyEnable");
        }
        Token token = tokenService.createVerificationToken(user);

        mailUtil.sendVerificationTokenEmail(
                getAppUrl(request),
                request.getLocale(),
                token.getToken(),
                userEmail);
    }

    @Override
    public void createAndSendPasswordResetToken(String userEmail, HttpServletRequest request) {
        User user = registrationService.findByEmail(userEmail);
        Token token = tokenService.createPasswordResetToken(user);

        mailUtil.sendResetPasswordTokenEmail(
                getAppUrl(request),
                request.getLocale(),
                token.getToken(),
                user.getEmail());
    }

    private String getAppUrl(HttpServletRequest request) {
        return String.format("http://%s:%d%s",
                request.getServerName(), request.getServerPort(), request.getContextPath());
    }
}
