package com.schoolmanagement.config;

import com.schoolmanagement.payload.dto.DeanDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateObjectBean {

    @Bean
    public DeanDto deanDTO() {
        return new DeanDto();
    }
}