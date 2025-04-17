package com.uphf.projetmongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "produits")
public class Produit {

    @Id
    private String id;

    @Indexed(unique = true)
    private String nom;

    private double prix;
    private String description;
    private String pays;

    public Produit() {
    }

    public Produit(String nom, double prix, String description, String pays) {
        this.nom = nom;
        this.prix = prix;
        this.description = description;
        this.pays = pays;
    }

    //Getters and Setters

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public double getPrix() {
        return prix;
    }
    public void setPrix(double prix) {
        this.prix = prix;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPays() {
        return pays;
    }
    public void setPays(String pays) {
        this.pays = pays;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", description='" + description + '\'' +
                ", pays='" + pays + '\'' +
                '}';
    }

}
