### **1. Création d’un topic**
**Commande :**
```bash
kafka-topics.sh --create
```
**Options importantes :**
- `--bootstrap-server <host:port>` : Adresse du broker Kafka.
- `--topic <topic-name>` : Nom du topic à créer.
- `--partitions <number>` : Nombre de partitions pour le topic.
- `--replication-factor <number>` : Facteur de réplication.
- `--config <key=value>` : Configurations spécifiques au topic (par exemple, durée de rétention `retention.ms`).

**Exemple :**
```bash
kafka-topics.sh --create --bootstrap-server localhost:9092 --topic my_topic --partitions 3 --replication-factor 2
```

---

### **2. Liste des topics**
**Commande :**
```bash
kafka-topics.sh --list
```
**Options importantes :**
- `--bootstrap-server <host:port>` : Adresse du broker.

**Exemple :**
```bash
kafka-topics.sh --list --bootstrap-server localhost:9092
```

---

### **3. Décrire un topic**
**Commande :**
```bash
kafka-topics.sh --describe
```
**Options importantes :**
- `--bootstrap-server <host:port>` : Adresse du broker.
- `--topic <topic-name>` : Nom du topic à décrire.

**Exemple :**
```bash
kafka-topics.sh --describe --bootstrap-server localhost:9092 --topic my_topic
```

---

### **4. Suppression d’un topic**
**Commande :**
```bash
kafka-topics.sh --delete
```
**Options importantes :**
- `--bootstrap-server <host:port>` : Adresse du broker.
- `--topic <topic-name>` : Nom du topic à supprimer.

**Exemple :**
```bash
kafka-topics.sh --delete --bootstrap-server localhost:9092 --topic my_topic
```
> ⚠️ La suppression doit être activée dans la configuration du cluster (`delete.topic.enable=true`).

---

### **5. Envoi de messages (producteur)**
**Commande :**
```bash
kafka-console-producer.sh
```
**Options importantes :**
- `--bootstrap-server <host:port>` : Adresse du broker.
- `--topic <topic-name>` : Nom du topic où envoyer les messages.
- `--property "key.separator=<separator>"` : Définit un séparateur pour les clés et les valeurs (utile si les messages ont des clés).
- `--property "parse.key=true"` : Active la prise en charge des clés dans les messages.

**Exemple :**
```bash
kafka-console-producer.sh --bootstrap-server localhost:9092 --topic my_topic
```

---

### **6. Lecture de messages (consommateur)**
**Commande :**
```bash
kafka-console-consumer.sh
```
**Options importantes :**
- `--bootstrap-server <host:port>` : Adresse du broker.
- `--topic <topic-name>` : Nom du topic à lire.
- `--from-beginning` : Lit tous les messages depuis le début du topic.
- `--group <group-id>` : Identifiant du groupe de consommateurs.

**Exemple :**
```bash
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my_topic --from-beginning
```

---

### **7. Vérification de l’état des consommateurs**
**Commande :**
```bash
kafka-consumer-groups.sh
```
**Options importantes :**
- `--bootstrap-server <host:port>` : Adresse du broker.
- `--list` : Liste tous les groupes de consommateurs.
- `--describe --group <group-id>` : Affiche les détails d’un groupe de consommateurs (par exemple, l’offset consommé).

**Exemple :**
```bash
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group my_group
```

---

### **8. Vérification des offsets d’un topic**
**Commande :**
```bash
kafka-run-class.sh kafka.tools.GetOffsetShell
```
**Options importantes :**
- `--broker-list <host:port>` : Liste des brokers Kafka.
- `--topic <topic-name>` : Nom du topic.
- `--time <timestamp>` : Spécifie les offsets à récupérer (`-1` pour le dernier offset, `-2` pour le premier offset).

**Exemple :**
```bash
kafka-run-class.sh kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic my_topic --time -1
```

---

### **9. Chargement des messages depuis un fichier**
**Commande :**
```bash
kafka-console-producer.sh
```
**Option importante :**
- `--bootstrap-server <host:port>` : Adresse du broker.
- `--topic <topic-name>` : Nom du topic.
- Redirection depuis un fichier en entrée (`< file-name`).

**Exemple :**
```bash
kafka-console-producer.sh --bootstrap-server localhost:9092 --topic my_topic < messages.txt
```

---

### **10. Tests de performances (producteur et consommateur)**
**Commande :**
```bash
kafka-producer-perf-test.sh
kafka-consumer-perf-test.sh
```
**Options importantes :**
- `--topic <topic-name>` : Nom du topic.
- `--num-records <number>` : Nombre de messages à envoyer ou lire.
- `--record-size <bytes>` : Taille des messages (producteur).
- `--throughput <number>` : Messages par seconde à envoyer.
- `--consumer.config <file>` : Fichier de configuration pour le consommateur.

**Exemple (producteur) :**
```bash
kafka-producer-perf-test.sh --topic my_topic --num-records 1000 --record-size 100 --throughput 500 --bootstrap-server localhost:9092
```

---

### **11. Vérification de la configuration des brokers**
**Commande :**
```bash
kafka-configs.sh --describe
```
**Options importantes :**
- `--bootstrap-server <host:port>` : Adresse du broker.
- `--entity-type <type>` : Type d’entité (topic, broker, client, etc.).
- `--entity-name <name>` : Nom de l’entité.

**Exemple :**
```bash
kafka-configs.sh --describe --bootstrap-server localhost:9092 --entity-type topics --entity-name my_topic
```
