package com.explorers.smartparking.config.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String SESSION_ATTR_AUTH_ERROR = "AUTH_ERROR";

    private final MessageSource messages;
    private final LocaleResolver localeResolver;

    @Autowired
    public AuthenticationFailureHandler(MessageSource messages, LocaleResolver localeResolver) {
        this.messages = messages;
        this.localeResolver = localeResolver;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        Locale locale = localeResolver.resolveLocale(request);

        setDefaultFailureUrl("/%s/login?error=true".formatted(locale.getLanguage()));
        super.onAuthenticationFailure(request, response, exception);

        AuthError authError = Arrays.stream(AuthError.values())
                .filter(e -> e.getErrorClassName().equals(exception.getClass().getSimpleName()))
                .findFirst()
                .orElse(AuthError.UNEXPECTED);

        String resultMessage = switch (authError) {
            case USERNAME_NOT_FOUND_EXCEPTION -> messages.getMessage("auth.notFound", null, locale);
            case BAD_CREDENTIALS_EXCEPTION -> messages.getMessage("auth.badCredentials", null, locale);
            case DISABLED_EXCEPTION -> messages.getMessage("auth.disabled", null, locale);
            case ACCOUNT_LOCKED_EXCEPTION -> messages.getMessage("auth.blocked", null, locale);
            case ACCOUNT_EXPIRED_EXCEPTION -> messages.getMessage("auth.expired", null, locale);
            default -> {
                logger.error("Unexpected authentication error " + exception.getMessage(), exception);
                yield messages.getMessage("auth.unexpected", null, locale);
            }
        };
        request.getSession().setAttribute(SESSION_ATTR_AUTH_ERROR, resultMessage);
    }

    private enum AuthError {
        USERNAME_NOT_FOUND_EXCEPTION(UsernameNotFoundException.class.getSimpleName()),
        BAD_CREDENTIALS_EXCEPTION(BadCredentialsException.class.getSimpleName()),
        DISABLED_EXCEPTION(DisabledException.class.getSimpleName()),
        ACCOUNT_LOCKED_EXCEPTION(LockedException.class.getSimpleName()),
        ACCOUNT_EXPIRED_EXCEPTION(AccountExpiredException.class.getSimpleName()),
        UNEXPECTED("UNEXPECTED");

        private final String errorClassName;

        AuthError(String errorClassName) {
            this.errorClassName = errorClassName;
        }

        public String getErrorClassName() {
            return errorClassName;
        }
    }
}
