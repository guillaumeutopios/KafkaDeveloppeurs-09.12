- Meme config que la démo:
  - Se connecter à la base de données pour voir les données (table data_destination)
  - Créer la table une fois connecter au conteneur 
    - docker exec -it postgres-container psql -U postgres -d postgres
    - CREATE TABLE data_destination (
      id SERIAL PRIMARY KEY,
      valeur INTEGER
      );