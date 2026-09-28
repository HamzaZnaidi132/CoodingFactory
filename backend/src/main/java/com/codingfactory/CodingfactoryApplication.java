package com.codingfactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CodingfactoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodingfactoryApplication.class, args);
    }
}
