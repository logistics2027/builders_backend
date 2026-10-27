package com.hltech.productservice.config;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CategoryClientConfig {

    @Bean
    @LoadBalanced
    public RestTemplate categoryRestTemplate() {
        return new RestTemplate();
    }
}