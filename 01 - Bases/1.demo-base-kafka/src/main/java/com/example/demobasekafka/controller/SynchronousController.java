package com.example.demobasekafka.controller;

import com.example.demobasekafka.service.SynchronousProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SynchronousController {
    private final SynchronousProducer synchronousProducer;

    public SynchronousController(SynchronousProducer synchronousProducer) {
        this.synchronousProducer = synchronousProducer;
    }

    @GetMapping("/sendSync")
    public String sendMessage(@RequestParam("message") String message) {
        synchronousProducer.sendMessage("demo_topic", message);
        return  "Message sent synchronously";
    }
}
