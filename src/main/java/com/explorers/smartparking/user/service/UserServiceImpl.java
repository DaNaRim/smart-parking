package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.persistence.dao.UserDao;
import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.web.dto.UpdatePasswordDto;
import com.explorers.smartparking.user.web.error.InvalidOldPasswordException;
import com.explorers.smartparking.user.web.error.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public User findById(long id) {
        Optional<User> user = userDao.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException("No user with id: " + id);
        }
        return user.get();
    }

    @Override
    public User findByEmail(String email) {
        User user = userDao.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("No user with email: " + email);
        }
        return user;
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
    public double putMoney(long userId, int money) {
        User user = findById(userId);
        user.setBalance(user.getBalance() + money);
        return user.getBalance();
    }


}
