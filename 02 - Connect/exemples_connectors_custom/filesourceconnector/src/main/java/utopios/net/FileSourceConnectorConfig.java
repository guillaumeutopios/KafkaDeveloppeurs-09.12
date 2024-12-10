package utopios.net;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class FileSourceConnectorConfig extends AbstractConfig {
    public static final String FILE_PATH_CONFIG = "file.path";
    public static final String TOPIC_CONFIG = "topic";
    public FileSourceConnectorConfig(ConfigDef definition, Map<?, ?> originals) {
        super(definition, originals);
    }

    public static ConfigDef config() {
        return new ConfigDef()
                .define(FILE_PATH_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Path to the source file.")
                .define(TOPIC_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Kafka topic to publish data.");
    }
}
