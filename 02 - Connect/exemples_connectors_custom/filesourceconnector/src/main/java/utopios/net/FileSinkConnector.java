package utopios.net;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.sink.SinkConnector;
import org.apache.kafka.connect.sink.SinkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileSinkConnector extends SinkConnector {
    private Map<String, String> configProperties;

    public String version() {
        return "1.0";
    }
    @Override
    public void start(Map<String, String> props) {
        configProperties = props;
    }

    @Override
    public Class<? extends SinkTask> taskClass() {
        return FileSinkTask.class;
    }

    @Override
    public java.util.List<Map<String, String>> taskConfigs(int maxTasks) {
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
        return new ConfigDef();
    }
}
