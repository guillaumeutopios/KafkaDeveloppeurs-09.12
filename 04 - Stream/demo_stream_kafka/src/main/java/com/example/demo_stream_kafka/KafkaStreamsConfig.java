package com.example.demo_stream_kafka;


import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

@Configuration
public class KafkaStreamsConfig {

    @Bean(name = "streamConfig")
    public Properties kafkaStreamsProperties() {
        Properties props = new Properties();

        // 1️⃣ Identifiant de l'application Kafka Streams
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "demo-stream-kafka");

        // 2️⃣ Serveurs de Kafka
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // 3️⃣ Sérialisation par défaut pour les clés et les valeurs
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // 4️⃣ Optionnel: Indique à Kafka combien de threads utiliser pour le traitement
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 1);

        return props;
    }
}
