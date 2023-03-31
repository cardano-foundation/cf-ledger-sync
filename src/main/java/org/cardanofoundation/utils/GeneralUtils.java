package org.cardanofoundation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class GeneralUtils {

    public final Logger log = LogManager.getLogger(GeneralUtils.class.getName());

    public void addInfoLog(String logText){
        log.info(logText);
    }
}
