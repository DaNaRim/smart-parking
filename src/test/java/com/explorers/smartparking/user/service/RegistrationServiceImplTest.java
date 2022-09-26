package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.error.UserAlreadyExistException;
import com.explorers.smartparking.user.persistence.dao.RoleDao;
import com.explorers.smartparking.user.persistence.dao.UserDao;
import com.explorers.smartparking.user.persistence.model.RoleName;
import com.explorers.smartparking.user.persistence.model.Token;
import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.web.dto.ForgotPasswordDto;
import com.explorers.smartparking.user.web.dto.RegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    private final UserDao userDao = mock(UserDao.class);
    private final RoleDao roleDao = mock(RoleDao.class);
    private final TokenService tokenService = mock(TokenService.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private final RegistrationServiceImpl registrationService = new RegistrationServiceImpl(
            userDao,
            roleDao,
            tokenService,
            passwordEncoder
    );

    @BeforeEach
    void setUp() {
        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void registerNewUserAccountWithExistEmail() {
        when(userDao.existsByEmail(anyString())).thenReturn(true);

        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setEmail("existEmail");
        registrationDto.setFirstName("firstName");
        registrationDto.setLastName("lastName");
        registrationDto.setPassword("password");
        registrationDto.setMatchingPassword("password");

        assertThrows(UserAlreadyExistException.class,
                () -> registrationService.registerNewUserAccount(registrationDto));

        verify(userDao).existsByEmail(registrationDto.getEmail());
    }

    @Test
    void registerNewUserAccount() {
        when(userDao.existsByEmail(anyString())).thenReturn(false);
        when(userDao.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setEmail("email");
        registrationDto.setFirstName("firstName");
        registrationDto.setLastName("lastName");
        registrationDto.setPassword("password");
        registrationDto.setMatchingPassword("password");

        User registeredUser = registrationService.registerNewUserAccount(registrationDto);

        assertEquals(registrationDto.getFirstName(), registeredUser.getFirstName());
        assertEquals(registrationDto.getLastName(), registeredUser.getLastName());
        assertEquals(registrationDto.getEmail(), registeredUser.getEmail());
        assertEquals(registrationDto.getPassword(), registeredUser.getPassword());

        verify(userDao).existsByEmail(registrationDto.getEmail());
        verify(userDao).save(any(User.class));
        verify(passwordEncoder).encode(registrationDto.getPassword());
        verify(roleDao).findByRoleName(RoleName.USER);
    }

    @Test
    void enableUser() {
        User user = new User();
        user.setEnabled(false);

        Token token = new Token();
        token.setToken("token");
        token.setUser(user);
        when(tokenService.validateVerificationToken(token.getToken())).thenReturn(token);

        registrationService.enableUser(token.getToken());
        assertTrue(user.isEnabled());

        verify(tokenService).validateVerificationToken(token.getToken());
    }

    @Test
    void changeForgottenPassword() {
        User user = new User();
        user.setPassword("oldPassword");

        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto("token");
        forgotPasswordDto.setNewPassword("newPassword");

        Token token = new Token();
        token.setToken("token");
        token.setUser(user);
        when(tokenService.validatePasswordResetToken(token.getToken())).thenReturn(token);

        registrationService.changeForgottenPassword(forgotPasswordDto);
        assertEquals(forgotPasswordDto.getNewPassword(), user.getPassword());

        verify(tokenService).validatePasswordResetToken(token.getToken());
        verify(passwordEncoder).encode(forgotPasswordDto.getNewPassword());
    }
}
