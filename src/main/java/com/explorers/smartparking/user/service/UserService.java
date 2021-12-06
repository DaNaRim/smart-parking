package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.web.dto.UpdatePasswordDto;

public interface UserService {

    User findByEmail(String email);
    void changeUserPassword(long userId, UpdatePasswordDto passwordDto);

    double putMoney(long userId, int money);
}
