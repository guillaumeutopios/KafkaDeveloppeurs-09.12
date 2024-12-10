package com.example.demo_stream_kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Envoie un message au topic Kafka
     * @param topic Le nom du topic Kafka
     * @param message Le message à envoyer
     */
    public void sendMessage(String topic, String key, String message) {
        System.out.println("📤 Sending message to topic: " + topic + ", message: " + message);
        kafkaTemplate.send(topic, key, message);
    }
}
