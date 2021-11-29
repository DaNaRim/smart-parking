package com.explorers.smartparking.user.web.util;

import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.web.error.UnauthorizedException;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthorizationUtil {

    public static long getUserId() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user instanceof User) {
            User user1 = (User) user;
            return user1.getId();
        } else {
            throw new UnauthorizedException("Can`t get id because user unauthorized");
        }
    }

}
