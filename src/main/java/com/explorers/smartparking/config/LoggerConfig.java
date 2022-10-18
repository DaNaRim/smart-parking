package com.explorers.smartparking.config;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.io.FileInputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Properties;

import static org.apache.logging.log4j.core.appender.ConsoleAppender.Target.SYSTEM_OUT;

@Plugin(name = "CustomConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class LoggerConfig extends ConfigurationFactory {

    private static final String LOGS_FOLDER = "./logs/";

    private static final String CONSOLE_APPENDER = "CONSOLE";
    private static final String CONSOLE_LOG_PATTERN
            = "%d %highlight{%-5p} [%15.15thread] %style{%40.40logger{1}}{blue} : %m %throwable{short}%n";
    private static final Level CONSOLE_LEVEL = Level.INFO;

    private static final String ROLLING_FILE_APPENDER = "ROLLING_FILE";
    private static final String ROLLING_FILE_NAME = "rolling.log";
    private static final String ROLLING_FILE_FOLDER = LOGS_FOLDER + "rolling/";
    private static final String ROLLING_FILE_FOLDER_PATTERN = ROLLING_FILE_FOLDER + "%d{yyyy-MM-dd}/";
    private static final String ROLLING_FILE_NAME_PATTERN = "rolling-%d{yyyy-MM-dd}-%i.log.gz";
    private static final String ROLLING_FILE_INTERVAL = "1";
    private static final String ROLLING_FILE_LOG_PATTERN
            = "%d %highlight{%-5p} [%20.20thread] %style{%40.40logger{3.}}{blue} : %m %n%throwable";
    private static final String ROLLING_FILE_MAX_SIZE = "100MB";
    private static final String ROLLING_FILE_MAX_COUNT = "10";
    private static final Level ROLLING_FILE_LEVEL = Level.DEBUG;

    private static final String FILE_APPENDER = "FILE";
    private static final String FILE_NAME = "smart-parking.log";
    private static final String FILE_LOG_PATTERN
            = "%d %highlight{%-5p} [%15.15thread] %style{%40.40logger{1}}{blue} : %m %n%throwable{5}";
    private static final Level FILE_LEVEL = Level.INFO;

    private static final String SMTP_APPENDER = "SMTP";
    private static final String SMTP_SUBJECT = "Smart Parking Error";
    private static final String SMTP_TO = "rangar.danarim@gmail.com";
    private static final String SMTP_LOG_PATTERN = "%d%n %m%n %throwable";
    private static final Level SMTP_LEVEL = Level.ERROR;

    private static Configuration createConfiguration(final String name,
                                                     final ConfigurationBuilder<BuiltConfiguration> builder) {

        builder.setConfigurationName(name);
//        builder.setMonitorInterval("5");

        builder.add(getConsoleAppender(builder));
        builder.add(getRollingFileAppender(builder));
        builder.add(getFileAppender(builder));
        builder.add(getSmtpAppender(builder));

        System.setProperty("mail.smtp.starttls.enable", "true");

        RootLoggerComponentBuilder rootLogger = builder.newAsyncRootLogger(Level.ALL)
                .add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL)
                        .addAttribute("level", Level.ALL)
                )
                .add(builder.newAppenderRef(CONSOLE_APPENDER).addAttribute("level", CONSOLE_LEVEL))
                .add(builder.newAppenderRef(FILE_APPENDER).addAttribute("level", FILE_LEVEL))
                .add(builder.newAppenderRef(ROLLING_FILE_APPENDER).addAttribute("level", ROLLING_FILE_LEVEL));

        LoggerComponentBuilder asyncLogger = builder.newAsyncLogger("com", Level.ALL)
                .add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL)
                        .addAttribute("level", Level.ALL)
                )
                .add(builder.newAppenderRef(SMTP_APPENDER).addAttribute("level", SMTP_LEVEL));

        builder.add(rootLogger);
        builder.add(asyncLogger);

        return builder.build();
    }

    private static AppenderComponentBuilder getConsoleAppender(ConfigurationBuilder<BuiltConfiguration> builder) {
        return builder.newAppender(CONSOLE_APPENDER, "Console")
                .addAttribute("target", SYSTEM_OUT)
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", CONSOLE_LOG_PATTERN)
                        .addAttribute("disableAnsi", "false")
                );
    }

    private static AppenderComponentBuilder getRollingFileAppender(ConfigurationBuilder<BuiltConfiguration> builder) {
        return builder.newAppender(ROLLING_FILE_APPENDER, "RollingFile")
                .addAttribute("fileName", ROLLING_FILE_FOLDER + ROLLING_FILE_NAME)
                .addAttribute("filePattern", ROLLING_FILE_FOLDER_PATTERN + ROLLING_FILE_NAME_PATTERN)
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", ROLLING_FILE_LOG_PATTERN)
                        .addAttribute("disableAnsi", "false")
                )
                .addComponent(builder.newComponent("Policies")
                        .addComponent(builder.newComponent("SizeBasedTriggeringPolicy")
                                .addAttribute("size", ROLLING_FILE_MAX_SIZE)
                        )
                        .addComponent(builder.newComponent("TimeBasedTriggeringPolicy")
                                .addAttribute("interval", ROLLING_FILE_INTERVAL)
                        )
                )
                .addComponent(builder.newComponent("DefaultRolloverStrategy")
                        .addAttribute("max", ROLLING_FILE_MAX_COUNT)
                );
    }

    private static AppenderComponentBuilder getFileAppender(ConfigurationBuilder<BuiltConfiguration> builder) {
        return builder.newAppender(FILE_APPENDER, "File")
                .addAttribute("fileName", LOGS_FOLDER + FILE_NAME)
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", FILE_LOG_PATTERN)
                        .addAttribute("disableAnsi", "false")
                );
    }

    private static AppenderComponentBuilder getSmtpAppender(ConfigurationBuilder<BuiltConfiguration> builder) {
        HashMap<String, String> properties = getSecurityProperties();
        String username = properties.get("secrets.mail-username");
        String password = properties.get("secrets.mail-password");

        return builder.newAppender(SMTP_APPENDER, "SMTP")
                .addAttribute("subject", SMTP_SUBJECT)
                .addAttribute("to", SMTP_TO)
                .addAttribute("from", username)
                .addAttribute("smtpHost", "smtp.gmail.com")
                .addAttribute("smtpPort", "587")
                .addAttribute("smtpUsername", username)
                .addAttribute("smtpPassword", password)
                .addAttribute("smtpProtocol", "smtp")
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", SMTP_LOG_PATTERN)
                );
    }

    private static HashMap<String, String> getSecurityProperties() {
        try {
            Properties properties = new Properties();

            properties.load(new FileInputStream("src/main/resources/secrets.properties"));

            return Maps.newHashMap(Maps.fromProperties(properties));
        } catch (Exception e) {
            throw new RuntimeException("Error while reading security.properties", e);
        }
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[]{"*"};
    }
}
