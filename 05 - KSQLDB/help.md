### **1. Requêtes de base pour inspection et gestion**
- **Lister les flux disponibles :**
  ```sql
  SHOW STREAMS;
  ```

- **Lister les tables disponibles :**
  ```sql
  SHOW TABLES;
  ```

- **Voir les propriétés de la session :**
  ```sql
  SHOW PROPERTIES;
  ```

- **Lister les requêtes en cours :**
  ```sql
  SHOW QUERIES;
  ```

- **Arrêter une requête active :**
  ```sql
  TERMINATE <query_id>;
  ```

---

### **2. Création de flux et tables**
- **Créer un flux basé sur un sujet Kafka existant :**
  ```sql
  CREATE STREAM <stream_name> (
      column1 TYPE,
      column2 TYPE
  ) WITH (
      KAFKA_TOPIC = '<topic_name>',
      VALUE_FORMAT = 'JSON' -- ou AVRO, PROTOBUF, etc.
  );
  ```

- **Créer une table basée sur un sujet Kafka existant :**
  ```sql
  CREATE TABLE <table_name> (
      column1 TYPE PRIMARY KEY,
      column2 TYPE
  ) WITH (
      KAFKA_TOPIC = '<topic_name>',
      VALUE_FORMAT = 'JSON'
  );
  ```

---

### **3. Interrogations simples sur flux et tables**
- **Requête sur un flux (stateless) :**
  ```sql
  SELECT column1, column2
  FROM <stream_name>
  EMIT CHANGES;
  ```

- **Filtrer les messages d’un flux :**
  ```sql
  SELECT *
  FROM <stream_name>
  WHERE column1 = 'value'
  EMIT CHANGES;
  ```

- **Requête agrégée sur une table (stateful) :**
  ```sql
  SELECT column1, COUNT(*)
  FROM <table_name>
  GROUP BY column1
  EMIT CHANGES;
  ```

---

### **4. Transformation et création d'objets dérivés**
- **Créer un flux dérivé par transformation :**
  ```sql
  CREATE STREAM <derived_stream_name> AS
  SELECT column1, UCASE(column2) AS upper_column2
  FROM <stream_name>
  WHERE column3 > 100;
  ```

- **Créer une table dérivée par agrégation :**
  ```sql
  CREATE TABLE <derived_table_name> AS
  SELECT column1, COUNT(*) AS count
  FROM <stream_name>
  GROUP BY column1;
  ```

---

### **5. Jointures**
- **Jointure entre flux et table :**
  ```sql
  SELECT stream.column1, table.column2
  FROM <stream_name> stream
  JOIN <table_name> table
  ON stream.key = table.key
  EMIT CHANGES;
  ```

- **Jointure entre deux flux :**
  ```sql
  SELECT stream1.column1, stream2.column2
  FROM <stream1_name> stream1
  JOIN <stream2_name> stream2
  WITHIN 10 SECONDS
  ON stream1.key = stream2.key
  EMIT CHANGES;
  ```

---

### **6. Gestion des sujets Kafka**
- **Créer un sujet Kafka :**
  ```sql
  CREATE STREAM <stream_name> (
      column1 TYPE,
      column2 TYPE
  ) WITH (
      KAFKA_TOPIC = '<new_topic_name>',
      PARTITIONS = 3,
      REPLICAS = 2,
      VALUE_FORMAT = 'JSON'
  );
  ```

- **Insérer des données dans un sujet Kafka :**
  ```sql
  INSERT INTO <stream_name> (column1, column2)
  VALUES ('value1', 'value2');
  ```

---

### **7. Débogage**
- **Afficher les données brutes d'un flux :**
  ```sql
  PRINT '<topic_name>';
  ```

- **Observer les métadonnées d'un flux ou table :**
  ```sql
  DESCRIBE <stream_or_table_name>;
  ```

- **Inclure les types détaillés :**
  ```sql
  DESCRIBE EXTENDED <stream_or_table_name>;
  ```
