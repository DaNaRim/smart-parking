package com.explorers.smartparking.user.web.controller;

import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.service.TokenEmailFacade;
import com.explorers.smartparking.user.service.TokenService;
import com.explorers.smartparking.user.service.UserService;
import com.explorers.smartparking.user.service.event.OnRegistrationCompleteEvent;
import com.explorers.smartparking.user.web.dto.ForgotPasswordDto;
import com.explorers.smartparking.user.web.dto.RegistrationDto;
import com.explorers.smartparking.user.web.util.GenericResponse;
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
    private final TokenService tokenService;
    private final TokenEmailFacade tokenEmailFacade;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageSource messages;

    @Autowired
    public RegistrationController(UserService userService,
                                  TokenService tokenService,
                                  TokenEmailFacade tokenEmailFacade,
                                  ApplicationEventPublisher eventPublisher,
                                  MessageSource messages) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.tokenEmailFacade = tokenEmailFacade;
        this.eventPublisher = eventPublisher;
        this.messages = messages;
    }

    @PostMapping("/registerUser")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    GenericResponse registerUserAccount(@RequestBody @Valid RegistrationDto registrationDto,
                                        HttpServletRequest request) {

        User user = userService.registerNewUserAccount(registrationDto);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request));
        return new GenericResponse(messages.getMessage("message.accountRegistered", null, request.getLocale()));
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token,
                                      Locale locale,
                                      Model model) {

        userService.enableUser(token);
        model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
        return "redirect:/login?lang=" + locale.getLanguage();
    }

    @GetMapping("/resendRegistrationToken")
    public String resendRegistrationToken(@RequestParam("email") String userEmail,
                                          HttpServletRequest request,
                                          Locale locale,
                                          Model model) {

        tokenEmailFacade.updateAndSendVerificationToken(userEmail, request);

        model.addAttribute("message", messages.getMessage("message.tokenResent", null, locale));
        return "redirect:/login?lang=" + locale.getLanguage();
    }

    @PostMapping("/sendPassResetToken")
    public @ResponseBody
    GenericResponse resetPassword(@RequestParam("email") String userEmail,
                                  HttpServletRequest request) {

        tokenEmailFacade.createAndSendPasswordResetToken(userEmail, request);
        return new GenericResponse(
                messages.getMessage("message.resetPassEmailSent", null, request.getLocale()));
    }

    @GetMapping("/resetPasswordPage")
    public String showResetPasswordPage(@RequestParam("token") String token,
                                        Locale locale,
                                        Model model) {

        tokenService.validatePasswordResetToken(token);
        model.addAttribute("token", token);
        return "redirect:/updateFogotPassword?lang=" + locale.getLanguage();
    }

    @PutMapping("/updateForgottenPassword")
    public String updateForgottenPassword(@RequestBody @Valid ForgotPasswordDto passwordDto, //FIXME
                                          Model model,
                                          Locale locale) {

        userService.changeForgottenPassword(passwordDto);
        model.addAttribute("message", messages.getMessage("message.updatePasswordSuc", null, locale));
        return "login";
    }
}
