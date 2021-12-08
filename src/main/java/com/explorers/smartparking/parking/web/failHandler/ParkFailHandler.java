package com.explorers.smartparking.parking.web.failHandler;

import com.explorers.smartparking.parking.error.NoEnoughMoneyException;
import com.explorers.smartparking.parking.error.ParkingExistsException;
import com.explorers.smartparking.parking.error.ParkingPlaceBusyException;
import com.explorers.smartparking.parking.error.ParkingPlaceNotFoundException;
import com.explorers.smartparking.user.web.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ParkFailHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messages;

    @Autowired
    public ParkFailHandler(MessageSource messages) {
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

    @ExceptionHandler({NoEnoughMoneyException.class})
    public ResponseEntity<Object> handleNoMoney(RuntimeException e, WebRequest request) {
        logger.warn("HTTP 400: No enough money " + e.getMessage());

        GenericResponse responseBody = new GenericResponse(
                messages.getMessage("error.parking.noMoney", null, request.getLocale()), "NoEnoughMoney");

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ParkingExistsException.class})
    public ResponseEntity<Object> handleParkingExists(RuntimeException e, WebRequest request) {
        logger.warn("HTTP 400: Parking already exists " + e.getMessage());

        GenericResponse responseBody = new GenericResponse(
                messages.getMessage("error.parking.exists", null, request.getLocale()), "ParkingExists");

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ParkingPlaceBusyException.class})
    public ResponseEntity<Object> handleParkingPlaceBusy(RuntimeException e, WebRequest request) {
        logger.warn("HTTP 400: Parking place is busy " + e.getMessage());

        GenericResponse responseBody = new GenericResponse(
                messages.getMessage("error.parking.busy", null, request.getLocale()), "ParkingPlaceBusy");

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ParkingPlaceNotFoundException.class})
    public ResponseEntity<Object> handleParkingPlaceNotFound(RuntimeException e, WebRequest request) {
        logger.warn("HTTP 400: Parking not found " + e.getMessage());

        GenericResponse responseBody = new GenericResponse(
                messages.getMessage("error.parking.notFound", null, request.getLocale()), "ParkingPlaceNotFound");

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
