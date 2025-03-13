package com.kreamify.domain.config;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AutoConfigureMockMvc
@Import({TestConfig.Sample.class,})


public @interface TestConfig {
    class Sample {
        @Bean
        MockMvcBuilderCustomizer utf8Config() {
            return builder ->
                    builder.addFilters(new CharacterEncodingFilter("UTF-8", true));
        }
    }
}
