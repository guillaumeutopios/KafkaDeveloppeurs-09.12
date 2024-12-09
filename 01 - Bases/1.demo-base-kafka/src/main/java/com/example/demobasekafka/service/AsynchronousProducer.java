package com.example.demobasekafka.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class AsynchronousProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public AsynchronousProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        future.whenCompleteAsync((result, ex) -> {
           if(ex != null) {
               ex.printStackTrace();
           }else {
               System.out.printf("Sent message to topic %s partition %d offset %d%n",
                       result.getRecordMetadata().topic(),
                       result.getRecordMetadata().partition(),
                       result.getRecordMetadata().offset());
           }
        });
    }
}

