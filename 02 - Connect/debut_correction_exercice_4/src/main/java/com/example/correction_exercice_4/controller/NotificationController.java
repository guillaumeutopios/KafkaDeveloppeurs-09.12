package com.example.correction_exercice_4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "notifications";

    @PostMapping("/send")
    public String sendNotification(@RequestParam String key, @RequestParam String message) {
        kafkaTemplate.send(TOPIC, message);
        return "Notification sent: Message=" + message;
    }

    @PostMapping("/sendToPostgreSink")
    public String sendData(@RequestParam Integer id, @RequestParam Integer valeur) {
        // Créer un message JSON
        String message = String.format("{\"id\": %d, \"valeur\": %d}", id, valeur);

        // Envoyer le message vers le sujet Kafka
        kafkaTemplate.send("postgres-data_demo", message);
        return "Message sent to Kafka: " + message;
    }
}
