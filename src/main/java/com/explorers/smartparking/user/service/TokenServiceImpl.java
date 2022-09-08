package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.error.InvalidTokenException;
import com.explorers.smartparking.user.persistence.dao.TokenDao;
import com.explorers.smartparking.user.persistence.dao.UserDao;
import com.explorers.smartparking.user.persistence.model.Token;
import com.explorers.smartparking.user.persistence.model.TokenType;
import com.explorers.smartparking.user.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    private final TokenDao tokenDao;
    private final UserDao userDao;

    @Autowired
    public TokenServiceImpl(TokenDao tokenDao, UserDao userDao) {
        this.tokenDao = tokenDao;
        this.userDao = userDao;
    }

    /*
        VerificationToken
     */

    @Override
    public Token createVerificationToken(User user) {
        return tokenDao.save(new Token(user, TokenType.VERIFICATION));
    }

    @Override
    public Token validateVerificationToken(String token) {
        Token verificationToken = tokenDao.findByToken(token);

        if (verificationToken == null) {
            throw new InvalidTokenException("invalidToken");

        } else if (verificationToken.isExpired()) {
            throw new InvalidTokenException("tokenExpired");

        } else if (verificationToken.getUser().isEnabled()) {
            throw new InvalidTokenException("userAlreadyEnable");
        }
        return verificationToken;
    }

    @Override
    public Token getVerificationToken(String verificationToken) {
        return tokenDao.findByToken(verificationToken);
    }

    /*
        PasswordResetToken
     */

    @Override
    public Token createPasswordResetToken(User user) {
        return tokenDao.save(new Token(user, TokenType.PASSWORD_RESET));
    }

    @Override
    public Token validatePasswordResetToken(String token) {
        Token passToken = tokenDao.findByToken(token);

        if (passToken == null) {
            throw new InvalidTokenException("invalidToken");

        } else if (passToken.isExpired()) {
            throw new InvalidTokenException("tokenExpired");
        }

        User user = passToken.getUser();
        Collection<GrantedAuthority> authorities =
                (Collection<GrantedAuthority>) userDao.findByEmail(user.getEmail()).getAuthorities();
        //TODO sipmify
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return passToken;
    }
}
