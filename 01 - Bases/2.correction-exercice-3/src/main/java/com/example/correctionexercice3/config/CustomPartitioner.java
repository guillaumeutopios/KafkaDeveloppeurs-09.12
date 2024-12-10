package com.example.correctionexercice3.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class CustomPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // Exemple : diriger les "CRITICAL" vers la partition 0 et les autres vers la partition 1
        String messageKey = (String) key;
        if (messageKey.contains("CRITICAL")) {
            return 0;
        } else {
            int numPartitions = cluster.availablePartitionsForTopic(topic).size();
            return ThreadLocalRandom.current().nextInt(numPartitions -1) +1;
        }
    }

    @Override
    public void close() {
        // Cleanup if necessary
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // Configuration if necessary
    }
}

