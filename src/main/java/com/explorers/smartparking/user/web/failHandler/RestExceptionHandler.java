package com.explorers.smartparking.user.web.failHandler;

import com.explorers.smartparking.user.error.*;
import com.explorers.smartparking.user.web.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messages;

    @Autowired
    public RestExceptionHandler(MessageSource messages) {
        this.messages = messages;
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException e,
                                                         HttpHeaders headers,
                                                         HttpStatus status,
                                                         WebRequest request) {
        BindingResult result = e.getBindingResult();
        GenericResponse bodyOfResponse = new GenericResponse(result.getFieldErrors(), result.getGlobalErrors());
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<Object> handleToken(RuntimeException e, Model model, WebRequest request) {
        logger.warn("HTTP 400: InvalidToken " + e.getMessage());

//        model.addAttribute() //TODO return bad token page
//        return "redirect:/badToken";

        GenericResponse responseBody = new GenericResponse(
                messages.getMessage("error.token." + e.getMessage(), null, request.getLocale()), "InvalidToken");

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({UserHasRoleException.class})
    public ResponseEntity<Object> handleHasRole(RuntimeException e, WebRequest request) {
        logger.warn("HTTP 400: User already has this role " + e.getMessage());

        GenericResponse responseBody = new GenericResponse(
                messages.getMessage("error.user.alreadyHasRole", null, request.getLocale()), "UserHasRole");

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({UserHasNotRoleException.class})
    public ResponseEntity<Object> handleHasNotRole(RuntimeException e, WebRequest request) {
        logger.warn("HTTP 400: User has not this role " + e.getMessage());

        GenericResponse responseBody = new GenericResponse(
                messages.getMessage("error.user.HasNotRole", null, request.getLocale()), "UserHasNotRole");

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<Object> handleUnauthorized(RuntimeException e, WebRequest request) {
        logger.warn("HTTP 401: Unauthorized " + e.getMessage());

        GenericResponse responseBody = new GenericResponse(
                messages.getMessage("error.user.unauthorized", null, request.getLocale()), "Unauthorized");

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFound(RuntimeException e, WebRequest request) {
        logger.warn("HTTP 404: UserNotFound " + e.getMessage());
        GenericResponse responseBody = new GenericResponse(
                messages.getMessage("error.user.notFound", null, request.getLocale()), "UserNotFound");

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({UserAlreadyExistException.class})
    public ResponseEntity<Object> handleUserAlreadyExist(RuntimeException e, WebRequest request) {
        logger.warn("HTTP 409: UserAlreadyExist " + e.getMessage());
        GenericResponse bodyOfResponse = new GenericResponse(
                messages.getMessage("error.user.busyEmail", null, request.getLocale()), "UserAlreadyExist");

        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({MailAuthenticationException.class})
    public ResponseEntity<Object> handleMail(RuntimeException e, WebRequest request) {
        logger.error("HTTP 500: MailAuthenticationException " + e.getMessage());
        GenericResponse responseBody = new GenericResponse(
                messages.getMessage("error.email.config", null, request.getLocale()), "MailError");

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleInternal(RuntimeException e, WebRequest request) {
        logger.error("HTTP 500: Internal server error ", e);
        GenericResponse responseBody = new GenericResponse(
                messages.getMessage("error.internalServer", null, request.getLocale()), "InternalServerError");

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
