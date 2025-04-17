package com.uphf.projetmongodb.repository;

import com.uphf.projetmongodb.model.Produit;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProduitRepository extends MongoRepository<Produit, String> {
            Optional<Produit> findByNom(String nom);
}
