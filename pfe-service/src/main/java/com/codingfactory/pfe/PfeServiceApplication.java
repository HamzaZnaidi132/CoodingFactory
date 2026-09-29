package com.codingfactory.pfe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PfeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PfeServiceApplication.class, args);
    }
}