# Configuration du projet MongoDB 

## Prérequis

- Docker
- Docker Compose

## 1. Lancer les conteneurs Docker

Pour démarrer les services , utilisez la commande suivante pour démarrer les conteneurs :

```bash
docker-compose up -d

docker ps
```


## 2. Initialiser le Replica Set du Serveur de Configuration

Exécutez la commande suivante pour initialiser le replica set du serveur de configuration :
```bash
docker exec -it configsvr mongosh --port 27019
```
```bash
rs.initiate({
  _id: "configRS",
  configsvr: true,
  members: [
    { _id: 0, host: "configsvr:27019" }
  ]
})
```

## 3. Initialiser le Replica Set Europe

Exécutez les commandes suivantes pour initialiser le replica set Europe :
```bash
docker exec -it europe1 mongosh --port 27101
```
```bash
rs.initiate({
  _id: "europeRS",
  members: [
    { _id: 0, host: "europe1:27101" },
    { _id: 1, host: "europe2:27102" }
  ]
})
```

## 4. Initialiser le Replica Set Asie

Exécutez les commandes suivantes pour initialiser le replica set Asie :
```bash
docker exec -it asia1 mongosh --port 27201
```
```bash

rs.initiate({
  _id: "asiaRS",
  members: [
    { _id: 0, host: "asia1:27201" },
    { _id: 1, host: "asia2:27202" }
  ]
})

```

## 5. Initialiser le Replica Set Global

Exécutez les commandes suivantes pour initialiser le replica set Global :
```bash
docker exec -it global1 mongosh --port 27301
```
```bash
rs.initiate({
  _id: "globalRS",
  members: [
    { _id: 0, host: "global1:27301" },
    { _id: 1, host: "global2:27302" }
  ]
})
```
## 6. Se connecter à mongos et Configurer le Sharding

Configuré les replica sets, connectez-vous à l'instance mongos pour configurer le sharding :
```bash
docker exec -it mongosDbprojet mongosh --port 27020
```

```bash
use produitsdb
```
Ajoutez les shards :
```bash
sh.addShard("europeRS/europe1:27101,europe2:27102")
sh.addShard("asiaRS/asia1:27201,asia2:27202")
sh.addShard("globalRS/global1:27301,global2:27302")
```
Activez le shard : 
```bash
sh.enableSharding("produitsdb")
```
```bash
sh.shardCollection("produitsdb.produits", { pays: 1 })
sh.shardCollection("produitsdb.utilisateurs", { pays: 1 })
sh.shardCollection("produitsdb.commandes", { numeroCommande: 1 })
```
## 7. Fractionner et Déplacer les Chunks
EXecuter les commandes :
```bash
sh.splitAt("produitsdb.produits", { pays: "France" })
sh.splitAt("produitsdb.produits", { pays: "Allemagne" })
sh.splitAt("produitsdb.produits", { pays: "Italie" })
sh.splitAt("produitsdb.produits", { pays: "Espagne" })
sh.splitAt("produitsdb.produits", { pays: "Royaume-Uni" })

sh.splitAt("produitsdb.produits", { pays: "Chine" })
sh.splitAt("produitsdb.produits", { pays: "Japon" })
sh.splitAt("produitsdb.produits", { pays: "Inde" })
sh.splitAt("produitsdb.produits", { pays: "Vietnam" })
sh.splitAt("produitsdb.produits", { pays: "Thaïlande" })
sh.splitAt("produitsdb.produits", { pays: "Singapour" })
```
Enfin pour les utilisateurs :

```bash
sh.splitAt("produitsdb.utilisateurs", { pays: "France" })
sh.splitAt("produitsdb.utilisateurs", { pays: "Allemagne" })
sh.splitAt("produitsdb.utilisateurs", { pays: "Italie" })
sh.splitAt("produitsdb.utilisateurs", { pays: "Espagne" })
sh.splitAt("produitsdb.utilisateurs", { pays: "Royaume-Uni" })

sh.splitAt("produitsdb.utilisateurs", { pays: "Chine" })
sh.splitAt("produitsdb.utilisateurs", { pays: "Japon" })
sh.splitAt("produitsdb.utilisateurs", { pays: "Inde" })
sh.splitAt("produitsdb.utilisateurs", { pays: "Vietnam" })
sh.splitAt("produitsdb.utilisateurs", { pays: "Thaïlande" })
sh.splitAt("produitsdb.utilisateurs", { pays: "Singapour" })
```

## 8. Déplacer les Chunks vers les Shards appropriés

Déplacer les produits Europe vers europeRS :
```bash
  sh.moveChunk("produitsdb.produits", { pays: "France" }, "europeRS")
  sh.moveChunk("produitsdb.produits", { pays: "Allemagne" }, "europeRS")
  sh.moveChunk("produitsdb.produits", { pays: "Italie" }, "europeRS")
  sh.moveChunk("produitsdb.produits", { pays: "Espagne" }, "europeRS")
  sh.moveChunk("produitsdb.produits", { pays: "Royaume-Uni" }, "europeRS")
```
Déplacer les produits Asie vers AsieRS :
```bash
sh.moveChunk("produitsdb.produits", { pays: "Chine" }, "asiaRS")
sh.moveChunk("produitsdb.produits", { pays: "Japon" }, "asiaRS")
sh.moveChunk("produitsdb.produits", { pays: "Inde" }, "asiaRS")
sh.moveChunk("produitsdb.produits", { pays: "Vietnam" }, "asiaRS")
sh.moveChunk("produitsdb.produits", { pays: "Thaïlande" }, "asiaRS")
sh.moveChunk("produitsdb.produits", { pays: "Singapour" }, "asiaRS")
```

PAREIL MAINTENANT POUR LES UTILISATEURS ! : 
```bash
sh.moveChunk("produitsdb.utilisateurs", { pays: "France" }, "europeRS")
sh.moveChunk("produitsdb.utilisateurs", { pays: "Allemagne" }, "europeRS")
sh.moveChunk("produitsdb.utilisateurs", { pays: "Italie" }, "europeRS")
sh.moveChunk("produitsdb.utilisateurs", { pays: "Espagne" }, "europeRS")
sh.moveChunk("produitsdb.utilisateurs", { pays: "Royaume-Uni" }, "europeRS")

sh.moveChunk("produitsdb.utilisateurs", { pays: "Chine" }, "asiaRS")
sh.moveChunk("produitsdb.utilisateurs", { pays: "Japon" }, "asiaRS")
sh.moveChunk("produitsdb.utilisateurs", { pays: "Inde" }, "asiaRS")
sh.moveChunk("produitsdb.utilisateurs", { pays: "Vietnam" }, "asiaRS")
sh.moveChunk("produitsdb.utilisateurs", { pays: "Thaïlande" }, "asiaRS")
sh.moveChunk("produitsdb.utilisateurs", { pays: "Singapour" }, "asiaRS")
```

## 9. Verifier le status
Vérifiez l'état du sharding 
```bash
sh.status()
```

## 10. API
Après avoir executer des Posts , connectez vous à vos shards en utilisant : 
```
docker exec -it europe1 mongosh --port 27101
docker exec -it europe2 mongosh --port 27102
docker exec -it asia1 mongosh --port 27201
docker exec -it asia2 mongosh --port 27202
docker exec -it global1 mongosh --port 27301
docker exec -it global2 mongosh --port 27302
```
Afin de voir les documents ajouter executer :
```
use produitsdb

db.produits.find()

```

ou 

Vous pouvez maintenant vous connectez à vos bases de données avec MongoDB Compass.
Important : comme les instances MongoDB fonctionnent dans des conteneurs Docker avec des Replica Sets, il est nécessaire d’ajouter ``` ?directConnection=true ``` à l’URL de connexion.



Car sur Windows chaque conteneur n’est pas directement accessible comme une machine indépendante, ce qui empêche la résolution automatique des membres du replica set. 
En ajoutant ```directConnection=true ``` ça permet de cibler uniquement le nœud principal sans chercher à contacter les autres membres du replica set.

Ou

<img width="515" alt="image" src="https://github.com/user-attachments/assets/fd3ca0e6-4568-4ed1-b597-df1dc4fc64c7" />

```
exemple pour asie : 

mongodb://localhost:27021/?directConnection=true"
```
