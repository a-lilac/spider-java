package com.carlos.config;


import com.carlos.es.TransportClientFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Carlos on 2017/3/23.
 */
@Configuration
public class TransportClientConfig {
    @Bean(initMethod = "initClient")
    public TransportClientFactoryBean getTransprotClientFactoryBean() {
        return new TransportClientFactoryBean();
    }
}
