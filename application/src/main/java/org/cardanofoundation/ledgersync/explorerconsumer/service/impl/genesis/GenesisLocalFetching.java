package org.cardanofoundation.ledgersync.explorerconsumer.service.impl.genesis;

import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ledgersync.explorerconsumer.service.GenesisFetching;
import org.cardanofoundation.ledgersync.explorerconsumer.util.FileUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;


@Slf4j
@Component
@Profile("!internet")
public class GenesisLocalFetching implements GenesisFetching {

    /**
     * get json string from input url
     *
     * @param url
     * @return json String
     */
    @Override
    public String getContent(String url) {
        try {
            if (url.startsWith("classpath:")) {
                return FileUtil.readFileFromClasspath(url.substring(10));
            } else {
                return FileUtil.readFile(ResourceUtils.getFile(url).getAbsolutePath());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("exception {} with url {}", e.getMessage(), url);
            throw new IllegalStateException("can't load file " + url);
        }
    }


}

