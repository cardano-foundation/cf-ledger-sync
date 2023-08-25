package org.cardanofoundation.ledgersync.explorerconsumer.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Aspect
@Component
public class ExceptionHandlerConfiguration {

    private static final Logger log = LogManager.getLogger(ExceptionHandlerConfiguration.class);

    @ExceptionHandler({Throwable.class})
    public void handleException(Throwable e) {
        log.warn("Business logic exception: {}, stack trace:", e.getMessage());
    }

}
