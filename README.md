# Configuration du projet MongoDB 

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
Après avoir executer des Posts , connectez vous à vos bdd avec mongoDBCOMPASS :
(Important : En cas d'erreur essayer d'ajouter "?directConnection=true" à l'url de connexion)
```
exemple pour asie : 

mongodb://localhost:27021/?directConnection=true"
```


