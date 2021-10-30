package com.explorers.smartparking.service;

import com.explorers.smartparking.persistence.model.Token;
import com.explorers.smartparking.persistence.model.User;
import com.explorers.smartparking.web.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional
public class TokenEmailFacadeImpl implements TokenEmailFacade {

    private final UserService userService;
    private final TokenService tokenService;
    private final MailUtil mailUtil;

    @Autowired
    public TokenEmailFacadeImpl(UserService userService,
                                TokenService tokenService,
                                MailUtil mailUtil) {
        this.userService = userService;
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

    private String getAppUrl(HttpServletRequest request) {
        return String.format("http://%s:%d%s",
                request.getServerName(), request.getServerPort(), request.getContextPath());
    }
}
