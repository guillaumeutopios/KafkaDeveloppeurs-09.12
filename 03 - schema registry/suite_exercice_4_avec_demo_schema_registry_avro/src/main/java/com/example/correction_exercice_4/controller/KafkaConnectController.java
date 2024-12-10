package com.example.correction_exercice_4.controller;

import com.example.correction_exercice_4.config.KafkaConnectConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/kafka-connect")
public class KafkaConnectController {

    @Autowired
    private KafkaConnectConfig kafkaConnectConfig;

    private RestTemplate restTemplate;

    public KafkaConnectController(KafkaConnectConfig config) {
        this.restTemplate = config.restTemplate();
    }

    @PostMapping("/connectors")
    public ResponseEntity<String> createConnector(@RequestBody Map<String, Object> connectorConfig) {
        String url = kafkaConnectConfig.getKafkaConnectUrl() + "/connectors";
        return restTemplate.postForEntity(url, connectorConfig, String.class);
    }

    @GetMapping("/connectors/{name}")
    public ResponseEntity<String> getConnector(@PathVariable String name) {
        String url = kafkaConnectConfig.getKafkaConnectUrl() + "/connectors/" + name;
        return restTemplate.getForEntity(url, String.class);
    }

    @PutMapping("/connectors/{name}/config")
    public ResponseEntity<String> updateConnector(@PathVariable String name, @RequestBody Map<String, Object> newConfig) {
        String url = kafkaConnectConfig.getKafkaConnectUrl() + "/connectors/" + name + "/config";
        return restTemplate.postForEntity(url, newConfig, String.class);
    }

    @DeleteMapping("/connectors/{name}")
    public ResponseEntity<String> deleteConnector(@PathVariable String name) {
        String url = kafkaConnectConfig.getKafkaConnectUrl() + "/connectors/" + name;
        restTemplate.delete(url);
        return ResponseEntity.ok("Connector deleted: " + name);
    }
}

