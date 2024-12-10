package com.example.correction_exercice_4.config;


import com.example.avro.DataDemo;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumers {

    @KafkaListener(
            topics = {"postgres-data"},
            groupId = "exercice-4"
    )
    public void consume(String message) {
        System.out.println("[Consumer] Received: " + message);
    }

    @KafkaListener(
            topics = {"postgres-data_demo"},
            groupId = "exercice-4"
    )
    public void consumeDataToSink(String message) {
        System.out.println("[Consumer] Received: " + message);
    }


    @KafkaListener(topics = "postgressink-data_demo", groupId = "avro-consumer-group")
    public void consume(DataDemo message) {
        System.out.println("Received Avro message: " + message);
    }
}
