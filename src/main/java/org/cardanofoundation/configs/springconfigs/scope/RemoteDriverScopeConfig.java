package org.cardanofoundation.configs.springconfigs.scope;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoteDriverScopeConfig {


    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new RemoteDriverScopePostProcess();
    }

}
