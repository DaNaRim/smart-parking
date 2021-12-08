package com.explorers.smartparking.user.persistence.model;

public enum RoleName {
    USER,
    ADMIN,
    SUPER_ADMIN,
    GUARD;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
