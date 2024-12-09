package com.example.demobasekafka.configuration;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class CustomPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        int numPartitions = cluster.partitionCountForTopic(topic);

        if (key == null) {
            // Si aucune clé n'est fournie, répartir de manière uniforme
            return ThreadLocalRandom.current().nextInt(numPartitions);
        }
        // une même clé sera sur une même partition
        // Assurez-vous que le hash est toujours positif pour éviter des IndexOutOfBounds
        return Math.abs(key.hashCode()) % numPartitions + 1;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
