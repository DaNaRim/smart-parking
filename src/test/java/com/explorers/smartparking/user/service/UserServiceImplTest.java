package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.error.InvalidBalanceException;
import com.explorers.smartparking.user.error.InvalidOldPasswordException;
import com.explorers.smartparking.user.error.UserNotFoundException;
import com.explorers.smartparking.user.persistence.dao.UserDao;
import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.web.dto.UpdatePasswordDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final UserDao userDao = mock(UserDao.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private final UserServiceImpl userService = new UserServiceImpl(userDao, passwordEncoder);

    @Test
    void findById() {
        long id = 1L;
        User user = new User();

        when(userDao.findById(id)).thenReturn(Optional.of(user));

        assertEquals(user, userService.findById(id));
    }

    @Test
    void findByInvalidId() {
        long id = 1L;

        when(userDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(id));
    }

    @Test
    void findByEmail() {
        String email = "email";
        User user = new User();

        when(userDao.findByEmail(email)).thenReturn(user);

        assertEquals(user, userService.findByEmail(email));
    }

    @Test
    void findByInvalidEmail() {
        String email = "email";

        when(userDao.findByEmail(email)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(email));
    }

    @Test
    void changeUserPassword() {
        UpdatePasswordDto passwordDto = new UpdatePasswordDto();
        passwordDto.setOldPassword("oldPassword");
        passwordDto.setNewPassword("newPassword");

        User user = new User();
        user.setId(1L);
        user.setPassword("oldPassword");

        when(userDao.getById(1L)).thenReturn(user);
        when(passwordEncoder.matches(passwordDto.getOldPassword(), userDao.getPasswordById(1L))).thenReturn(true);
        when(passwordEncoder.encode(passwordDto.getNewPassword())).thenReturn("newPassword");

        userService.changeUserPassword(1L, passwordDto);

        assertEquals(passwordDto.getNewPassword(), user.getPassword());
    }

    @Test
    void changeUserPasswordInvalidOld() {
        UpdatePasswordDto passwordDto = new UpdatePasswordDto();
        passwordDto.setOldPassword("oldPassword");
        passwordDto.setNewPassword("newPassword");

        User user = new User();
        user.setId(1L);
        user.setPassword("oldPassword");

        when(userDao.getById(1L)).thenReturn(user);
        when(passwordEncoder.matches(passwordDto.getOldPassword(), userDao.getPasswordById(1L))).thenReturn(false);

        assertThrows(InvalidOldPasswordException.class, () -> userService.changeUserPassword(1L, passwordDto));
    }

    @Test
    void putMoney() {
        long id = 1L;
        int money = 10;

        User user = new User();
        user.setId(id);
        user.setBalance(0.0);

        UserServiceImpl userServiceSpy = spy(this.userService);
        doReturn(user).when(userServiceSpy).findById(id);

        userServiceSpy.putMoney(id, money);

        assertEquals(money, user.getBalance());
    }

    @Test
    void putNegativeMoney() {
        long id = 1L;
        int money = -10;

        User user = new User();
        user.setId(id);
        user.setBalance(0.0);

        assertThrows(InvalidBalanceException.class, () -> userService.putMoney(id, money));
    }

    @Test
    void putZeroMoney() {
        long id = 1L;
        int money = 0;

        User user = new User();
        user.setId(id);
        user.setBalance(0.0);

        assertThrows(InvalidBalanceException.class, () -> userService.putMoney(id, money));
    }
}
