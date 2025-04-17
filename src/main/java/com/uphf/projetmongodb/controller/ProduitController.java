package com.uphf.projetmongodb.controller;

import com.uphf.projetmongodb.model.Produit;
import com.uphf.projetmongodb.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    private final ProduitService produitService;

    @Autowired
    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public List<Produit> getAllProduits() {
        return produitService.getAllProduits();
    }

    @GetMapping("/{nom}")
    public ResponseEntity<Produit> getProduitByNom(@PathVariable String nom) {
        return produitService.getProduitByName(nom)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Produit> createProduit(@RequestBody Produit produit) {
        try {
            Produit created = produitService.createProduit(produit);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{nom}")
    public ResponseEntity<Produit> updateProduit(@PathVariable String nom, @RequestBody Produit produit) {
        try {
            Produit updated = produitService.updateProduit(nom, produit);
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{nom}")
    public ResponseEntity<Void> deleteProduit(@PathVariable String nom) {
        try {
            produitService.deleteProduit(nom);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
