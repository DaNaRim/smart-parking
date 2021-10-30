package com.explorers.smartparking.web.controller;

import com.explorers.smartparking.persistence.model.User;
import com.explorers.smartparking.service.UserService;
import com.explorers.smartparking.service.event.OnRegistrationCompleteEvent;
import com.explorers.smartparking.web.dto.RegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;

@Controller
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageSource messages;

    @Autowired
    public RegistrationController(UserService userService,
                                  ApplicationEventPublisher eventPublisher,
                                  MessageSource messages) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.messages = messages;
    }

    @PostMapping("/registerUser")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    String registerUserAccount(@RequestBody @Valid RegistrationDto registrationDto,
                               HttpServletRequest request) {

        User user = userService.registerNewUserAccount(registrationDto);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request));
        return "success";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token,
                                      Locale locale,
                                      Model model) {

        userService.enableUser(token);

        model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
        return "redirect:/login.html?lang=" + locale.getLanguage();
    }
}
