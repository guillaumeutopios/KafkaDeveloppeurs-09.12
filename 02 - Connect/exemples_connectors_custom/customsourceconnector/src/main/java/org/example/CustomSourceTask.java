package org.example;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import java.util.*;

public class CustomSourceTask extends SourceTask {

    //private Random random = new Random();
    private String topic;

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public void start(Map<String, String> map) {
        topic = map.get("topic");
    }

    private String randomData() {
        return UUID.randomUUID().toString();
    }
    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        String data = randomData();
        SourceRecord record = new SourceRecord(null, null, topic, Schema.STRING_SCHEMA, data);
        return Collections.singletonList(record);
    }

    @Override
    public void stop() {

    }
}
