package com.example.demobasekafka.configuration;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class MessagePartitioner implements Partitioner {

    private int criticalMsgPartition;
    private final String NEW_MESSAGE = "new_message";
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueByte, Cluster cluster) {
        String notificationType = (String) key;
        if(NEW_MESSAGE.equals(notificationType)) {
            return criticalMsgPartition;
        } else {
            int numPartitions = cluster.availablePartitionsForTopic(topic).size();
            //return (key.hashCode() & Integer.MAX_VALUE) % (numPartitions - 1) + 1;
            return 0;
        }
    }

    @Override
    public void close() {
        //Nettoyer et libérer les ressources
    }

    @Override
    public void configure(Map<String, ?> config) {
        String criticalPartition = (String) config.get("critical.partition");
        criticalMsgPartition = criticalPartition != null ? Integer.parseInt(criticalPartition) : 0;
    }
}
