
- Kafka
  - 2009, java + scala, 
    initialement dev par LinkedIn lorsqu'ils avaient de nombreux pipelines de données dans leur archi microservices => unifier
    dev actuellement par l'Apache Software Foundation (open 2011)
    société confluent créée par l'équipe de base => documentation
  - Event Streaming => déclenchement par événements
  - Message-oriented middleware
  - multiplateforme
  - idéal pour :
    - microservices
    - les application avec du streaming data (flux continu de donnée) 
      ex: géolocalisation, audio, video, analyse en temps réel
    - event driven
    - data
    - iot (temps réel)
  - similaire aux message broker classique mais :
    - gère plus de capacité/débit => résilient et rapide (moins que Red Panda, Kafka recodé en c++)
    - fault tolerant grace à ses mécanismes
    - mécanismes : 
      répartition sur plusieurs brokers, 
      réplication des partitions, 
      niveaux de confirmation (acks),
      gestion du cluster, ...
    - via commitlog (bus de message) 
      => possibilité de déplacer un offset plus tôt/tard pour chaque consumer
      les messages ne sont pas détruits
      rejouabilité des messages par un consumer
- Record == Message ~= Event
  - data produite par un producer suite à un évènement de logique métier
  - stocké dans un topic, sur une partition
  - Composé de : key, value, offset, timestamp, headers/metadata optionnel
    - Key => quelle partition d'un topic ce message sera stocké, si null => selon la politique de répartition : round-robin par défaut
    - Value => contenu/corps du message : peut être un texte brut, du JSON, de l'Avro, du Protobuf, etc.
    - offset => un identifiant unique (un entier croissant) attribué à chaque message dans une partition. 
    - timestamp => Soit l'heure à laquelle le producteur a créé le message ou le broker a reçu le message.
    - headers => en-têtes/métadonnées sous forme de paires clé-valeur ex :  "Content-Type: application/json"
- Topic 
  - queue de message partitionnée et sans suppression des messages
  - ensemble de records dans un ordre immutable sous forme de journal/log (commit log)
  - peut être persistant ou disparaître si inutile, peut être créé automatiquement à l'ajout de message
  - distribués et répliqués dans un Cluster
  - chaque consumer peut déplacer un offset plus tôt/tard pour indiquer où il en est
- Partition
  - fragmentation logique des topics en morceaux
  - permet de distribuer sur l'ensemble des brokers d'un même cluster en les conservant dans le même topic
  - important pour la performance et la scalabilité
  - clé de partition pour savoir dans quelle partition stocker les messages (via une fonction de hachage) 
  - divisée en segments : regroupement de messages pour le stockage physique de 1 GB par défaut
  - répliquées sur les différents brokers pour de la haute disponibilité, une est leader et d'autres sont followers
- Cluster
  - regroupement de Brokers
- Broker == server kafka (machine) => unité d'exécution 
  - dans le cluster
  - augmenter leur nombre facilite la scalabilité
  - contiennent les topics de manière répliquées
  - principe de Quorum (participants élection), toujours en nombre impair pour la réplication des partitions en leader/followers
    si nouvelle partition ou partition leader supprimée, où mettre la leader et les répliquée ? 
    => élection pour choisir qui a la leader selon le principe de Quorum
- pubsub 
  - publish and subscribe de messages => producteurs et consommateurs
- Producer
  - applications/services qui produisent les messages d'un ou plusieurs topics
  - config acks :
    - acks=0 : Aucun accusé de réception 
    - acks=1 : Accusé de réception d’un broker 
    - acks=all (ou acks=-1) : Accusé de réception de toutes les répliques 
- Consumers
  - applications/services qui consument les messages d'un ou plusieurs topics
  - peuvent lire un par un ou tout d'un coup, possible d'écouter en temps réel
  - ont chacun un offset de lecture (commitlog) pour savoir où ils en sont 
    il permet aussi de savoir si on commence par les messages ancien ou récent
  - quand ils sont plusieurs pour un même topic regroupées dans des consumer groups 
  - 2 modes :
    - subscribe : utilise un topic, utilisation d'un consumer group
    - assign : utilise une partition unique d'un topic, pas de consumer group, cas avancé
- Consumer Groups 
  - répartissent équitablement les demandes des consumers, 
  - via un broker coordinator, qui gère l'offset
  - permet de s'assurer qu'un seul broker consume à la fois un message
  - consommation équilibrée, haute disponibilité
  - Rééquilibrage Automatique : Si un nouveau consommateur rejoint le groupe ou si un consommateur existant quitte le groupe ou échoue
- Zookeeper / KRaft
  - manager de cluster
  - Zookeeper => au moins 3 machine pour son propre cluster qui permet de gérer le cluster kafka
    cas d'utilisation plus généraux : service registry, stockage de configurations, cluster management, gestions de locks et concurrences
  - KRaft => récent, simplifie et remplace Zookeeper