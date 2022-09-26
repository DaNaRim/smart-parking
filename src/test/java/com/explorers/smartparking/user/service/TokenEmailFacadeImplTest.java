package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.persistence.model.Token;
import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.web.util.MailUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenEmailFacadeImplTest {

    private final UserService userService = mock(UserService.class);
    private final TokenService tokenService = mock(TokenService.class);
    private final MailUtil mailUtil = mock(MailUtil.class);

    private final TokenEmailFacadeImpl tokenEmailFacade = new TokenEmailFacadeImpl(userService, tokenService, mailUtil);

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Token token = new Token();
        token.setToken("token");
        when(tokenService.createVerificationToken(any())).thenReturn(token);
        when(tokenService.updateVerificationToken(any())).thenReturn(token);
        when(tokenService.createPasswordResetToken(any())).thenReturn(token);
    }

    @Test
    void createAndSendVerificationToken() {
        User user = new User();
        tokenEmailFacade.createAndSendVerificationToken(user);

        verify(tokenService).createVerificationToken(user);
        verify(mailUtil).sendVerificationTokenEmail(any(), any(), any(), any());
    }

    @Test
    void updateAndSendVerificationToken() {
        tokenEmailFacade.updateAndSendVerificationToken("email");

        verify(tokenService).updateVerificationToken("email");
        verify(mailUtil).sendVerificationTokenEmail(any(), any(), any(), any());
    }

    @Test
    void createAndSendPasswordResetToken() {
        when(userService.findByEmail(any())).thenReturn(new User());
        tokenEmailFacade.createAndSendPasswordResetToken("email");

        verify(tokenService).createPasswordResetToken(any());
        verify(mailUtil).sendResetPasswordTokenEmail(any(), any(), any(), any());
    }
}
