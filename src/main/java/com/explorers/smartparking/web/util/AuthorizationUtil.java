package com.explorers.smartparking.web.util;

import com.explorers.smartparking.persistence.model.User;
import com.explorers.smartparking.web.exception.UnauthorizedException;
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

}
