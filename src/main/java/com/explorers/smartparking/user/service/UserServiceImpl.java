package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.persistence.dao.UserDao;
import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.web.error.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public User findById(long id) {
        Optional<User> user = userDao.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException("No user with id: " + id);
        }
        return user.get();
    }

    @Override
    public double putMoney(long userId, int money) {
        User user = findById(userId);
        user.setBalance(user.getBalance() + money);
        return user.getBalance();
    }


}
