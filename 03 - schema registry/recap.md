# Utilisation du Schema Registry avec Kafka

Le **Schema Registry** de Kafka permet de gérer des schémas pour les données transmises via Apache Kafka. Il prend en charge plusieurs formats de schémas, notamment **Avro**, **JSON Schema** et **Protobuf**, et garantit la compatibilité entre les producteurs et les consommateurs.

Ce document explique comment interagir avec le Schema Registry en utilisant des commandes `curl` pour les trois formats de schéma pris en charge.

---

## Pré-requis
1. **Kafka** et le **Schema Registry** doivent être installés et en cours d'exécution.
2. Par défaut, le Schema Registry est accessible sur `http://localhost:8081`.

## Commandes pour interagir avec le Schema Registry

### 1. Enregistrement d'un schéma

#### a) Schéma Avro
Pour enregistrer un schéma Avro, utilisez la commande suivante :

```bash
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
    --data '{
        "schema": "{\"type\":\"record\",\"name\":\"User\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"age\",\"type\":\"int\"}]}"
    }' \
    http://localhost:8081/subjects/my-topicavro-value/versions
```

- **URL** : `http://localhost:8081/subjects/my-topicavro-value/versions`
  - Remplacez `my-topicavro-value` par le nom de votre sujet Kafka.
- **Corps de la requête** :
  - Le champ `schema` contient une définition Avro JSON valide.

#### b) JSON Schema
Pour enregistrer un schéma JSON Schema, utilisez la commande suivante :

```bash
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
    --data '{
        "schema": "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"}},\"required\":[\"name\",\"age\"]}"
    }' \
    http://localhost:8081/subjects/my-topicjson-value/versions
```

- **URL** : `http://localhost:8081/subjects/my-topicjson-value/versions`
  - Remplacez `my-topicjson-value` par le nom de votre sujet Kafka.
- **Corps de la requête** :
  - Le champ `schema` contient un JSON Schema valide.

#### c) Protobuf
Pour enregistrer un schéma Protobuf, utilisez la commande suivante :

```bash
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
    --data '{
        "schema": "syntax = \"proto3\"; message User { string name = 1; int32 age = 2; }"
    }' \
    http://localhost:8081/subjects/my-topicproto-value/versions
```

- **URL** : `http://localhost:8081/subjects/my-topicproto-value/versions`
  - Remplacez `my-topicproto-value` par le nom de votre sujet Kafka.
- **Corps de la requête** :
  - Le champ `schema` contient une définition Protobuf valide.

---

### 2. Consultation des schémas

#### a) Récupérer la dernière version d'un schéma
```bash
curl -X GET \
    http://localhost:8081/subjects/my-topicavro-value/versions/latest
```

- **URL** : `http://localhost:8081/subjects/{subject}/versions/latest`
  - Remplacez `{subject}` par le sujet correspondant.

#### b) Récupérer une version spécifique
```bash
curl -X GET \
    http://localhost:8081/subjects/my-topicavro-value/versions/1
```

- **URL** : `http://localhost:8081/subjects/{subject}/versions/{version}`

---

### 3. Supprimer un schéma
Pour supprimer toutes les versions d'un sujet spécifique :

```bash
curl -X DELETE \
    http://localhost:8081/subjects/my-topicavro-value
```

Pour supprimer une version spécifique :

```bash
curl -X DELETE \
    http://localhost:8081/subjects/my-topicavro-value/versions/1
```

---

### 4. Tester la compatibilité d'un schéma
Avant d'enregistrer un nouveau schéma, vous pouvez vérifier sa compatibilité avec les schémas existants :

```bash
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
    --data '{
        "schema": "{\"type\":\"record\",\"name\":\"User\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}"
    }' \
    http://localhost:8081/compatibility/subjects/my-topicavro-value/versions/latest
```

- **URL** : `http://localhost:8081/compatibility/subjects/{subject}/versions/latest`
- **Corps de la requête** :
  - Le champ `schema` contient le schéma à tester.

---

### 5. Liste des sujets
Pour récupérer la liste de tous les sujets enregistrés :

```bash
curl -X GET \
    http://localhost:8081/subjects
```

---

## Notes importantes
- Les noms de sujet suivent généralement la convention `{topic-name}-{key|value}`.
- La compatibilité des schémas (backward, forward, full) est configurable pour chaque sujet.
- Assurez-vous que les schémas sont valides avant de les soumettre au Schema Registry.
