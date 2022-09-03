package com.explorers.smartparking.user.web.controller;

import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.service.RegistrationService;
import com.explorers.smartparking.user.service.TokenEmailFacade;
import com.explorers.smartparking.user.service.TokenService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;
    private final TokenService tokenService;
    private final TokenEmailFacade tokenEmailFacade;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageSource messages;

    @Autowired
    public RegistrationController(RegistrationService registrationService,
                                  TokenService tokenService,
                                  TokenEmailFacade tokenEmailFacade,
                                  ApplicationEventPublisher eventPublisher,
                                  MessageSource messages) {
        this.registrationService = registrationService;
        this.tokenService = tokenService;
        this.tokenEmailFacade = tokenEmailFacade;
        this.eventPublisher = eventPublisher;
        this.messages = messages;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDto", new RegistrationDto());
        return "registration";
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUserAccount(@ModelAttribute @Valid RegistrationDto registrationDto,
                                      BindingResult result,
                                      Locale locale,
                                      Model model) {

        if (result.hasErrors()) return "registration";

        User user = registrationService.registerNewUserAccount(registrationDto);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));

        model.addAttribute("message", messages.getMessage("message.user.accountRegistered", null, locale));
        return "registration";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token,
                                      Locale locale,
                                      Model model) {

        registrationService.enableUser(token);
        model.addAttribute("message", messages.getMessage("message.user.accountVerified", null, locale));
        return "redirect:/login?lang=" + locale.getLanguage();
    }

    @GetMapping("/resendRegistrationToken")
    public String resendRegistrationToken(@RequestParam("email") String userEmail,
                                          Locale locale,
                                          Model model) {

        tokenEmailFacade.updateAndSendVerificationToken(userEmail);

        model.addAttribute("message", messages.getMessage("message.user.tokenResent", null, locale));
        return "redirect:/login?lang=" + locale.getLanguage();
    }

    @PostMapping("/sendPassResetToken")
    public @ResponseBody
    GenericResponse resetPassword(@RequestParam("email") String userEmail,
                                  Locale locale) {

        tokenEmailFacade.createAndSendPasswordResetToken(userEmail);
        return new GenericResponse(messages.getMessage("message.user.resetPassEmailSent", null, locale));
    }

    @GetMapping("/resetPasswordPage")
    public String showResetPasswordPage(@RequestParam("token") String token,
                                        Locale locale,
                                        Model model) {

        tokenService.validatePasswordResetToken(token);
        model.addAttribute("token", token);
        return "redirect:/updateForgotPassword?lang=" + locale.getLanguage();
    }

    @GetMapping("/updateForgotPassword")
    public String showUpdateForgotPasswordPage(Model model) {
        model.addAttribute("forgotPasswordDto", new ForgotPasswordDto());
        return "updateForgotPassword";
    }

    @RequestMapping(path = "/updateForgottenPassword", method = {RequestMethod.PUT, RequestMethod.POST})
    public String updateForgottenPassword(@ModelAttribute @Valid ForgotPasswordDto passwordDto,
                                          BindingResult result,
                                          Model model,
                                          Locale locale) {

        if (result.hasErrors()) return "updateForgotPassword";

        registrationService.changeForgottenPassword(passwordDto);
        model.addAttribute("message", messages.getMessage("message.user.updatePasswordSuc", null, locale));
        return "login";
    }
}
