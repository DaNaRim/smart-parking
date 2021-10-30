package com.explorers.smartparking.service;

import com.explorers.smartparking.persistence.dao.TokenDao;
import com.explorers.smartparking.persistence.model.Token;
import com.explorers.smartparking.persistence.model.TokenType;
import com.explorers.smartparking.persistence.model.User;
import com.explorers.smartparking.web.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    private final TokenDao tokenDao;

    @Autowired
    public TokenServiceImpl(TokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

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

}
