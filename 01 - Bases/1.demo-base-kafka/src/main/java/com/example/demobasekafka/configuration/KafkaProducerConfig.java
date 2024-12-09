//package com.example.demobasekafka.configuration;
//
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaProducerConfig {
//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapServers;
//
//    @Value("${critical.partition}")
//    private String criticalPartition;
//
//    private Map<String, Object> commonConfig() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        configProps.put("critical.partition", criticalPartition);
//        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MessagePartitioner.class);
//        return configProps;
//    }
//
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplateSync() {
//        Map<String, Object> configProps = commonConfig();
//        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
//        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
//        return new KafkaTemplate<>(producerFactory);
//    }
//
//    @Bean
//    @Primary
//    public KafkaTemplate<String, String> kafkaTemplateAsync() {
//        Map<String, Object> configProps = commonConfig();
//        configProps.put(ProducerConfig.ACKS_CONFIG, "1");
//        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
//        return new KafkaTemplate<>(producerFactory);
//    }
//
//}
