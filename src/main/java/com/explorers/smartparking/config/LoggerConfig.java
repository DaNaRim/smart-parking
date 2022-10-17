package com.explorers.smartparking.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.net.URI;

@Plugin(name = "CustomConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class LoggerConfig extends ConfigurationFactory {

    private static final String LOGS_FOLDER = "./logs/";
    private static final Level ROOT_LEVEL = Level.ALL;

    private static final String CONSOLE_APPENDER = "CONSOLE";
    private static final String CONSOLE_PATTERN
            = "%d %highlight{%-5p} [%15.15thread] %style{%40.40logger{1}}{blue} : %m %throwable{short}%n";
    private static final ConsoleAppender.Target CONSOLE_TARGET = ConsoleAppender.Target.SYSTEM_OUT;
    private static final Level CONSOLE_LEVEL = Level.INFO;

    private static final String ROLLING_FILE_APPENDER = "ROLLING_FILE";
    private static final String ROLLING_FILE_NAME = "rolling.log";
    private static final String ROLLING_FILE_NAME_PATTERN = "rolling-%d{yyyy-MM-dd}-%i.log.gz";
    private static final String ROLLING_FILE_INTERVAL = "1";
    private static final String ROLLING_FILE_PATTERN
            = "%d %highlight{%-5p} [%20.20thread] %style{%40.40logger{3.}}{blue} : %m %n%throwable";
    private static final String ROLLING_FILE_MAX_SIZE = "100MB";
    private static final String ROLLING_FILE_MAX_COUNT = "5";
    private static final Level ROLLING_FILE_LEVEL = Level.DEBUG;

    private static final String FILE_APPENDER = "FILE";
    private static final String FILE_NAME = "log.log";
    private static final String FILE_PATTERN
            = "%d %highlight{%-5p} [%15.15thread] %style{%40.40logger{1}}{blue} : %m %n%throwable{5}";
    private static final Level FILE_LEVEL = Level.INFO;

    private static Configuration createConfiguration(final String name,
                                                     final ConfigurationBuilder<BuiltConfiguration> builder) {

        builder.setConfigurationName(name);
//        builder.setMonitorInterval("5");

        builder.add(getConsoleAppender(builder));
        builder.add(getRollingFileAppender(builder));
        builder.add(getFileAppender(builder));

        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(ROOT_LEVEL)
                .add(builder.newAppenderRef(CONSOLE_APPENDER).addAttribute("level", CONSOLE_LEVEL))
                .add(builder.newAppenderRef(FILE_APPENDER).addAttribute("level", FILE_LEVEL))
                .add(builder.newAppenderRef(ROLLING_FILE_APPENDER).addAttribute("level", ROLLING_FILE_LEVEL));

        builder.add(rootLogger);

        return builder.build();
    }

    private static AppenderComponentBuilder getConsoleAppender(ConfigurationBuilder<BuiltConfiguration> builder) {
        return builder.newAppender(CONSOLE_APPENDER, "Console")
                .addAttribute("target", CONSOLE_TARGET)
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", CONSOLE_PATTERN)
                        .addAttribute("disableAnsi", "false")
                );
    }

    private static AppenderComponentBuilder getRollingFileAppender(ConfigurationBuilder<BuiltConfiguration> builder) {
        return builder.newAppender(ROLLING_FILE_APPENDER, "RollingFile")
                .addAttribute("fileName", LOGS_FOLDER + ROLLING_FILE_NAME)
                .addAttribute("filePattern", LOGS_FOLDER + ROLLING_FILE_NAME_PATTERN)
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", ROLLING_FILE_PATTERN)
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
                        .addAttribute("pattern", FILE_PATTERN)
                        .addAttribute("disableAnsi", "false")
                );
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
