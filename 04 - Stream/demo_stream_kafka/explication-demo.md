🗂️ Structure des Fichiers

Voici la structure typique du projet :

📦 src
 ┣ 📂 main
 ┃ ┣ 📂 java
 ┃ ┃ ┗ 📂 com.example.demo_stream_kafka
 ┃ ┃   ┣ 📜 DemoStreamKafkaApplication.java  // Point d'entrée de l'application Spring Boot
 ┃ ┃   ┣ 📜 StreamProcessService.java       // Contient la logique Kafka Streams
 ┃ ┃   ┗ 📜 KafkaStreamsConfig.java        // Contient la configuration Kafka Streams
 ┃ ┣ 📂 resources
 ┃ ┃ ┗ 📜 application.properties            // Contient la configuration de Kafka (topics, bootstrap, serdes, etc.)

📘 Fichier 1 : application.properties

Le fichier application.properties contient la configuration de l’application Spring Boot et Kafka Streams.

Contenu du fichier :

# Config de Kafka Streams
spring.kafka.streams.application-id=demo-stream-kafka
spring.kafka.streams.bootstrap-servers=kafka-1:19091

# Serde par défaut pour les clés et les valeurs (sérialisation/désérialisation)
spring.kafka.streams.default-key-serde=org.apache.kafka.common.serialization.Serdes$StringSerde
spring.kafka.streams.default-value-serde=org.apache.kafka.common.serialization.Serdes$StringSerde

# Nombre de threads pour Kafka Streams
spring.kafka.streams.threads=1

🔍 Explications des propriétés

	1.	spring.kafka.streams.application-id
➡️ Identifiant unique de l’application Kafka Streams. Chaque application Kafka Streams doit avoir un application-id unique, car Kafka utilise cet ID pour suivre l’état des tâches de traitement.
	2.	spring.kafka.streams.bootstrap-servers
➡️ Adresse des serveurs Kafka. C’est ici que Kafka Streams se connecte.
	3.	spring.kafka.streams.default-key-serde et spring.kafka.streams.default-value-serde
➡️ Définit le Serde par défaut pour les clés et les valeurs. Un Serde est une combinaison de Serializer et de Deserializer. Ici, nous utilisons la sérialisation de type String.
	4.	spring.kafka.streams.threads
➡️ Nombre de threads utilisés pour exécuter les tâches du flux Kafka Streams. Par défaut, c’est 1.


📘 Fichier 3 : KafkaStreamsConfig.java

Ce fichier configure explicitement les propriétés de Kafka Streams si vous ne souhaitez pas tout mettre dans application.properties.

Contenu du fichier :

package com.example.demo_stream_kafka;

import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaStreamsConfig {

    @Bean(name = "streamConfig")
    public Properties kafkaStreamsProperties() {
        Properties props = new Properties();
        
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "demo-stream-kafka");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1:19091");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        
        return props;
    }
}

🔍 Explications

	1.	@Configuration
➡️ Indique à Spring que cette classe contient des Beans de configuration.
	2.	@Bean(name = “streamConfig”)
➡️ Définit un Bean qui fournit les propriétés Kafka Streams.
	3.	Properties
➡️ Ce sont les propriétés de configuration Kafka Streams, qui incluent le APPLICATION_ID_CONFIG, le BOOTSTRAP_SERVERS_CONFIG, etc.

📘 Fichier 4 : StreamProcessService.java

Le fichier le plus important. Ce fichier configure et exécute le flux Kafka Streams.

Contenu du fichier :

package com.example.demo_stream_kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StreamProcessService {

    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> kStream = streamsBuilder.stream(
            "demo-stream-topic", 
            Consumed.with(Serdes.String(), Serdes.String())
        );

        KStream<String, String> processedStream = kStream
            .mapValues(value -> "[" + value.toUpperCase() + "]")
            .peek((key, value) -> System.out.println("🔄 Transformed message: key=" + key + ", value=" + value));

        processedStream.to("demo-output-stream-topic", 
            Produced.with(Serdes.String(), Serdes.String())
        );

        return processedStream;
    }
}

🔍 Explications

	1.	@Configuration
➡️ Indique que cette classe définit des Beans.
	2.	@Bean
➡️ La méthode kStream est un Bean Kafka Streams qui retourne un KStream<String, String>.
	3.	KStream<String, String> kStream
➡️ C’est le flux (stream) d’entrée à partir du topic demo-stream-topic. Le flux est consommé avec Consumed.with(Serdes.String(), Serdes.String()) qui indique que la clé et la valeur sont des chaînes de caractères.
	4.	kStream.mapValues()
➡️ Transforme les valeurs des messages, ici il met les valeurs en majuscules et ajoute des crochets [ ] autour.
	5.	kStream.to()
➡️ Envoie les messages transformés au topic demo-output-stream-topic.

🔥 Étapes de bout en bout : De l’envoi au traitement du message

	1.	Création des topics

docker exec kafka-1 kafka-topics --create --topic demo-stream-topic --bootstrap-server kafka-1:19091 --partitions 1 --replication-factor 1
docker exec kafka-1 kafka-topics --create --topic demo-output-stream-topic --bootstrap-server kafka-1:19091 --partitions 1 --replication-factor 1


	2.	Envoi d’un message au topic source

docker exec -it kafka-1 kafka-console-producer --broker-list kafka-1:19091 --topic demo-stream-topic
> hello


	3.	Transformation par Kafka Streams
Le message est transformé par le flux hello → [HELLO].
	4.	Lecture dans le topic de sortie

docker exec -it kafka-1 kafka-console-consumer --bootstrap-server kafka-1:19091 --topic demo-output-stream-topic --from-beginning

Flux des données :

demo-stream-topic (input) ➡️ traitement (mapValues) ➡️ demo-output-stream-topic (output)


Les autres flux de la démos :

- Créer les topics 

kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic demo-stream-topic

kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic demo-output-stream-topic

kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic demo-stream-filter-topic


kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic demo-output-filter-topic

kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic demo-stream-map-topic


kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic demo-output-map-topic

kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic demo-stream-branch-topic


kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic demo-output-branch-topic-1

kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic demo-output-branch-topic-2

kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic demo-stream-aggregate-topic


kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic demo-output-aggregation-topic

kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic demo-stream-merge1-topic

kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic demo-stream-merge2-topic

kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic demo-output-merge-topic

kafka-topics --create \
    --bootstrap-server kafka-1:19091 \
    --replication-factor 1 \
    --partitions 1 \
    --topic other-demo-stream-topic


curl -X POST 'http://localhost:8080/api/messages/send/demo-stream' \
    -d 'key=user1' \
    -d 'value=hello world'
# Consommer les messages du flux kStream()
kafka-console-consumer --topic demo-output-stream-topic --from-beginning --bootstrap-server kafka-1:19091


curl -X POST 'http://localhost:8080/api/messages/send/demo-output-filter?key=user2&value=important update for the team'
# Consommer les messages du flux filterStream()
kafka-console-consumer --topic demo-output-filter-topic --from-beginning --bootstrap-server kafka-1:19091

curl -X POST 'http://localhost:8080/api/messages/send/demo-output-map?key=user3&value=hello world'
# Consommer les messages du flux mapValuesStream()
kafka-console-consumer --topic demo-output-map-topic --from-beginning --bootstrap-server kafka-1:19091


curl -X POST 'http://localhost:8080/api/messages/send/demo-output-branch-1?key=user4&value=error critical issue'

curl -X POST 'http://localhost:8080/api/messages/send/demo-output-branch-2?key=user4&value=info user has logged in'

# Consommer les messages des branches du flux branchStream()
kafka-console-consumer --topic demo-output-branch-topic-1 --from-beginning --bootstrap-server kafka-1:19091
kafka-console-consumer --topic demo-output-branch-topic-2 --from-beginning --bootstrap-server kafka-1:19091

curl -X POST 'http://localhost:8080/api/messages/send/demo-output-aggregation?key=user6&value=aggregation message'
# Consommer les messages du flux aggregateStream()
kafka-console-consumer --topic demo-output-aggregation-topic --from-beginning --bootstrap-server kafka-1:19091


curl -X POST 'http://localhost:8080/api/messages/send/demo-output-merge?key=user5&value=merge content from two streams'
# Consommer les messages du flux mergeStreams()
kafka-console-consumer --topic demo-output-merge-topic --from-beginning --bootstrap-server kafka-1:19091