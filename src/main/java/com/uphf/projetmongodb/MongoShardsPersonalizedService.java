package com.uphf.projetmongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MongoShardsPersonalizedService {

    @Autowired
    @Qualifier("europe1MongoTemplate")
    private MongoTemplate europe1MongoTemplate;

    @Autowired
    @Qualifier("europe2MongoTemplate")
    private MongoTemplate europe2MongoTemplate;

    @Autowired
    @Qualifier("asia1MongoTemplate")
    private MongoTemplate asia1MongoTemplate;

    @Autowired
    @Qualifier("asia2MongoTemplate")
    private MongoTemplate asia2MongoTemplate;

    @Autowired
    @Qualifier("global1MongoTemplate")
    private MongoTemplate global1MongoTemplate;

    @Autowired
    @Qualifier("global2MongoTemplate")
    private MongoTemplate global2MongoTemplate;

    public void saveDataToShard(Object entity, String region) {
        switch (region.toLowerCase()) {
            case "europe":
                europe1MongoTemplate.save(entity);
                europe2MongoTemplate.save(entity);
                break;
            case "asia":
                asia1MongoTemplate.save(entity);
                asia2MongoTemplate.save(entity);
                break;
            default:
                global1MongoTemplate.save(entity);
                global2MongoTemplate.save(entity);
                break;
        }
    }

    public void deleteDataFromShard(Object entity, String region) {
        switch (region.toLowerCase()) {
            case "europe":
                europe1MongoTemplate.remove(entity);
                europe2MongoTemplate.remove(entity);
                break;
            case "asia":
                asia1MongoTemplate.remove(entity);
                asia2MongoTemplate.remove(entity);
                break;
            default:
                global1MongoTemplate.remove(entity);
                global2MongoTemplate.remove(entity);
                break;
        }
    }
}