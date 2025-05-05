package com.uphf.projetmongodb.service;

import com.uphf.projetmongodb.MongoShardsPersonalizedService;
import com.uphf.projetmongodb.model.Produit;
import com.uphf.projetmongodb.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    @Autowired
    private MongoShardsPersonalizedService mongoShardsPersonalizedService;

    @Autowired
    @Qualifier("europe1MongoTemplate")
    private MongoTemplate europe1MongoTemplate;

    @Autowired
    @Qualifier("asia1MongoTemplate")
    private MongoTemplate asia1MongoTemplate;

    @Autowired
    @Qualifier("global1MongoTemplate")
    private MongoTemplate global1MongoTemplate;

    @Autowired
    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> getAllProduits() {
        List<Produit> allProduits = new ArrayList<>();

        allProduits.addAll(europe1MongoTemplate.findAll(Produit.class));
        allProduits.addAll(asia1MongoTemplate.findAll(Produit.class));
        allProduits.addAll(global1MongoTemplate.findAll(Produit.class));

        return allProduits.stream()
                .collect(Collectors.toMap(
                        Produit::getNom,
                        produit -> produit,
                        (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());
    }


    public Optional<Produit> getProduitByName(String nom) {
        Query query = new Query(Criteria.where("nom").is(nom));

        // test dans Europe 1
        Produit produit = europe1MongoTemplate.findOne(query, Produit.class);
        if (produit != null) {
            return Optional.of(produit);
        }

        // test dans Asie 1
        produit = asia1MongoTemplate.findOne(query, Produit.class);
        if (produit != null) {
            return Optional.of(produit);
        }

        return Optional.empty();
    }

    public Produit createProduit(Produit produit) {
        if (getProduitByName(produit.getNom()).isPresent()) {
            throw new IllegalArgumentException("Un produit avec le même nom existe déjà.");
        }

        String region = determineRegion(produit.getPays());
        mongoShardsPersonalizedService.saveDataToShard(produit, region);

        return produitRepository.save(produit);
    }

    public Produit updateProduit(String nom, Produit produit) {
        Optional<Produit> existingProduit = getProduitByName(nom);

        if (existingProduit.isPresent()) {
            Produit updatedProduit = existingProduit.get();

            String oldRegion = determineRegion(updatedProduit.getPays());
            System.out.println("oldRegion: " + oldRegion);
            mongoShardsPersonalizedService.deleteDataFromShard(updatedProduit, oldRegion);

            updatedProduit.setNom(produit.getNom());
            updatedProduit.setPrix(produit.getPrix());
            updatedProduit.setDescription(produit.getDescription());
            updatedProduit.setPays(produit.getPays());

            String newRegion = determineRegion(produit.getPays());
            mongoShardsPersonalizedService.saveDataToShard(updatedProduit, newRegion);
            return produitRepository.save(updatedProduit);
        } else {
            throw new IllegalArgumentException("Produit non trouvé avec le nom : " + nom);
        }
    }

    public void deleteProduit(String nom) {
        Optional<Produit> existingProduit = getProduitByName(nom);

        if (existingProduit.isPresent()) {
            Produit produit = existingProduit.get();
            String region = determineRegion(produit.getPays());

            mongoShardsPersonalizedService.deleteDataFromShard(produit, region);

            produitRepository.delete(produit);
        } else {
            throw new IllegalArgumentException("Produit non trouvé avec le nom : " + nom);
        }
    }

    private String determineRegion(String pays) {
        if (List.of("France", "Allemagne", "Italie", "Espagne", "Royaume-Uni").contains(pays)) {
            return "europe";
        } else if (List.of("Chine", "Japon", "Inde", "Vietnam", "Thaïlande", "Singapour").contains(pays)) {
            return "asia";
        } else {
            return "global";
        }
    }

}
