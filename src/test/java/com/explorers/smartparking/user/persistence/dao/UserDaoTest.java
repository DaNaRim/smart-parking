package com.explorers.smartparking.user.persistence.dao;

import com.explorers.smartparking.user.persistence.model.RoleName;
import com.explorers.smartparking.user.persistence.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserDaoTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    @Test
    void getPasswordById() {
        User user = new User();
        user.setEmail("email");
        user.setFirstName("test");
        user.setLastName("test");
        user.setPassword("test");
        user.setRoles(Collections.singleton(roleDao.findByRoleName(RoleName.USER)));
        User user1 = entityManager.persist(user);

        String password = userDao.getPasswordById(user1.getId());
        assertEquals(password, user.getPassword());
    }
}
