package com.explorers.smartparking.user.web.error;

public class UserHasRoleException extends RuntimeException {

    public UserHasRoleException(String message) {
        super(message);
    }
}
