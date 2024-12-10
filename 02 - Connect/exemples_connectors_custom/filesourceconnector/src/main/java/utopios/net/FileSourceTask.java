package utopios.net;

import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileSourceTask extends SourceTask {

    private String filePath;
    private String topic;
    private BufferedReader reader;
    @Override
    public String version() {
        return null;
    }

    @Override
    public void start(Map<String, String> map) {
        filePath = map.get("filepath");
        topic = map.get("topic");
        try {
            reader = new BufferedReader(new FileReader(filePath));
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        List<SourceRecord> records = new ArrayList<>();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                records.add(new SourceRecord(null, null, topic, null, line));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return records;
    }

    @Override
    public void stop() {
        try {
            reader.close();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
