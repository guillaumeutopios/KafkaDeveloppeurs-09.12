package com.example.demobasekafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class DataConsumer {

    @KafkaListener(topics = "demo_topic", groupId = "demo-id")
    public void listenData(String message, Acknowledgment acknowledgment) {
        System.out.println("Received message "+message);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "demo_topic", groupId = "demo-id")
    public void listenData2(String message, Acknowledgment acknowledgment) {
        System.out.println("Received message "+message);
        acknowledgment.acknowledge();
    }
}
