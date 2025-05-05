package com.uphf.projetmongodb.config;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoTemplateConfig {

        @Primary
        @Bean(name = "mongoTemplate")
        @Qualifier("europe1MongoTemplate")
        public MongoTemplate europe1MongoTemplate() {
            return new MongoTemplate(
                    new SimpleMongoClientDatabaseFactory(
                            MongoClients.create("mongodb://localhost:27017/?directConnection=true"),
                            "store_europe"));
        }

        @Bean
        @Qualifier("asia1MongoTemplate")
        public MongoTemplate asia1MongoTemplate() {
            return new MongoTemplate(
                    new SimpleMongoClientDatabaseFactory(
                            MongoClients.create("mongodb://localhost:27019/?directConnection=true"),
                            "store_asia"));
        }

        @Bean
        @Qualifier("global1MongoTemplate")
        public MongoTemplate global1MongoTemplate() {
            return new MongoTemplate(
                    new SimpleMongoClientDatabaseFactory(
                            MongoClients.create("mongodb://localhost:27021/?directConnection=true"),
                            "store_global"));
        }

}

