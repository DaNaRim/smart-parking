package com.explorers.smartparking.user.web.failHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String SESSION_ATTR_AUTH_ERROR = "AUTH_ERROR";

    private final MessageSource messages;

    @Autowired
    public AuthenticationFailureHandler(MessageSource messages) {
        this.messages = messages;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        Locale locale = LocaleContextHolder.getLocale();

        setDefaultFailureUrl("/%s/login?error=true".formatted(locale.getLanguage()));
        super.onAuthenticationFailure(request, response, exception);

        String errorMessage = exception.getMessage();

        String resultMessage = switch (errorMessage) {
            case "Bad credentials" -> messages.getMessage("auth.badCredentials", null, locale);
            case "User is disabled" -> messages.getMessage("auth.disabled", null, locale);
            case "User account has expired" -> messages.getMessage("auth.expired", null, locale);
            case "blocked" -> messages.getMessage("auth.blocked", null, locale);
            default -> {
                logger.warn("Unknown login error: " + errorMessage);
                yield messages.getMessage("auth.unexpected", null, locale);
            }
        };
        request.getSession().setAttribute(SESSION_ATTR_AUTH_ERROR, resultMessage);
    }
}
