package com.uphf.projetmongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "commandes")
public class Commande {

    @Id
    private String id;

    @Indexed(unique = true)
    private String numeroCommande;
    private String[] produits;
    private String emailUtilisateur;

    public Commande() {
    }

    public Commande(String numeroCommande, String[] produits, String emailUtilisateur) {
        this.numeroCommande = numeroCommande;
        this.produits = produits;
        this.emailUtilisateur = emailUtilisateur;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNumeroCommande() {
        return numeroCommande;
    }
    public void setNumeroCommande(String numeroCommande) {
        this.numeroCommande = numeroCommande;
    }
    public String[] getProduits() {
        return produits;
    }
    public void setProduits(String[] produits) {
        this.produits = produits;
    }
    public String getEmailUtilisateur() {
        return emailUtilisateur;
    }
    public void setEmailUtilisateur(String emailUtilisateur) {
        this.emailUtilisateur = emailUtilisateur;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id='" + id + '\'' +
                ", numeroCommande='" + numeroCommande + '\'' +
                ", produits=" + String.join(", ", produits) +
                ", emailUtilisateur='" + emailUtilisateur + '\'' +
                '}';
    }
}
