package com.explorers.smartparking.user.web.controller;

import com.explorers.smartparking.user.service.UserService;
import com.explorers.smartparking.user.web.dto.UpdatePasswordDto;
import com.explorers.smartparking.user.web.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/updatePassword")
    public String showUpdatePasswordPage(Model model) {
        model.addAttribute("updatePasswordDto", new UpdatePasswordDto());
        return "updatePassword";
    }

    @RequestMapping(path = "/updatePassword", method = {RequestMethod.PUT, RequestMethod.POST})
    public String updatePassword(@ModelAttribute @Valid UpdatePasswordDto updatePasswordDto,
                                 BindingResult result,
                                 Model model,
                                 Locale locale) {

        if (result.hasErrors()) return "updatePassword"; //TODO: check old password

        userService.changeUserPassword(AuthorizationUtil.getUserId(), updatePasswordDto);

        model.addAttribute("message", messages.getMessage("message.user.updatePasswordSuc", null, locale));
        return "redirect:/login";
    }

    @PutMapping("/putMoney")
    public @ResponseBody
    double putMoney(@RequestParam int money) {
        return userService.putMoney(AuthorizationUtil.getUserId(), money); //TODO message
    }
}
