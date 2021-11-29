package com.explorers.smartparking.user.service;

import com.explorers.smartparking.user.persistence.model.RoleName;

public interface RoleService {

    void addRoleByUserId(long userId, RoleName roleName);

    void removeRoleByUserId(long userId, RoleName roleName);
}
