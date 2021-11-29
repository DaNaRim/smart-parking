package com.explorers.smartparking.user.web.error;

public class UserHasNotRoleException extends RuntimeException {

    public UserHasNotRoleException(String message) {
        super(message);
    }
}
