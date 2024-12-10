package com.example.demo_stream.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "fraud-alertes")
    public void listenStreamFraude(String message) {
        logger.info("Received message in output-stream-topic: {}", message);
    }
}
