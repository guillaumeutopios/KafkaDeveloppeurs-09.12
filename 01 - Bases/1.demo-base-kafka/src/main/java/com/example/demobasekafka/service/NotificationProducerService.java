package com.example.demobasekafka.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class NotificationProducerService {
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public NotificationProducerService(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }


    private final KafkaTemplate<String, String> kafkaTemplateAsync;

    private final KafkaTemplate<String, String> kafkaTemplateSync;

    public NotificationProducerService(/*@Qualifier("kafkaTemplateAsync")*/ KafkaTemplate<String, String> kafkaTemplateAsync,/*@Qualifier("kafkaTemplateSync")*/ KafkaTemplate<String, String> kafkaTemplateSync) {
        this.kafkaTemplateAsync = kafkaTemplateAsync;
        this.kafkaTemplateSync = kafkaTemplateSync;
    }

    public void sendCriticalNotification(String topic, String message) {
        try {
            kafkaTemplateSync.send(topic,"new_message", message).get();
        }catch (InterruptedException | ExecutionException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to send message", ex);
        }
    }

    public void sendGeneralNotification(String topic, String message) {
        kafkaTemplateAsync.send(topic,"general_message",message).whenCompleteAsync((result, ex) -> {
           if(ex != null) {
               System.out.println("Message sent successfully");
           }else {
               System.out.println("Failed to send message " +ex.getMessage());
           }
        });
    }
}
