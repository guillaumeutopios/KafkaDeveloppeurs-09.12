package com.example.demo_stream_kafka;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final KafkaProducerService producerService;

    @Autowired
    public MessageController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    /**
     * 🔥 Envoie un message au topic 'demo-stream-topic'
     * pour tester toutes les transformations, les filtrages, les branchements et autres opérations.
     */
    @PostMapping("/send/demo-stream")
    public String sendToDemoStreamTopic(@RequestParam String key, @RequestParam String value) {
        producerService.sendMessage("demo-stream-topic", key, value);
        return "Message sent to demo-stream-topic";
    }

    /**
     * 🔥 Envoie un message au topic 'other-demo-stream-topic'
     * pour tester la fusion des flux **merge**.
     */
    @PostMapping("/send/other-demo-stream")
    public String sendToOtherDemoStreamTopic(@RequestParam String key, @RequestParam String value) {
        producerService.sendMessage("other-demo-stream-topic", key, value);
        return "Message sent to other-demo-stream-topic";
    }

    /**
     * 🔥 Envoie un message au topic 'demo-output-filter-topic'.
     * Ce topic reçoit les messages **filtrés** par la méthode filterStream.
     */
    @PostMapping("/send/demo-output-filter")
    public String sendToDemoOutputFilterTopic(@RequestParam String key, @RequestParam String value) {
        producerService.sendMessage("demo-stream-filter-topic", key, value);
        return "Message sent to demo-output-filter-topic";
    }

    /**
     * 🔥 Envoie un message au topic 'demo-output-map-topic'.
     * Ce topic reçoit les messages **transformés** par la méthode mapValuesStream.
     */
    @PostMapping("/send/demo-output-map")
    public String sendToDemoOutputMapTopic(@RequestParam String key, @RequestParam String value) {
        producerService.sendMessage("demo-stream-map-topic", key, value);
        return "Message sent to demo-output-map-topic";
    }

    /**
     * 🔥 Envoie un message au topic 'demo-output-branch-topic-1'.
     * Ce topic reçoit les messages **branchement 1** où la valeur contient le mot "error".
     */
    @PostMapping("/send/demo-output-branch-1")
    public String sendToDemoOutputBranch1(@RequestParam String key, @RequestParam String value) {
        producerService.sendMessage("demo-stream-branch-topic", key, value);
        return "Message sent to demo-output-branch-topic-1";
    }

    /**
     * 🔥 Envoie un message au topic 'demo-output-branch-topic-2'.
     * Ce topic reçoit les messages **branchement 2** où la valeur contient le mot "info".
     */
    @PostMapping("/send/demo-output-branch-2")
    public String sendToDemoOutputBranch2(@RequestParam String key, @RequestParam String value) {
        producerService.sendMessage("demo-stream-branch-topic", key, value);
        return "Message sent to demo-output-branch-topic-2";
    }

    /**
     * 🔥 Envoie un message au topic 'demo-output-aggregation-topic'.
     * Ce topic reçoit les messages **agrégés** à partir de la méthode aggregateStream.
     */
    @PostMapping("/send/demo-output-aggregation")
    public String sendToDemoOutputAggregationTopic(@RequestParam String key, @RequestParam String value) {
        producerService.sendMessage("demo-stream-aggregate-topic", key, value);
        return "Message sent to demo-output-aggregation-topic";
    }

    /**
     * 🔥 Envoie un message au topic 'demo-output-merge-topic'.
     * Ce topic reçoit les messages **fusionnés** provenant de plusieurs flux.
     */
    @PostMapping("/send/demo-output-merge")
    public String sendToDemoOutputMergeTopic(@RequestParam String key, @RequestParam String value) {
        producerService.sendMessage("demo-stream-merge1-topic", key, value);
        producerService.sendMessage("demo-stream-merge2-topic", key, value);
        return "Message sent to demo-output-merge-topic";
    }

    /**
     * 🔥 Envoie un message au topic 'demo-output-stream-topic'.
     * Ce topic reçoit les messages traités par la méthode kStream.
     */
    @PostMapping("/send/demo-output-stream")
    public String sendToDemoOutputStreamTopic(@RequestParam String key, @RequestParam String value) {
        producerService.sendMessage("demo-output-stream-topic", key, value);
        return "Message sent to demo-output-stream-topic";
    }

}
