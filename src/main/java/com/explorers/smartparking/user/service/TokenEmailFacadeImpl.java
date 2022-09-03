package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.error.InvalidTokenException;
import com.explorers.smartparking.user.persistence.model.Token;
import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.web.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    public void createAndSendVerificationToken(User user) {
        Token token = tokenService.createVerificationToken(user); //TODO optimize

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        mailUtil.sendVerificationTokenEmail(
                baseUrl,
                LocaleContextHolder.getLocale(),
                token.getToken(),
                user.getEmail()
        );
    }

    @Override
    public void updateAndSendVerificationToken(String userEmail) {
        User user = userService.findByEmail(userEmail);

        if (user.isEnabled()) {
            throw new InvalidTokenException("userAlreadyEnable");
        }
        Token token = tokenService.createVerificationToken(user);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        mailUtil.sendVerificationTokenEmail(
                baseUrl,
                LocaleContextHolder.getLocale(),
                token.getToken(),
                userEmail
        );
    }

    @Override
    public void createAndSendPasswordResetToken(String userEmail) {
        User user = userService.findByEmail(userEmail); //TODO optimize
        Token token = tokenService.createPasswordResetToken(user);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        mailUtil.sendResetPasswordTokenEmail(
                baseUrl,
                LocaleContextHolder.getLocale(),
                token.getToken(),
                user.getEmail());
    }
}
