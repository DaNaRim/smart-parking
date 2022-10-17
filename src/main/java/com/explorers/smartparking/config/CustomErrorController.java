package com.explorers.smartparking.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    private final Log logger = LogFactory.getLog(CustomErrorController.class);

    @RequestMapping({"/error", "/{lang}/error"})
    public String handleError(final HttpServletRequest request) {
        final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        final Object path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        final Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        final Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        if (status == null) {
//            logger.error("null status on request: " + path + " message: " + message + " exception: " + exception);
            return "errors/error";
        }

        final HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(status.toString()));

        return switch (httpStatus) {
            case FORBIDDEN -> {
                logger.debug("403 status on request: " + path);
                yield "errors/error-403";
            }
            case NOT_FOUND -> {
                logger.debug("404 status on request: " + path);
                yield "errors/error-404";
            }
            case INTERNAL_SERVER_ERROR -> {
                logger.error("500 status on request: %s message: %s exception: %s"
                        .formatted(path, message, exception));
                yield "errors/error-500";
            }
            default -> {
                Object code = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
                logger.warn("unmapped status (%s) on request: %s message: %s exception: %s"
                        .formatted(code, path, message, exception));
                yield "errors/error";
            }
        };
    }
}
