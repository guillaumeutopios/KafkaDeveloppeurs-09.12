package org.example;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomSourceConnector extends SourceConnector {

    private Map<String, String> configProperties;

    @Override
    public void start(Map<String, String> props) {
        configProperties = props;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return CustomSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> configs = new ArrayList<>();
        for(int i=0; i < maxTasks; i++) {
            configs.add(configProperties);
        }
        return configs;
    }

    @Override
    public void stop() {

    }

    @Override
    public ConfigDef config() {
        return null;
    }

    @Override
    public String version() {
        return "1.0";
    }
}
