package com.example.correction_exercice_4.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class KafkaConnectConfig {

    @Value("${kafka.connect.url}")
    private String kafkaConnectUrl;

    public String getKafkaConnectUrl() {
        return kafkaConnectUrl;
    }

    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
