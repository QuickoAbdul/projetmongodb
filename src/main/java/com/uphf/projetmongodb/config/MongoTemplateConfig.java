package com.uphf.projetmongodb.config;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoTemplateConfig {

    @Bean
    @Qualifier("europe1MongoTemplate")
    public MongoTemplate europe1MongoTemplate() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        MongoClients.create("mongodb://localhost:27017"),
                        "europe_db_1"));
    }

    @Bean
    @Qualifier("europe2MongoTemplate")
    public MongoTemplate europe2MongoTemplate() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        MongoClients.create("mongodb://localhost:27018"),
                        "europe_db_2"));
    }

    @Bean
    @Qualifier("asia1MongoTemplate")
    public MongoTemplate asia1MongoTemplate() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        MongoClients.create("mongodb://localhost:27019"),
                        "asia_db_1"));
    }

    @Bean
    @Qualifier("asia2MongoTemplate")
    public MongoTemplate asia2MongoTemplate() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        MongoClients.create("mongodb://localhost:27020"),
                        "asia_db_2"));
    }

    @Bean
    @Qualifier("global1MongoTemplate")
    public MongoTemplate global1MongoTemplate() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        MongoClients.create("mongodb://localhost:27021"),
                        "global_db_1"));
    }

    @Bean
    @Qualifier("global2MongoTemplate")
    public MongoTemplate global2MongoTemplate() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        MongoClients.create("mongodb://localhost:27022"),
                        "global_db_2"));
    }
}

