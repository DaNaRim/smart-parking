package com.explorers.smartparking.user.web.controller;

import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.service.RegistrationService;
import com.explorers.smartparking.user.service.TokenEmailFacade;
import com.explorers.smartparking.user.service.TokenService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;

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
                                      HttpServletRequest request,
                                      Model model) {

        if (result.hasErrors()) return "registration";

//        User user = registrationService.registerNewUserAccount(registrationDto);
//        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request));

        model.addAttribute(
                "message",
                messages.getMessage("message.user.accountRegistered", null, request.getLocale())
        );
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
                                          HttpServletRequest request,
                                          Locale locale,
                                          Model model) {

        tokenEmailFacade.updateAndSendVerificationToken(userEmail, request);

        model.addAttribute("message", messages.getMessage("message.user.tokenResent", null, locale));
        return "redirect:/login?lang=" + locale.getLanguage();
    }

    @PostMapping("/sendPassResetToken")
    public @ResponseBody
    GenericResponse resetPassword(@RequestParam("email") String userEmail,
                                  HttpServletRequest request) {

        tokenEmailFacade.createAndSendPasswordResetToken(userEmail, request);
        return new GenericResponse(
                messages.getMessage("message.user.resetPassEmailSent", null, request.getLocale()));
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
    public String updateForgottenPassword(@RequestBody @Valid ForgotPasswordDto passwordDto,
                                          Model model,
                                          Locale locale) {

        registrationService.changeForgottenPassword(passwordDto);
        model.addAttribute("message", messages.getMessage("message.user.updatePasswordSuc", null, locale));
        return "login";
    }
}
