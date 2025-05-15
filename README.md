# Configuration du projet MongoDB 
Shahzad Abdul rahman, Amrani Ayoub, Ndoye Papa Abdoulaye
## Prérequis

- Docker
- Docker Compose

## 1. Lancer les conteneurs Docker

Pour démarrer les conteneurs , utilisez la commande suivante pour démarrer les conteneurs :

```bash
docker-compose up -d

docker ps
```


## 2. Initialiser le Replica Set Europe

Exécutez la commande suivante pour initialiser le replica set Europe:
```bash
docker exec -it mongo_europe_primary mongosh --eval 
```
```bash
rs.initiate({
  _id: 'europe_rs', 
  members: 
      [
          {_id: 0, host: 'mongo_europe_primary:27017', priority: 2 }, 
          {_id: 1, host: 'mongo_europe_replica:27017', priority: 1 }
      ]
  })
```

## 3. Initialiser le Replica Set Asie

Exécutez les commandes suivantes pour initialiser le replica set Asie :
```bash
docker exec -it mongo_asia_primary mongosh --eval
```
```bash
rs.initiate({
  _id: 'asia_rs', 
  members: 
      [
          {_id: 0, host: 'mongo_asia_primary:27017', priority: 2 }, 
          {_id: 1, host: 'mongo_asia_replica:27017', priority: 1 }
      ]
  })
```

## 4. Initialiser le Replica Set Global

Exécutez les commandes suivantes pour initialiser le replica set Global :
```bash
docker exec -it mongo_global_primary mongosh --eval
```
```bash

rs.initiate({
  _id: 'global_rs', 
  members: 
      [
          {_id: 0, host: 'mongo_global_primary:27017', priority: 2 }, 
          {_id: 1, host: 'mongo_global_replica:27017', priority: 1 }
      ]
  })
  
```

## 5. Verifier le status
Vérifiez l'état des mongos 
```bash
rs.status()
```

## 10. Resultat
Vous pouvez maintenant vous connectez à vos bases de données avec MongoDB Compass.
Important : comme les instances MongoDB fonctionnent dans des conteneurs Docker avec des Replica Sets, il est nécessaire d’ajouter ``` ?directConnection=true ``` à l’URL de connexion.



Car sur Windows chaque conteneur n’est pas directement accessible comme une machine indépendante, ce qui empêche la résolution automatique des membres du replica set. 
En ajoutant ```directConnection=true ``` ça permet de cibler uniquement le nœud principal sans chercher à contacter les autres membres du replica set.

Ou

Vous pouvez également importer le fichier compass-connections.json  ``` compass-connections.json ``` , pour avoir toutes les URLS de connexion sur mongoDbCompass  )

<img width="515" alt="image" src="https://github.com/user-attachments/assets/fd3ca0e6-4568-4ed1-b597-df1dc4fc64c7" />

```
exemple pour asie : 

mongodb://localhost:27021/?directConnection=true"
```

## 11. PostMan
Sur postman importer le fichier " API_projetmongodb_.json "
Testez les différents Get Put Post et delete

<img width="233" alt="image" src="https://github.com/user-attachments/assets/ea6c65f0-f01c-43fa-8311-b3e792e297af" />

Fermer un container et effetuer des actions, vous verrez le failover automatique fonctionne bien 

<img width="710" alt="image" src="https://github.com/user-attachments/assets/9b6cd2c1-1e2c-40ce-a677-6205a86c49ff" />


## 12. Conclusion 

Dans ce projet, nous avons alors choisis de faire une répartition personnalisée des données en fonction du pays.
Nous avons créer les bases de données en 3 replica sets (europe_rs, asia_rs, global_rs) et chaque replica est composé d’un nœud principal et d’un nœud secondaire, permettant d'assurer la haute disponibilité grâce au de failover de MongoDB.

Cela nous permet d'avoir une organisation qui :

améliore les performances : les lectures et écritures sont envoyées uniquement vers les instances concernées, donc réduit la charge sur chaque base.
respecte la localisation : les données européennes restent sur les serveurs Europe, grâce au code.
simplifie la maintenance : en gardant une structure distinct claire par région.
maintient la disponibilité : grâce au failover, si une base de donnée tombe l'autre prend le relais

(Vous pouvez également voir comment fonctionne le sharding automatique proposer par mongoDB en choisisant la branch1 (master), il faut alors à nouveau suivre le ReadMe qui s'y trouve) 
