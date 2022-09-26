package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.error.InvalidTokenException;
import com.explorers.smartparking.user.persistence.dao.TokenDao;
import com.explorers.smartparking.user.persistence.dao.UserDao;
import com.explorers.smartparking.user.persistence.model.Token;
import com.explorers.smartparking.user.persistence.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    private final TokenDao tokenDao = mock(TokenDao.class);
    private final UserDao userDao = mock(UserDao.class);

    private final TokenServiceImpl tokenService = new TokenServiceImpl(tokenDao, userDao);

    @Test
    void createVerificationToken() {
        tokenService.createVerificationToken(new User());
        verify(tokenDao).save(any(Token.class));
    }

    @Test
    void updateVerificationToken() {
        User user = new User();
        user.setEnabled(false);

        when(userDao.findByEmail(any())).thenReturn(user);
        TokenServiceImpl tokenServiceSpy = spy(this.tokenService);

        tokenServiceSpy.updateVerificationToken(user.getEmail());
        verify(tokenServiceSpy).createVerificationToken(any());
    }

    @Test
    void updateVerificationTokenWithEnabledUser() {
        User user = new User();
        user.setEnabled(true);

        when(userDao.findByEmail(any())).thenReturn(user);

        assertThrows(InvalidTokenException.class, () -> tokenService.updateVerificationToken(user.getEmail()));
    }

    @Test
    void validateVerificationToken() {
        User user = new User();

        Token token = new Token();
        token.setToken("token");
        token.setUser(user);
        token.setExpiryDate(new Date(System.currentTimeMillis() + 10_000));

        when(tokenDao.findByToken(any())).thenReturn(token);

        assertEquals(token, tokenService.validateVerificationToken(token.getToken()));
    }

    @Test
    void validateVerificationTokenInvalid() {
        when(tokenDao.findByToken(any())).thenReturn(null);
        assertThrows(InvalidTokenException.class, () -> tokenService.validateVerificationToken("token"));
    }

    @Test
    void validateVerificationTokenExpired() {
        Token token = new Token();
        token.setToken("token");
        token.setExpiryDate(new Date(System.currentTimeMillis() - 10_000));

        when(tokenDao.findByToken(any())).thenReturn(token);
        assertThrows(InvalidTokenException.class, () -> tokenService.validateVerificationToken(token.getToken()));
    }

    @Test
    void validateVerificationTokenUserEnabled() {
        User user = new User();
        user.setEnabled(true);

        Token token = new Token();
        token.setToken("token");
        token.setUser(user);
        token.setExpiryDate(new Date(System.currentTimeMillis() + 10_000));

        when(tokenDao.findByToken(any())).thenReturn(token);
        assertThrows(InvalidTokenException.class, () -> tokenService.validateVerificationToken(token.getToken()));
    }

    @Test
    void getVerificationToken() {
        tokenService.getVerificationToken("token");
        verify(tokenDao).findByToken("token");
    }

    @Test
    void createPasswordResetToken() {
        tokenService.createPasswordResetToken(new User());
        verify(tokenDao).save(any(Token.class));
    }

    @Test
    void validatePasswordResetToken() {
        User user = new User();

        Token token = new Token();
        token.setToken("token");
        token.setUser(user);
        token.setExpiryDate(new Date(System.currentTimeMillis() + 10_000));

        when(tokenDao.findByToken(any())).thenReturn(token);
        when(userDao.findByEmail(any())).thenReturn(user);

        assertEquals(token, tokenService.validatePasswordResetToken(token.getToken()));
    }

    @Test
    void validatePasswordResetTokenInvalid() {
        when(tokenDao.findByToken(any())).thenReturn(null);
        assertThrows(InvalidTokenException.class, () -> tokenService.validatePasswordResetToken("token"));
    }

    @Test
    void validatePasswordResetTokenExpired() {
        Token token = new Token();
        token.setToken("token");
        token.setExpiryDate(new Date(System.currentTimeMillis() - 10_000));

        when(tokenDao.findByToken(any())).thenReturn(token);
        assertThrows(InvalidTokenException.class, () -> tokenService.validatePasswordResetToken(token.getToken()));
    }
}
