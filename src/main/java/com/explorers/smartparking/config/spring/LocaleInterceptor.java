package com.explorers.smartparking.config.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static com.explorers.smartparking.config.spring.MvcConfig.RESOURCES;
import static com.explorers.smartparking.config.spring.MvcConfig.SUPPORTED_LOCALES;

public class LocaleInterceptor implements HandlerInterceptor {

    private static final ArrayList<String> EXCLUSIONS = new ArrayList<>();

    static {
        EXCLUSIONS.addAll(RESOURCES.stream().map(resource -> "/" + resource).toList());
        EXCLUSIONS.add("/favicon.ico");
        EXCLUSIONS.add("/swagger-ui");
        EXCLUSIONS.add("/swagger-resources");
        EXCLUSIONS.add("/v2/api-docs");
    }

    private final Log logger = LogFactory.getLog(LocaleInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();

        if (EXCLUSIONS.stream().anyMatch(path::startsWith)
                || !request.getMethod().equals(HttpMethod.GET.toString())) {
            return true;
        }

        if (path.length() < 3) {
            redirect(response, "/" + LocaleContextHolder.getLocale().getLanguage());
            return false;
        } else {
            String lang = path.substring(1, 3);
            Locale locale = Locale.forLanguageTag(lang);

            if (!SUPPORTED_LOCALES.contains(locale)) {
                redirect(response, "/%s%s".formatted(LocaleContextHolder.getLocale().getLanguage(), path));
                return false;
            }
        }
        Locale requestLocale = Locale.forLanguageTag(path.substring(1, 3));

        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        Objects.requireNonNull(localeResolver).setLocale(request, response, requestLocale);

        return true;
    }

    private void redirect(HttpServletResponse response, String path) {
        try {
            response.sendRedirect(path);
        } catch (IOException e) {
            logger.error("Error redirecting to " + path, e);
            throw new RuntimeException("Error redirecting to " + path, e);
        }
    }
}
