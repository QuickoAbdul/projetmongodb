package com.uphf.projetmongodb.repository;


import com.uphf.projetmongodb.model.Utilisateur;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UtilisateurRepository extends MongoRepository<Utilisateur, String> {
         Optional<Utilisateur> findByEmail(String email);
}

