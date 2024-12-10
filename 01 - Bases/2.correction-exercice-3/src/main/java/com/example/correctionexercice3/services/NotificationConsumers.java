package com.example.correctionexercice3.services;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumers {

    @KafkaListener(topicPartitions = @TopicPartition(topic = "notifications", partitions = {"0"}), groupId = "critical-consumer-group")
    public void consumeCritical(ConsumerRecord<String,String> record) {
        System.out.println("Received critical message: \"" + record.value() + "\" from partition: " + record.partition());
    }

    @KafkaListener(topics = "notifications", groupId = "likes-consumer-group")
    public void consumeLikes(ConsumerRecord<String,String> record) {
        if(record.partition() != 0)
            if (record.value().contains("LIKE")) {
                System.out.println("Received LIKE: \"" + record.value() + "\" from partition: " + record.partition());
            }
            else {
                System.out.println("Received message: \"" + record.value() + "\" from partition: " + record.partition());
            }
    }
}

