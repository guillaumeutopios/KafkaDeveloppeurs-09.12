package com.example.demo_stream_kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafkaStreams
public class StreamProcessService {

    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
        System.out.println("🚀 KStream method called!");

        // Liaison au topic source
        KStream<String, String> kStream = streamsBuilder.stream("demo-stream-topic", Consumed.with(Serdes.String(), Serdes.String()));

        // Traitement du flux : Ajoute des crochets au message
        KStream<String, String> processedStream = kStream
                .mapValues(value -> "[" + value.toUpperCase() + "]") // Exemple de transformation
                .peek((key, value) -> System.out.println("🔄 Transformed message: key=" + key + ", value=" + value));

        // Envoie le message vers un autre topic
        //Le serde indique comment serialiser les messages
        processedStream.to("demo-output-stream-topic",  Produced.with(Serdes.String(), Serdes.String()));

        // Retourne le flux traité
        return processedStream;
    }

//    @Bean
//    public KStream<String, String> filterStream(StreamsBuilder streamsBuilder) {
//        KStream<String, String> kStream = streamsBuilder.stream("demo-stream-filter-topic", Consumed.with(Serdes.String(), Serdes.String()));
//
//        KStream<String, String> filteredStream = kStream
//                .filter((key, value) -> value != null && value.contains("important"))
//                .peek((key, value) -> System.out.println("📩 Filtered message: " + value));
//
//        filteredStream.to("demo-output-filter-topic",  Produced.with(Serdes.String(), Serdes.String()));
//
//        return filteredStream;
//    }
//
//
//    @Bean
//    public KStream<String, String> mapValuesStream(StreamsBuilder streamsBuilder) {
//        KStream<String, String> kStream = streamsBuilder.stream("demo-stream-map-topic", Consumed.with(Serdes.String(), Serdes.String()));
//
//        KStream<String, String> mappedStream = kStream
//                .mapValues(value -> value.toUpperCase())
//                .peek((key, value) -> System.out.println("🔄 Transformed message: " + value));
//
//        mappedStream.to("demo-output-map-topic",  Produced.with(Serdes.String(), Serdes.String()));
//
//        return mappedStream;
//    }
//
//    @Bean
//    public KStream<String, String> branchStream(StreamsBuilder streamsBuilder) {
//        KStream<String, String> kStream = streamsBuilder.stream("demo-stream-branch-topic", Consumed.with(Serdes.String(), Serdes.String()));
//
//        KStream<String, String>[] branches = kStream.branch(
//                (key, value) -> value.contains("error"), // Branche 1
//                (key, value) -> value.contains("info")   // Branche 2
//        );
//
//        branches[0].to("demo-output-branch-topic-1",  Produced.with(Serdes.String(), Serdes.String()));
//        branches[1].to("demo-output-branch-topic-2",  Produced.with(Serdes.String(), Serdes.String()));
//
//        return kStream; // Retourne le flux d'origine (optionnel)
//    }
//
//    @Bean
//    public KStream<String, Long> aggregateStream(StreamsBuilder streamsBuilder) {
//        KStream<String, String> kStream = streamsBuilder.stream("demo-stream-aggregate-topic", Consumed.with(Serdes.String(), Serdes.String()));
//
//        KStream<String, Long> aggregatedStream = kStream.groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
//                .count()
//                .toStream()
//                .peek((key, value) -> System.out.println("📊 Aggregated key: " + key + ", count: " + value));
//
//        aggregatedStream.to("demo-output-aggregation-topic", Produced.with(Serdes.String(), Serdes.Long()));
//
//        return aggregatedStream;
//    }
//
//    @Bean
//    public KStream<String, String> mergeStreams(StreamsBuilder streamsBuilder) {
//        KStream<String, String> kStream1 = streamsBuilder.stream("demo-stream-merge1-topic", Consumed.with(Serdes.String(), Serdes.String()));
//        KStream<String, String> kStream2 = streamsBuilder.stream("demo-stream-merge2-topic", Consumed.with(Serdes.String(), Serdes.String()));
//
//        KStream<String, String> mergedStream = kStream1.merge(kStream2)
//                .peek((key, value) -> System.out.println("📦 Merged message: " + value));
//
//        mergedStream.to("demo-output-merge-topic",  Produced.with(Serdes.String(), Serdes.String()));
//
//        return mergedStream;
//    }
}