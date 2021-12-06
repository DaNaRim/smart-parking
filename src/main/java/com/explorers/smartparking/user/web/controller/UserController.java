package com.explorers.smartparking.user.web.controller;

import com.explorers.smartparking.user.service.UserService;
import com.explorers.smartparking.user.web.dto.UpdatePasswordDto;
import com.explorers.smartparking.user.web.util.AuthorizationUtil;
import com.explorers.smartparking.user.web.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final MessageSource messages;

    @Autowired
    public UserController(UserService userService, MessageSource messages) {
        this.userService = userService;
        this.messages = messages;
    }

    @PutMapping("/user/updatePassword")
    public @ResponseBody
    GenericResponse updatePassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto,
                                   Locale locale) {

        userService.changeUserPassword(AuthorizationUtil.getUserId(), updatePasswordDto);
        return new GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
    }
}
