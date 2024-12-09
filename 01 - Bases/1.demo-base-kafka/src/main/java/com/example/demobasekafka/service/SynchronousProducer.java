package com.example.demobasekafka.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class SynchronousProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public SynchronousProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        // la clé sert au partitionnement → une clé donnera une partition selon le partitionner
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic,"custom_key", message);
        try {
            SendResult<String, String> result = future.get();
            System.out.printf("Sent message to topic %s partition %d offset %d%n",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

