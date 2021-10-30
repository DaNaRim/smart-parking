package com.explorers.smartparking.service;

import com.explorers.smartparking.persistence.dao.UserDao;
import com.explorers.smartparking.persistence.model.Token;
import com.explorers.smartparking.persistence.model.User;
import com.explorers.smartparking.web.dto.RegistrationDto;
import com.explorers.smartparking.web.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao,
                           TokenService tokenService,
                           PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerNewUserAccount(RegistrationDto registrationDto) {

        if (userDao.existsByEmail(registrationDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + registrationDto.getEmail());
        }

        User user = new User(
                registrationDto.getFirstName(),
                registrationDto.getLastName(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()));

        return userDao.save(user);
    }

    @Override
    public void enableUser(String token) {
        Token verificationToken = tokenService.validateVerificationToken(token);
        User user = verificationToken.getUser();
        user.setEnabled(true);
    }
}
