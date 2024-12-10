package com.example.demo_stream.service;


import com.example.demo_stream.entity.Transaction;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.time.Duration;

@Configuration
@EnableKafkaStreams
public class FraudDetectionStreamService {

    @Bean
    public KStream<String, Transaction> kStreamFraudDetection(StreamsBuilder builder) {

        // 1️⃣ Création d'un flux KStream à partir du topic 'transactions'
        // Ce flux contient des transactions où la clé est l'identifiant de l'utilisateur
        KStream<String, Transaction> transactions = builder.stream("transactions");

        // 2️⃣ Création d'une KTable pour compter les transactions par utilisateur dans une fenêtre temporelle de 1 minute
        KTable<Windowed<String>, Long> fraudCounts = transactions
                // Regroupe les transactions par clé (par utilisateur)
                .groupByKey()
                // Définit une fenêtre de temps glissante de 1 minute avec une avancée toutes les 10 secondes
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1)).advanceBy(Duration.ofSeconds(10)))
                // Compte le nombre de transactions pour chaque utilisateur dans la fenêtre de temps
                .count(Materialized.as("fraud-counts"));

        // 3️⃣ Filtrer les utilisateurs qui ont effectué au moins 3 transactions dans la fenêtre de 1 minute
        fraudCounts.toStream()
                // Filtre les utilisateurs dont le nombre de transactions dans la fenêtre de temps est supérieur ou égal à 3
                .filter((key, count) -> count >= 3)
                // Crée un nouveau flux de paires clé-valeur, où la clé est l'identifiant de l'utilisateur et la valeur est le message d'alerte de fraude
                .map((key, value) -> new KeyValue<>(key.key(), "Fraud activity detected " + value + " in the last minute"))
                // Envoie le message d'alerte au topic 'fraud-alertes'
                .to("fraud-alertes", Produced.with(Serdes.String(), Serdes.String()));

        // Retourne le flux des transactions originales (utile si on veut réutiliser ce flux dans d'autres traitements)
        return transactions;
    }
}
