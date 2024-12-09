package com.example.demobasekafka.controller;

import com.example.demobasekafka.service.NotificationProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/notification")
public class NotificationController {

    private final NotificationProducerService notificationProducerService;

    public NotificationController(NotificationProducerService notificationProducerService) {
        this.notificationProducerService = notificationProducerService;
    }

    @PostMapping("/critical")
    public String sendCriticalNotification(@RequestParam String message) {
        notificationProducerService.sendCriticalNotification("critical_notifications", message);
        return  "Critical notification sent";
    }

    @PostMapping("/general")
    public String sendGeneralNotification(@RequestParam String message) {
        notificationProducerService.sendGeneralNotification("general_notifications", message);
        return  "genral notification sent";
    }
}

