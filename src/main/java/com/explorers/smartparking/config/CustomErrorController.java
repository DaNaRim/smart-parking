package com.explorers.smartparking.config;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status == null) return "errors/error";

        HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(status.toString()));

        return switch (httpStatus) {
            case FORBIDDEN -> "errors/error-403";
            case NOT_FOUND -> "errors/error-404";
            case INTERNAL_SERVER_ERROR -> "errors/error-500";
            default -> "errors/error";
        };
    }
}
