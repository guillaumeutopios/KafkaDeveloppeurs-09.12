package com.example.demobasekafka.controller;

import com.example.demobasekafka.service.AsynchronousProducer;
import com.example.demobasekafka.service.SynchronousProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AsynchronousController {
    private final AsynchronousProducer asynchronousProducer;

    public AsynchronousController(AsynchronousProducer asynchronousProducer) {
        this.asynchronousProducer = asynchronousProducer;
    }

    @GetMapping("/sendAsync")
    public String sendMessage(@RequestParam("message") String message) {
        asynchronousProducer.sendMessage("demo_topic", message);
        return  "Message sent Asynchronously";
    }
}
