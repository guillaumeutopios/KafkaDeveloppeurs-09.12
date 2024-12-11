### **1. UDF (User Defined Function)**

Les **UDFs** sont utilisées pour transformer des données dans une seule ligne. Elles prennent une ou plusieurs valeurs en entrée et retournent une seule valeur.

#### **Étapes pour créer une UDF :**

1. **Créer une classe Java :**
   - Implémentez une méthode annotée avec `@Udf` pour définir la fonction.
   - Ajoutez l’annotation `@UdfDescription` pour documenter la fonction.

2. **Exemple :**
   Voici un exemple de fonction UDF qui met une chaîne de caractères en majuscules.

   ```java
   import io.confluent.ksql.function.Udf;
   import io.confluent.ksql.function.UdfDescription;

   @UdfDescription(
       name = "to_upper",
       description = "Converts a string to uppercase.",
       version = "1.0.0"
   )
   public class ToUpperUdf {

       @Udf(description = "Converts a string to uppercase")
       public String toUpper(final String input) {
           if (input == null) {
               return null;
           }
           return input.toUpperCase();
       }
   }
   ```

3. **Compilation et Packaging :**
   - Compilez la classe en un fichier `.jar`.
   - Ajoutez toutes les dépendances nécessaires dans le fichier `.jar`.

4. **Déploiement dans ksqlDB :**
   - Placez le fichier `.jar` dans le répertoire `ext` du serveur ksqlDB.
   - Redémarrez le serveur ksqlDB pour que la fonction soit chargée.

5. **Utilisation dans les requêtes :**
   Une fois déployée, vous pouvez utiliser la fonction dans vos requêtes SQL :
   ```sql
   SELECT to_upper(column_name) FROM stream_name;
   ```

---

### **2. UDAF (User Defined Aggregate Function)**

Les **UDAFs** sont utilisées pour effectuer des calculs sur un groupe de lignes, comme des agrégats personnalisés.

#### **Étapes pour créer une UDAF :**

1. **Créer une classe Java :**
   - Implémentez la fonction d'agrégation en utilisant les interfaces fournies par `io.confluent.ksql.function.udaf`.
   - Déclarez les étapes principales :
     - **Initialization** : initialise l'état de l'agrégat.
     - **Accumulation** : ajoute une valeur à l'état.
     - **Combinaison** : combine deux états (utile dans les scénarios distribués).
     - **Finalization** : retourne le résultat final de l'agrégat.

2. **Exemple :**
   Voici un exemple de UDAF qui calcule la somme des carrés des valeurs.

   ```java
   import io.confluent.ksql.function.udaf.Udaf;
   import io.confluent.ksql.function.udaf.UdafAggregator;
   import io.confluent.ksql.function.udaf.UdafDescription;
   import io.confluent.ksql.function.udaf.UdafFactory;

   @UdafDescription(
       name = "sum_of_squares",
       description = "Calculates the sum of squares of values.",
       version = "1.0.0"
   )
   public class SumOfSquaresUdaf {

       @UdafFactory(description = "Creates a sum of squares UDAF")
       public static Udaf<Long, Long, Long> create() {
           return UdafAggregator.of(
               0L, // Initial value
               (agg, val) -> agg + (val * val), // Accumulation step
               Long::sum // Combination step
           );
       }
   }
   ```

3. **Compilation et Packaging :**
   - Comme pour les UDFs, compilez la classe en un fichier `.jar`.
   - Ajoutez les dépendances nécessaires.

4. **Déploiement dans ksqlDB :**
   - Placez le fichier `.jar` dans le répertoire `ext` du serveur ksqlDB.
   - Redémarrez le serveur.

5. **Utilisation dans les requêtes :**
   Une fois la fonction déployée, utilisez-la dans des agrégats SQL :
   ```sql
   SELECT key_column, sum_of_squares(value_column)
   FROM stream_name
   GROUP BY key_column;
   ```

---

### **Différences entre UDF et UDAF**

| Aspect                 | UDF                                   | UDAF                                     |
|------------------------|---------------------------------------|------------------------------------------|
| **Utilisation**         | Transformations ligne par ligne      | Calculs sur des groupes de lignes        |
| **Input/Output**        | Une ou plusieurs valeurs → une valeur | Plusieurs valeurs → une valeur agrégée   |
| **Exemple**             | Conversion de texte                  | Somme, moyenne, ou autre agrégat personnalisé |
