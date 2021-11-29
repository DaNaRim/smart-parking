package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.persistence.dao.RoleDao;
import com.explorers.smartparking.user.persistence.dao.UserDao;
import com.explorers.smartparking.user.persistence.model.RoleName;
import com.explorers.smartparking.user.persistence.model.Token;
import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.web.dto.ForgotPasswordDto;
import com.explorers.smartparking.user.web.dto.RegistrationDto;
import com.explorers.smartparking.user.web.dto.UpdatePasswordDto;
import com.explorers.smartparking.user.web.error.InvalidOldPasswordException;
import com.explorers.smartparking.user.web.error.UserAlreadyExistException;
import com.explorers.smartparking.user.web.error.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerNewUserAccount(RegistrationDto registrationDto) {

        if (userDao.existsByEmail(registrationDto.getEmail())) {
            throw new UserAlreadyExistException("email is busy: " + registrationDto.getEmail());
        }

        User user = new User(
                registrationDto.getFirstName(),
                registrationDto.getLastName(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()),
                Collections.singleton(roleDao.findByRoleName(RoleName.USER)));

        return userDao.save(user);
    }

    @Override
    public void enableUser(String token) {
        Token verificationToken = tokenService.validateVerificationToken(token);
        User user = verificationToken.getUser();
        user.setEnabled(true);
    }

    @Override
    public User findById(long id) {
        Optional<User> user = userDao.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException("No user with id: " + id);
        }
        return user.get();
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public void changeUserPassword(long userId, UpdatePasswordDto passwordDto) {
        if (!passwordEncoder.matches(passwordDto.getOldPassword(), userDao.getPasswordById(userId))) {
            throw new InvalidOldPasswordException("Invalid old password");
        }
        User user = userDao.getById(userId);
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
    }

    @Override
    public void changeForgottenPassword(ForgotPasswordDto passwordDto) {
        Token token = tokenService.validatePasswordResetToken(passwordDto.getToken());
        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
    }
}
