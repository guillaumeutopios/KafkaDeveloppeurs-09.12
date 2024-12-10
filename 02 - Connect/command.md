## Démo connecteur avec base de données JDBC.

- Démarrer le docker compose dans le dossier kafka-docker-composer.
- Vérifier que le connecteur est bien installer.
    - Normalement oui ils sont déjà dans le dossier volumes/connect-plugin-jars monté comme volume avec docker.
    - Dans le cadre ou ce n'est pas le cas, on peut installer le connecteur:
        - Soit manuellement, en téléchargeant le jar.
        - Soit en utilisant le repos public et la commande suivante, par exemple pour jdbc 'confluent-hub install confluentinc/kafka-connect-jdbc:latest'
- Voir la liste des plugins installés en visitant la page: 
    - http://localhost:8083/connector-plugins
- Voir la liste des connecteurs:
    - http://localhost:8083/connectors
    - C'est normale au début, il n'y a pas de connecteurs.



- Créer un conteneur postgres, avec une base de données postgres pour tester le connecteur.
    - Commande pour démarrer un conteneur docker
        - docker run --name postgres-container -e POSTGRES_PASSWORD=yourpassword -p 5432:5432 -d --network kafka-docker-composer_default postgres
    - Connecter le conteneur postgres au réseau avec les conteneurs kafka, sinon ça ne marchera pas
        - docker network connect postgres-container kafka-docker-composer_default 
    - Créer la table de démos "data_demo".
        - Entrer dans le conteneur
            - docker exec -it postgres-container psql -U postgres -d postgres
            - CREATE TABLE data_demo (id SERIAL PRIMARY KEY, name VARCHAR(255),value INTEGER);
              - INSERT INTO data_demo (id, name, value) VALUES (1, 'Test Data', 100);
                INSERT INTO data_demo (id, name, value) VALUES (2, 'More Data', 200);
                INSERT INTO data_demo (name, value) VALUES ('Test Data', 100);
            
- Créer le connecteur en envoyant un fichier json qui décrit le connecteur.
    - Le plugin
    - Le nom
    - Les informations de connection.
    - Les informations de topic.
- Chaque connecteur possède ses propres caractéristiques, il faut se référer à la documentation.

- Envoyer l'exemple connector-postgres.json vers l'api des connectors.
    - on peut le faire avec un post en curl, postman, api rest
    - curl -X POST -H "Content-Type: application/json" \
     --data @connector-postgres.json \
     http://localhost:8083/connectors

    - Vérifier que le connecteur existe:
        -  curl -X POST -H "Content-Type: application/json" \
     --data @connector-postgres.json \
     http://localhost:8083/connectors

- Utiliser un consumer démo avec lecture du topic spécifier dans la configuration du connecteur:
- Revenir à la base de données et inserer des données et les visualiser dans le consumers.