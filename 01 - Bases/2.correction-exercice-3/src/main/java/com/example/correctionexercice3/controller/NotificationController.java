package com.example.correctionexercice3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "notifications";

    @PostMapping("/send")
    public String sendNotification(@RequestParam String key, @RequestParam String message) {
        kafkaTemplate.send(TOPIC, key, message);
        return "Notification sent: Key=" + key + ", Message=" + message;
    }
}
