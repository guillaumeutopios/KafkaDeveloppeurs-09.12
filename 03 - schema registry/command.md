1. Définir un schéma
Un schéma peut être écrit en **Avro**, **JSON Schema**, ou **Protobuf**. 


Avro
- **Content-Type** : `application/vnd.schemaregistry.v1+json`
- Exemple de commande pour un schéma Avro :
  ```bash
  curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
      --data '{
          "schema": "{\"type\":\"record\",\"name\":\"User\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"age\",\"type\":\"int\"}]}"
      }' \
      http://localhost:8081/subjects/my-topicavro-value/versions
  ```

#### **JSON Schema**
- **Content-Type** : `application/vnd.schemaregistry.v1+json`
- Exemple de schéma JSON :
  ```json
  {
    "$schema": "http://json-schema.org/draft-07/schema#",
    "type": "object",
    "title": "User",
    "properties": {
      "name": { "type": "string" },
      "age": { "type": "integer" }
    },
    "required": ["name", "age"]
  }
  ```
- Commande pour enregistrer un schéma JSON :
  ```bash
  curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
      --data '{
          "schemaType": "JSON",
          "schema": "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"title\":\"User\",\"properties\":{\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"}},\"required\":[\"name\",\"age\"]}"
      }' \
      http://localhost:8081/subjects/my-topicjson-value/versions
  ```

#### **Protobuf**
- **Content-Type** : `application/vnd.schemaregistry.v1+json`
- Exemple de schéma Protobuf :
  ```protobuf
  syntax = "proto3";

  message User {
    string name = 1;
    int32 age = 2;
  }
  ```
- Commande pour enregistrer un schéma Protobuf :
  ```bash
  curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
      --data '{
          "schemaType": "PROTOBUF",
          "schema": "syntax = \"proto3\"; message User { string name = 1; int32 age = 2; }"
      }' \
      http://localhost:8081/subjects/my-topicproto-value/versions
  ```


Lors de l'utilisation de l'API REST pour enregistrer un schéma, voici les champs importants :
- **schema** : Le schéma, encodé sous forme de chaîne JSON.
- **schemaType** : (Optionnel) Le type du schéma :
  - Omittez ce champ pour **Avro** (c'est le type par défaut).
  - Utilisez `"JSON"` pour les schémas JSON.
  - Utilisez `"PROTOBUF"` pour les schémas Protobuf.

---

### **3. Validation du type dans Schema Registry**

Pour vérifier quel type de schéma est enregistré pour un sujet donné :
```bash
curl -X GET http://localhost:8081/subjects/my-topic-value/versions/1
```

Réponse typique pour un schéma Avro :
```json
{
  "subject": "my-topic-value",
  "version": 1,
  "id": 1,
  "schema": "{\"type\":\"record\",\"name\":\"User\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"age\",\"type\":\"int\"}]}",
  "schemaType": null
}
```

Réponse typique pour un schéma JSON :
```json
{
  "subject": "my-topic-value",
  "version": 1,
  "id": 2,
  "schema": "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"title\":\"User\",\"properties\":{\"name\":{\"type\":\"string\"},\"age\":{\"type\":\"integer\"}},\"required\":[\"name\",\"age\"]}",
  "schemaType": "JSON"
}
```


Gestion automatique du type avec les Producers

Lorsque vous utilisez un **Kafka Producer** avec des serializers spécifiques comme `KafkaAvroSerializer` ou `KafkaJsonSchemaSerializer`, le type de schéma est automatiquement géré :
- **Avro** : Pas besoin de spécifier explicitement le type, c'est le comportement par défaut.
- **JSON Schema** : Configurez `KafkaJsonSchemaSerializer` pour gérer les schémas JSON.
- **Protobuf** : Configurez `KafkaProtobufSerializer`.



**Enregistrement automatique via Kafka Producer**
Lorsque vous utilisez un **Kafka Producer** avec un sérialiseur comme `KafkaAvroSerializer`, le schéma est automatiquement enregistré dans le Schema Registry lors de l'envoi du premier message.

Exemple :
```java
props.put("value.serializer", KafkaAvroSerializer.class.getName());
props.put("schema.registry.url", "http://localhost:8081");
```
Le **KafkaAvroSerializer** vérifie si le schéma existe dans le **Schema Registry** :
- S'il existe, il l'utilise pour sérialiser les données.
- S'il n'existe pas, il enregistre automatiquement le schéma.

---

### **Vérification des schémas enregistrés**

Vous pouvez lister les sujets avec leurs schémas associés :
```bash
curl -X GET http://localhost:8081/subjects
```

Pour voir les versions d'un schéma associé à un sujet :
```bash
curl -X GET http://localhost:8081/subjects/my-topic-value/versions
```

Pour récupérer un schéma particulier :
```bash
curl -X GET http://localhost:8081/subjects/my-topic-value/versions/1
```

---

### **Compatibilité des schémas**

Lors de l'enregistrement d'un nouveau schéma, le **Schema Registry** vérifie la compatibilité avec les schémas précédents selon la politique définie :
- **Backward** (par défaut) : Les consommateurs utilisant des versions plus anciennes du schéma peuvent toujours lire les données produites.
- **Forward** : Les consommateurs utilisant des versions plus récentes peuvent lire les anciennes données.
- **Full** : Assure la compatibilité dans les deux sens.

Configurer la compatibilité pour un sujet :
```bash
curl -X PUT -H "Content-Type: application/vnd.schemaregistry.v1+json" \
    --data '{"compatibility": "BACKWARD"}' \
    http://localhost:8081/config/my-topic-value
```

---

### **Avantages de l'enregistrement**
1. **Validation des messages** : Les messages produits sont conformes au schéma.
2. **Interopérabilité** : Assure que les producteurs et consommateurs utilisent un format compatible.
3. **Gestion des versions** : Historique des versions des schémas pour chaque sujet.





**`DataDemo`** est une classe générée automatiquement à partir d’un schéma Avro (fichier `.avsc`). Cette classe est utilisée comme un type spécifique pour représenter les messages Avro dans vos producteurs et consommateurs.

Voici comment générer cette classe et l'utiliser :

---

### **1. Définir un Schéma Avro**

Créez un fichier **`DataDemo.avsc`** qui décrit la structure de vos messages. Exemple :

```json
{
  "type": "record",
  "name": "DataDemo",
  "fields": [
    { "name": "id", "type": "int" },
    { "name": "valeur", "type": "int" }
  ]
}
```

---

### **2. Générer la Classe Java**

#### Ajouter le Plugin Avro à `pom.xml`

Ajoutez le plugin Maven pour générer automatiquement les classes Java à partir des fichiers `.avsc` :

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.avro</groupId>
            <artifactId>avro-maven-plugin</artifactId>
            <version>1.11.1</version> <!-- Version compatible avec votre projet -->
            <executions>
                <execution>
                    <phase>generate-sources</phase>
                    <goals>
                        <goal>schema</goal>
                    </goals>
                    <configuration>
                        <sourceDirectory>${project.basedir}/src/main/resources/avro</sourceDirectory>
                        <outputDirectory>${project.basedir}/target/generated-sources/avro</outputDirectory>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

#### Placer le fichier `.avsc` dans le bon dossier

Créez le dossier suivant et placez-y votre fichier `DataDemo.avsc` :

```
src/main/resources/avro/DataDemo.avsc
```

#### Générer la Classe

Exécutez la commande Maven suivante pour générer la classe Java :

```bash
mvn clean compile
```

Cela génère la classe `DataDemo` dans le dossier suivant :

```
target/generated-sources/avro/DataDemo.java
```

---

### **3. Ajouter le Répertoire Généré au Classpath**

Assurez-vous que le répertoire contenant les classes générées est inclus dans votre classpath.

Dans IntelliJ IDEA, faites un clic droit sur le dossier **`target/generated-sources/avro`**, puis sélectionnez **"Mark Directory as > Generated Sources Root"**.

---

### **4. Utiliser `DataDemo` dans Votre Code**

Vous pouvez maintenant utiliser la classe `DataDemo` comme un type spécifique dans votre producteur et votre consommateur.

#### Exemple dans le Producteur :
```java
import com.example.generated.DataDemo; // Chemin de la classe générée

@PostMapping("/sendAvroMessage")
public String sendAvroMessage(@RequestParam Integer id, @RequestParam Integer valeur) {
    // Créer une instance de DataDemo
    DataDemo message = DataDemo.newBuilder()
                               .setId(id)
                               .setValeur(valeur)
                               .build();

    // Envoyer le message
    kafkaTemplate.send("postgressink-data_demo", message);
    return "Message sent: " + message;
}
```

#### Exemple dans le Consommateur :
```java
import com.example.generated.DataDemo;

@KafkaListener(topics = "postgressink-data_demo", groupId = "avro-consumer-group")
public void consume(DataDemo message) {
    System.out.println("Received Avro message: " + message);
}
```

---

### **5. Avantages des Classes Générées**

1. **Validation Automatique :** Les classes générées garantissent que vos messages respectent le schéma défini.
2. **Interopérabilité :** Les classes générées peuvent être utilisées à la fois par les producteurs et les consommateurs.
3. **Maintenance Simplifiée :** Les modifications dans le fichier `.avsc` sont automatiquement propagées dans la classe générée.

---

### **6. Résolution des Problèmes**

- **Classe non générée :** Assurez-vous que le chemin vers le fichier `.avsc` est correct dans le plugin Maven.
- **Import incorrect :** Vérifiez que l’import de la classe générée correspond à son emplacement dans le package.
