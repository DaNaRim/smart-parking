package com.explorers.smartparking.user.error;

public class UserHasNotRoleException extends RuntimeException {

    public UserHasNotRoleException(String message) {
        super(message);
    }
}
