docker exec <id_container_kafka> kafka-topics.sh --create --topic notifications --partitions 2 --replication-factor 1 --bootstrap-server localhost:9092
