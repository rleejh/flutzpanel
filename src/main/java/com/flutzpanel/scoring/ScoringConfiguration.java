package com.flutzpanel.scoring;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ScoringConfiguration {


    @Bean
    public ScaleOfValues scaleOfValues() throws IOException {
        return new ScaleOfValuesLoader().load();
    }
}
