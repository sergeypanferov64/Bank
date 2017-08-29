package ru.spanferov.interview.test.bank.config;

import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ru.spanferov.interview.test.bank.controller.rest.util.RestErrorAttributes;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ErrorAttributes errorAttributes() {
        return new RestErrorAttributes();
    }

}
