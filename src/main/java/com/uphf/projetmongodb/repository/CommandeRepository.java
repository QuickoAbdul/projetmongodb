package com.uphf.projetmongodb.repository;

import com.uphf.projetmongodb.model.Commande;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CommandeRepository extends MongoRepository<Commande, String> {
    Optional<Commande> findByNumeroCommande(String numeroCommande);
}

