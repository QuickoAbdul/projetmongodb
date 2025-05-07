package com.uphf.projetmongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "utilisateurs")
public class Utilisateur {

    @Id
    private String id;

    private String nom;
    private String prenom;

    @Indexed(unique = true)
    private String email;
    private String pays;

    public Utilisateur() {
    }

    public Utilisateur(String nom, String prenom, String email, String pays) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.pays = pays;
    }

    // Getters and Setters
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
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPays() {
        return pays;
    }
    public void setPays(String pays) {
        this.pays = pays;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", pays='" + pays + '\'' +
                '}';
    }
}
