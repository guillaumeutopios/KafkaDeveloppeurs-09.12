package com.example.correction_exercice_4.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    /*@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;*/
    @Autowired
    private KafkaTemplate<String, GenericRecord> kafkaTemplate;



    private static final String TOPIC = "notifications";

    @PostMapping("/send")
    public String sendNotification(@RequestParam String key, @RequestParam String message) {
        //kafkaTemplate.send(TOPIC, message);
        return "Notification sent: Message=" + message;
    }

    @PostMapping("/sendToPostgreSink")
    public String sendData(@RequestParam Integer id, @RequestParam Integer valeur) {
        // Construire le message Avro
        GenericRecord record = new GenericData.Record(getSchema());
        record.put("id", id);
        record.put("valeur", valeur);

        kafkaTemplate.send("sink4", record);
        return "Message sent to Kafka: " + record.toString();
    }

    private Schema getSchema() {
        String schemaStr = "{\"type\":\"record\",\"name\":\"DataDemo\",\"fields\":[" +
                "{\"name\":\"id\",\"type\":\"int\"}," +
                "{\"name\":\"valeur\",\"type\":\"int\"}" +
                "]}";
        return new Schema.Parser().parse(schemaStr);
    }
}
