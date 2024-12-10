package com.example.demo_stream.controller;

import com.example.demo_stream.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {



    @Autowired
    private KafkaTemplate<String, Transaction> kafkaTemplate;

    @PostMapping
    public String sendTransaction(@RequestBody Transaction transaction) {
        kafkaTemplate.send("transactions", transaction.getCardNumber(), transaction);
        return "Transaction sent";
    }
}