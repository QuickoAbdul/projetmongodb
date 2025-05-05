package com.uphf.projetmongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


@Service
public class MongoShardsPersonalizedService {

    @Autowired
    @Qualifier("europe1MongoTemplate")
    private MongoTemplate europe1MongoTemplate;

    @Autowired
    @Qualifier("asia1MongoTemplate")
    private MongoTemplate asia1MongoTemplate;

    @Autowired
    @Qualifier("global1MongoTemplate")
    private MongoTemplate global1MongoTemplate;


    public void saveDataToShard(Object entity, String region) {
        switch (region.toLowerCase()) {
            case "europe":
                europe1MongoTemplate.save(entity);
                break;
            case "asia":
                asia1MongoTemplate.save(entity);
                break;
            default:
                global1MongoTemplate.save(entity);
                break;
        }
    }

    public void deleteDataFromShard(Object entity, String region) {
        switch (region.toLowerCase()) {
            case "europe":
                europe1MongoTemplate.remove(entity);
                break;
            case "asia":
                asia1MongoTemplate.remove(entity);
                break;
            default:
                global1MongoTemplate.remove(entity);
                break;
        }
    }
}