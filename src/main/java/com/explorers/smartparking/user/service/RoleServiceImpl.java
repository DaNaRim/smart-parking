package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.persistence.dao.RoleDao;
import com.explorers.smartparking.user.persistence.model.Role;
import com.explorers.smartparking.user.persistence.model.RoleName;
import com.explorers.smartparking.user.error.UserHasNotRoleException;
import com.explorers.smartparking.user.error.UserHasRoleException;
import com.explorers.smartparking.user.error.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public void addRoleByUserId(long userId, RoleName roleName) {
        Set<Role> roles = roleDao.getRolesByUserId(userId);

        if (roles.isEmpty()) {
            throw new UserNotFoundException("No user with id: " + userId);
        } else if (roles.contains(new Role(roleName))) {
            throw new UserHasRoleException("User already has this role");
        }
        roleDao.addRoleById(userId, roleName.name());
    }

    @Override
    public void removeRoleByUserId(long userId, RoleName roleName) {
        Set<Role> roles = roleDao.getRolesByUserId(userId);

        if (roles.isEmpty()) {
            throw new UserNotFoundException("No user with id: " + userId);
        } else if (!roles.contains(new Role(roleName))) {
            throw new UserHasNotRoleException("User has not this role");
        }
        roleDao.removeRoleById(userId, roleName.name());
    }
}
