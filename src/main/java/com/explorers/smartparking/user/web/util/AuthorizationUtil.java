package com.explorers.smartparking.user.web.util;

import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.error.UnauthorizedException;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthorizationUtil {

    public static long getUserId() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user instanceof User user1) {
            return user1.getId();
        } else {
            throw new UnauthorizedException("Can`t get id because user unauthorized");
        }
    }

    public static String getUserEmail() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user instanceof User user1) {
            return user1.getEmail();
        } else {
            throw new UnauthorizedException("Can`t get email because user unauthorized");
        }
    }

}
