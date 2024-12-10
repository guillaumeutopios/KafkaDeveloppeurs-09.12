package com.example.demo_stream.entity;

import lombok.Data;

@Data
public class Transaction {
    private String transactionId;
    private String cardNumber;
    private double amount;
    private long timestamp;
}
