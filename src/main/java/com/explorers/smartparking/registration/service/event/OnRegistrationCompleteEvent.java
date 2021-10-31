package com.explorers.smartparking.registration.service.event;

import com.explorers.smartparking.registration.persistence.model.User;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private HttpServletRequest request;

    public OnRegistrationCompleteEvent(User user, HttpServletRequest request) {
        super(user);
        this.user = user;
        this.request = request;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
