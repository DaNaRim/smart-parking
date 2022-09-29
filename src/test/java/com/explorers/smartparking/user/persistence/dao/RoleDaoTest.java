package com.explorers.smartparking.user.persistence.dao;

import com.explorers.smartparking.user.persistence.model.Role;
import com.explorers.smartparking.user.persistence.model.RoleName;
import com.explorers.smartparking.user.persistence.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class RoleDaoTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private RoleDao roleDao;

    @Test
    void getRolesByUserId() {
        User user = new User();
        user.setEmail("email");
        user.setFirstName("test");
        user.setLastName("test");
        user.setPassword("test");
        user.setRoles(Set.of(roleDao.findByRoleName(RoleName.USER), roleDao.findByRoleName(RoleName.ADMIN)));
        User user1 = entityManager.persist(user);

        Set<Role> roles = roleDao.getRolesByUserId(user1.getId());

        assertEquals(roles, user.getRoles());
    }

    @Test
    void addRoleById() {
        User user = new User();
        user.setEmail("email");
        user.setFirstName("test");
        user.setLastName("test");
        user.setPassword("test");
        user.setRoles(Set.of(roleDao.findByRoleName(RoleName.USER)));
        User user1 = entityManager.persist(user);

        roleDao.addRoleById(user1.getId(), RoleName.ADMIN.name());

        Set<Role> roles = roleDao.getRolesByUserId(user1.getId());

        assertEquals(roles, Set.of(roleDao.findByRoleName(RoleName.USER), roleDao.findByRoleName(RoleName.ADMIN)));
    }

    @Test
    void removeRoleById() {
        User user = new User();
        user.setEmail("email");
        user.setFirstName("test");
        user.setLastName("test");
        user.setPassword("test");
        user.setRoles(Set.of(roleDao.findByRoleName(RoleName.USER), roleDao.findByRoleName(RoleName.ADMIN)));
        User user1 = entityManager.persist(user);

        roleDao.removeRoleById(user1.getId(), RoleName.ADMIN.name());

        Set<Role> roles = roleDao.getRolesByUserId(user1.getId());

        assertEquals(roles, Set.of(roleDao.findByRoleName(RoleName.USER)));
    }
}
