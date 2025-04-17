package com.uphf.projetmongodb.service;

import com.uphf.projetmongodb.model.Produit;
import com.uphf.projetmongodb.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    @Autowired
    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Optional<Produit> getProduitByName(String nom) {
        return produitRepository.findByNom(nom);
    }

    public Produit createProduit(Produit produit) {
        // Check si le nom existe déjà sinon save
        if (produitRepository.findByNom(produit.getNom()).isPresent()) {
            throw new IllegalArgumentException("Un produit avec le même nom existe déjà.");
        }
        return produitRepository.save(produit);
    }

    public Produit updateProduit(String nom, Produit produit) {
        Optional<Produit> existingProduit = produitRepository.findByNom(nom);

        if (existingProduit.isPresent()) {
            Produit updatedProduit = existingProduit.get();
            updatedProduit.setNom(produit.getNom());
            updatedProduit.setPrix(produit.getPrix());
            updatedProduit.setDescription(produit.getDescription());
            updatedProduit.setPays(produit.getPays());
            return produitRepository.save(updatedProduit);
        } else {
            throw new IllegalArgumentException("Produit non trouvé avec le nom : " + nom);
        }
    }

    public void deleteProduit(String nom) {
        Optional<Produit> existingProduit = produitRepository.findByNom(nom);

        if (existingProduit.isPresent()) {
            produitRepository.delete(existingProduit.get());
        } else {
            throw new IllegalArgumentException("Produit non trouvé avec le nom : " + nom);
        }
    }
}
