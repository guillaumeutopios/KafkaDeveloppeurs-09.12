package utopios.net;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileSourceConnector extends SourceConnector {
    private Map<String, String> configProperties;

    public String version() {
        return "1.0";
    }
    @Override
    public void start(Map<String, String> props) {
        configProperties = props;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return FileSourceTask.class;
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
        // Cleanup
    }

    @Override
    public ConfigDef config() {
        return new ConfigDef();
    }
}
