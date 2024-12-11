### Utilisation de kafa Streams
## Exemple 1
1. Collecte des logs "TOPIC"
2. Traitement des logs "Stream"
3. Alert "TOPIC"

### Ajout d'un serveur ksqldb

```bash
docker run -d \
--name=ksqldb-server \
--network=docker-compose_default \
-e KSQL_LISTENERS=http://0.0.0.0:8088 \
-e KSQL_BOOTSTRAP_SERVERS=http://kafka-1:19091 \
-e KSQL_KSQL_SCHEMA_REGISTRY_URL=http://schema-registry-1:8081 \
-e KSQL_KSQL_LOGGING_PROCESSING_STREAM_AUTO_CREATE='true' \
-e KSQL_KSQL_LOGGING_PROCESSING_TOPIC_AUTO_CREATE='true' \
-p 8088:8088 \
confluentinc/cp-ksqldb-server:latest


docker run -d --name=ksqldb-server --network=docker-compose_default -e KSQL_LISTENERS=http://0.0.0.0:8088 -e KSQL_BOOTSTRAP_SERVERS=http://kafka-1:19091 -e KSQL_KSQL_SCHEMA_REGISTRY_URL=http://schema-registry-1:8081 -e KSQL_KSQL_LOGGING_PROCESSING_STREAM_AUTO_CREATE='true' -e KSQL_KSQL_LOGGING_PROCESSING_TOPIC_AUTO_CREATE='true' -p 8088:8088 confluentinc/cp-ksqldb-server:latest
```

### pour avoir un client à l'interieur du conteneur du serveur ksqldb
```bash
docker exec -it ksqldb-server ksql http://localhost:8088
```


⚙️ 1. Création des Streams

Nous allons d’abord créer les streams KSQL en utilisant les définitions fournies.

Créer le stream orders

CREATE STREAM orders (
  order_id INT,
  customer_id INT,
  product_id INT
) WITH (
  KAFKA_TOPIC='orders_topic',
  VALUE_FORMAT='JSON'
);

	Ce stream est mappé au topic Kafka orders_topic. Les messages JSON ont la structure suivante :

{
  "order_id": 1,
  "customer_id": 101,
  "product_id": 5001
}

Créer le stream customers

CREATE STREAM customers (
  customer_id INT,
  customer_name STRING
) WITH (
  KAFKA_TOPIC='customers_topic',
  VALUE_FORMAT='JSON'
);

	Ce stream est mappé au topic Kafka customers_topic. Les messages JSON ont la structure suivante :

{
  "customer_id": 101,
  "customer_name": "John Doe"
}

Créer le stream enriched_orders (stream join)

CREATE STREAM enriched_orders AS
SELECT 
  orders.order_id,
  orders.product_id,
  orders.customer_id,
  customers.customer_name
FROM orders
JOIN customers WITHIN 10 MINUTES
ON orders.customer_id = customers.customer_id;

	Ce stream combine les informations des topics orders_topic et customers_topic pour enrichir les commandes avec le nom du client. Le délai d’attente de 10 minutes garantit que les messages correspondants des deux flux sont combinés.

⚙️ 2. Test des flux et insertion des données

Nous allons maintenant insérer des messages dans les topics et observer le comportement de KSQL.

Étape 1: Insertion des messages dans le topic orders_topic

Vous pouvez insérer des messages de test directement depuis le CLI Kafka ou via une commande KSQL.

Via ksqlDB:

INSERT INTO orders (order_id, customer_id, product_id) VALUES (1, 101, 5001);
INSERT INTO orders (order_id, customer_id, product_id) VALUES (2, 102, 5002);
INSERT INTO orders (order_id, customer_id, product_id) VALUES (3, 101, 5003);

Via Kafka CLI:

kafka-console-producer --broker-list kafka-1:19091 --topic orders_topic <<EOF
{"order_id": 1, "customer_id": 101, "product_id": 5001}
{"order_id": 2, "customer_id": 102, "product_id": 5002}
{"order_id": 3, "customer_id": 101, "product_id": 5003}
EOF

	Les 3 messages sont envoyés au topic orders_topic.
		•	La première et la troisième commandes sont des commandes de l’utilisateur 101.
	•	La deuxième commande appartient au client 102.

Étape 2: Insertion des messages dans le topic customers_topic

Ensuite, nous insérons des informations clients dans le topic customers_topic.

Via ksqlDB:

INSERT INTO customers (customer_id, customer_name) VALUES (101, 'John Doe');
INSERT INTO customers (customer_id, customer_name) VALUES (102, 'Jane Smith');

Via Kafka CLI:

kafka-console-producer --broker-list kafka-1:19091 --topic customers_topic <<EOF
{"customer_id": 101, "customer_name": "John Doe"}
{"customer_id": 102, "customer_name": "Jane Smith"}
EOF

	Les 2 messages sont envoyés au topic customers_topic.
		•	John Doe est l’utilisateur 101.
	•	Jane Smith est l’utilisateur 102.

⚙️ 3. Observation des résultats du stream enrichi

Nous allons maintenant observer le flux enrichi pour voir les résultats de la jointure entre orders et customers.

Requête KSQL pour lire le flux enrichi

SELECT * FROM enriched_orders EMIT CHANGES;

	Ce que vous allez voir :

+------------------+------------------+----------------+------------------+
| ORDER_ID         | PRODUCT_ID       | CUSTOMER_ID    | CUSTOMER_NAME    |
+------------------+------------------+----------------+------------------+
| 1                | 5001             | 101            | John Doe         |
| 2                | 5002             | 102            | Jane Smith       |
| 3                | 5003             | 101            | John Doe         |
+------------------+------------------+----------------+------------------+

	Explications :

	•	La ligne 1 représente la commande order_id = 1 de John Doe.
	•	La ligne 2 représente la commande order_id = 2 de Jane Smith.
	•	La ligne 3 représente la commande order_id = 3 de John Doe (ce client a effectué deux commandes).

⚙️ 4. Vérification des données dans les topics

Si vous souhaitez vérifier les messages dans Kafka, vous pouvez le faire directement à l’aide de la ligne de commande Kafka.

Consulter le topic orders_topic

kafka-console-consumer --bootstrap-server kafka-1:19091 --topic orders_topic --from-beginning

	Exemple de sortie:

{"order_id": 1, "customer_id": 101, "product_id": 5001}
{"order_id": 2, "customer_id": 102, "product_id": 5002}
{"order_id": 3, "customer_id": 101, "product_id": 5003}

Consulter le topic customers_topic

kafka-console-consumer --bootstrap-server kafka-1:19091 --topic customers_topic --from-beginning

	Exemple de sortie:

{"customer_id": 101, "customer_name": "John Doe"}
{"customer_id": 102, "customer_name": "Jane Smith"}

Consulter le topic enriched_orders

kafka-console-consumer --bootstrap-server localhost:9092 --topic enriched_orders --from-beginning

	Exemple de sortie:

{"order_id": 1, "product_id": 5001, "customer_id": 101, "customer_name": "John Doe"}
{"order_id": 2, "product_id": 5002, "customer_id": 102, "customer_name": "Jane Smith"}
{"order_id": 3, "product_id": 5003, "customer_id": 101, "customer_name": "John Doe"}

	Vous pouvez constater que le flux enriched_orders a correctement enrichi chaque commande (order) avec le nom du client provenant du topic customers.

⚙️ 5. Explications de la jointure

Voici comment fonctionne la jointure entre les flux orders et customers.
	1.	Concept de la jointure
	•	La jointure est définie dans la requête KSQL :

CREATE STREAM enriched_orders AS
SELECT 
  orders.order_id,
  orders.product_id,
  orders.customer_id,
  customers.customer_name
FROM orders
JOIN customers WITHIN 10 MINUTES
ON orders.customer_id = customers.customer_id;


	2.	Qu’est-ce que WITHIN 10 MINUTES ?
	•	Cette clause signifie que les messages de orders et customers doivent arriver dans un intervalle de 10 minutes pour être joints.
	•	Si une commande arrive et qu’aucune correspondance n’est trouvée dans le flux customers, elle attendra jusqu’à 10 minutes avant d’abandonner la jointure.
	3.	Quels messages sont joints ?
	•	Les messages de orders sont joints avec les messages de customers sur le champ customer_id.
	•	Le résultat est un flux enrichi (enriched_orders).

🔥 Résumé des commandes utiles

	•	Créer les streams : CREATE STREAM ...
	•	Insérer des messages : INSERT INTO ...
	•	Visualiser les streams : SELECT * FROM ... EMIT CHANGES;
	•	Consulter les topics : kafka-console-consumer --bootstrap-server kafka-1:19091 --topic enriched_orders --from-beginning



