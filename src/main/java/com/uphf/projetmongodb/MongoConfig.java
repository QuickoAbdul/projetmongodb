package com.uphf.projetmongodb;

import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {

    private static final String MONGOS_URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "produitsdb";

    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        MongoClients.create(MONGOS_URI), DATABASE_NAME));
    }
}

/*
// TODO pour le projet 2
// Je dois modifie le docker et réer 3 bases de données distincts Europe, Asia et General & Faire leur replicaSet
// Puis créer une liste des pays par région dans le code pour les filtrer
// et envoyer au bon shard en intégrant 3 nouveaux MongoTemplate
// et les insérer dans leurs Services et dans la Méhodes CREATE&AJOUTER
//

@Configuration
@EnableMongoRepositories(basePackages = "com.uphf.projetmongodb.repository")
public class MongoConfig {

    @Bean(name = "europeMongoTemplate")
    public MongoTemplate europeMongoTemplate() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        MongoClients.create("mongodb://localhost:27101"), "produitsdb"));
    }

    @Bean(name = "asiaMongoTemplate")
    public MongoTemplate asiaMongoTemplate() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        MongoClients.create("mongodb://localhost:27201"), "produitsdb"));
    }

    @Bean(name = "generalMongoTemplate")
    public MongoTemplate generalMongoTemplate() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        MongoClients.create("mongodb://localhost:27301"), "produitsdb"));
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        MongoClients.create("mongodb://localhost:27020"), "produitsdb"));
    }
}
 */