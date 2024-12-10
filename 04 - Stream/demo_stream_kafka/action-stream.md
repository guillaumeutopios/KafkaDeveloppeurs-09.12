📚 1. filter()

🛠️ Utilité :

Filtre les enregistrements dans le flux en fonction d’une condition logique.

📘 Exemple :

On garde uniquement les messages dont la valeur est supérieure à 10.

KStream<String, Integer> filteredStream = kStream.filter(
    (key, value) -> value > 10
);

📜 Explications :

	1.	(key, value) -> value > 10
➡️ Cette condition signifie “ne garder que les messages dont la valeur est supérieure à 10”.
	2.	Le flux filteredStream ne contient que les valeurs > 10.

📚 2. filterNot()

🛠️ Utilité :

Le contraire de filter(). Filtre les enregistrements qui ne satisfont pas une condition.

📘 Exemple :

On supprime tous les messages dont la valeur est vide (null ou "").

KStream<String, String> nonEmptyStream = kStream.filterNot(
    (key, value) -> value == null || value.isEmpty()
);

📜 Explications :

	1.	La condition value == null || value.isEmpty() filtre toutes les valeurs nulles ou vides.
	2.	Le flux nonEmptyStream ne contiendra que des valeurs non vides.

📚 3. map()

🛠️ Utilité :

Transforme les clés et les valeurs en utilisant une nouvelle fonction de mappage.

📘 Exemple :

On modifie la clé et la valeur du message.

KStream<String, String> mappedStream = kStream.map(
    (key, value) -> new KeyValue<>("new-key-" + key, "[" + value + "]")
);

📜 Explications :

	1.	La clé est modifiée en “new-key-” + key.
	2.	La valeur est mise entre crochets [ ].
	3.	L’opération retourne une nouvelle clé et une nouvelle valeur.

📚 4. mapValues()

🛠️ Utilité :

Transforme seulement la valeur (et pas la clé).

📘 Exemple :

On convertit toutes les valeurs en majuscules.

KStream<String, String> upperCaseStream = kStream.mapValues(
    value -> value.toUpperCase()
);

📜 Explications :

	1.	La valeur de chaque message est convertie en MAJUSCULES.
	2.	La clé reste inchangée.

📚 5. flatMap()

🛠️ Utilité :

Divise un message en plusieurs messages (comme un split).

📘 Exemple :

Chaque message est une phrase, et on la divise en mots.

KStream<String, String> flatMappedStream = kStream.flatMap(
    (key, value) -> {
        List<KeyValue<String, String>> words = new ArrayList<>();
        for (String word : value.split(" ")) {
            words.add(new KeyValue<>(key, word));
        }
        return words;
    }
);

📜 Explications :

	1.	Chaque message est une phrase (comme “Bonjour le monde”).
	2.	La phrase est divisée en mots, et chaque mot devient un nouveau message dans le flux.

📚 6. flatMapValues()

🛠️ Utilité :

Divise uniquement la valeur en plusieurs morceaux.

📘 Exemple :

On divise une phrase en mots.

KStream<String, String> wordStream = kStream.flatMapValues(
    value -> Arrays.asList(value.split(" "))
);

📜 Explications :

	1.	La valeur de chaque message est divisée en mots.
	2.	Chaque mot devient un nouveau message dans le flux.

📚 7. branch()

🛠️ Utilité :

Divise un flux en plusieurs flux basés sur des conditions.

📘 Exemple :

On divise le flux en deux flux, un pour les valeurs supérieures à 10 et un autre pour les valeurs inférieures ou égales.

KStream<String, Integer>[] branches = kStream.branch(
    (key, value) -> value > 10,  // 1er flux : valeurs > 10
    (key, value) -> value <= 10  // 2ème flux : valeurs <= 10
);
KStream<String, Integer> bigValues = branches[0];
KStream<String, Integer> smallValues = branches[1];

📜 Explications :

	1.	branch() permet de créer plusieurs flux à partir d’un seul flux.
	2.	Chaque branche respecte une condition.

📚 8. groupByKey()

🛠️ Utilité :

Regroupe les messages ayant la même clé. Cela permet de préparer une agrégation.

📘 Exemple :

On regroupe les messages par clé.

KGroupedStream<String, String> groupedStream = kStream.groupByKey();

📚 9. groupBy()

🛠️ Utilité :

Regroupe les messages par une nouvelle clé.

📘 Exemple :

On utilise la valeur comme nouvelle clé.

KGroupedStream<String, String> groupedByValueStream = kStream.groupBy(
    (key, value) -> value
);

📚 10. count()

🛠️ Utilité :

Compte le nombre de messages par clé.

📘 Exemple :

On compte le nombre de messages pour chaque clé.

KTable<String, Long> countTable = kStream
    .groupByKey()
    .count();

📚 11. reduce()

🛠️ Utilité :

Aggrège les valeurs par clé au fil du temps.

📘 Exemple :

On maintient la somme cumulative de toutes les valeurs par clé.

KTable<String, Integer> reducedTable = kStream
    .groupByKey()
    .reduce(
        (aggValue, newValue) -> aggValue + newValue
    );

📜 Explications :

	1.	Pour chaque nouvelle valeur de la clé, la valeur est ajoutée à la précédente.
	2.	Exemple : si la clé user1 reçoit les valeurs [5, 10, 15], la valeur agrégée sera 30.

📚 12. merge()

🛠️ Utilité :

Fusionne plusieurs flux en un seul.

📘 Exemple :

On fusionne deux flux.

KStream<String, String> mergedStream = stream1.merge(stream2);

📚 13. join()

🛠️ Utilité :

Joint deux flux de messages sur une clé commune.

📘 Exemple :

On joint deux flux basés sur la clé.

KStream<String, String> joinedStream = stream1.join(
    stream2,
    (value1, value2) -> value1 + " " + value2
);

📜 Explications :

	1.	Le flux stream1 est joint au flux stream2.
	2.	Les valeurs des deux flux sont combinées avec un espace entre elles.

📚 14. windowedBy()

🛠️ Utilité :

Crée des fenêtres temporelles sur les flux.

📘 Exemple :

On compte les messages par tranche de 1 minute.

KTable<Windowed<String>, Long> windowedCount = kStream
    .groupByKey()
    .windowedBy(TimeWindows.of(Duration.ofMinutes(1)))
    .count();

🎉 Résumé

Ces opérations permettent de :
	•	Filtrer (filter, filterNot),
	•	Transformer (map, mapValues, flatMap),
	•	Agréger (groupBy, reduce, count),
	•	Combiner (merge, join),
	•	Gérer le temps (windowedBy).
