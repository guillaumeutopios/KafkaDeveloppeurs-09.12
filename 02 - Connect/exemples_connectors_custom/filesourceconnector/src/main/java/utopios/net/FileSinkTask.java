package utopios.net;

import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import java.io.BufferedWriter;
import java.util.Collection;
import java.util.Map;

public class FileSinkTask extends SinkTask {

    private String filePath;
    private BufferedWriter writer;
    @Override
    public String version() {
        return null;
    }

    @Override
    public void start(Map<String, String> map) {
        filePath = map.get("filepath");
    }

    @Override
    public void put(Collection<SinkRecord> collection) {
        try {
            for (SinkRecord record : collection) {
                writer.write(record.value().toString());
                writer.newLine();
            }
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        try {
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
