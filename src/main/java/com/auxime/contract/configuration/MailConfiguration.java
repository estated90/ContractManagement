package com.auxime.contract.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

/**
 * @author Nicolas
 * @version 1.0.0
 *
 */
@Configuration
public class MailConfiguration {

    /**
     * @return the configuration used for the mails
     */
    @Bean
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean fmConfigFactoryBean = new FreeMarkerConfigurationFactoryBean();
        fmConfigFactoryBean.setTemplateLoaderPath("classpath:/templates");
        return fmConfigFactoryBean;
    }
    
}
