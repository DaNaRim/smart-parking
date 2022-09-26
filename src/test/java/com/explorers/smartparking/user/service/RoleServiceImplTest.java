package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.error.UserHasNotRoleException;
import com.explorers.smartparking.user.error.UserHasRoleException;
import com.explorers.smartparking.user.error.UserNotFoundException;
import com.explorers.smartparking.user.persistence.dao.RoleDao;
import com.explorers.smartparking.user.persistence.model.Role;
import com.explorers.smartparking.user.persistence.model.RoleName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    private final RoleDao roleDao = mock(RoleDao.class);

    private final RoleServiceImpl roleService = new RoleServiceImpl(roleDao);

    @Test
    void addRoleByUserId() {
        long userId = 1L;
        Set<Role> roles = Set.of(new Role(RoleName.USER));

        when(roleDao.getRolesByUserId(userId)).thenReturn(roles);

        roleService.addRoleByUserId(userId, RoleName.ADMIN);

        verify(roleDao).getRolesByUserId(userId);
        verify(roleDao).addRoleById(userId, RoleName.ADMIN.name());
    }

    @Test
    void addRoleByFakeUserId() {
        long userId = 1L;
        Set<Role> roles = Collections.emptySet();

        when(roleDao.getRolesByUserId(userId)).thenReturn(roles);

        assertThrows(UserNotFoundException.class, () -> roleService.addRoleByUserId(userId, RoleName.ADMIN));

        verify(roleDao).getRolesByUserId(userId);
    }

    @Test
    void addExistsRoleByUserId() {
        long userId = 1L;
        Set<Role> roles = Set.of(new Role(RoleName.ADMIN));

        when(roleDao.getRolesByUserId(userId)).thenReturn(roles);

        assertThrows(UserHasRoleException.class, () -> roleService.addRoleByUserId(userId, RoleName.ADMIN));

        verify(roleDao).getRolesByUserId(userId);
    }

    @Test
    void removeRoleByUserId() {
        long userId = 1L;
        Set<Role> roles = Set.of(new Role(RoleName.ADMIN));

        when(roleDao.getRolesByUserId(userId)).thenReturn(roles);

        roleService.removeRoleByUserId(userId, RoleName.ADMIN);

        verify(roleDao).getRolesByUserId(userId);
        verify(roleDao).removeRoleById(userId, RoleName.ADMIN.name());
    }

    @Test
    void removeRoleByFakeUserId() {
        long userId = 1L;
        Set<Role> roles = Collections.emptySet();

        when(roleDao.getRolesByUserId(userId)).thenReturn(roles);

        assertThrows(UserNotFoundException.class, () -> roleService.removeRoleByUserId(userId, RoleName.ADMIN));

        verify(roleDao).getRolesByUserId(userId);
    }

    @Test
    void removeExistRoleByUserId() {
        long userId = 1L;
        Set<Role> roles = Set.of(new Role(RoleName.USER));

        when(roleDao.getRolesByUserId(userId)).thenReturn(roles);

        assertThrows(UserHasNotRoleException.class, () -> roleService.removeRoleByUserId(userId, RoleName.ADMIN));

        verify(roleDao).getRolesByUserId(userId);
    }
}
