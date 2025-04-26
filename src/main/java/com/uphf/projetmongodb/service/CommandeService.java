package com.uphf.projetmongodb.service;

import com.uphf.projetmongodb.model.Commande;
import com.uphf.projetmongodb.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommandeService {

    private final CommandeRepository commandeRepository;

    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate generalMongoTemplate;


    @Autowired
    public CommandeService(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public Optional<Commande> getCommandeByNumeroCommande(String numeroCommande) {
        return commandeRepository.findByNumeroCommande(numeroCommande);
    }

    public Commande createCommande(Commande commande) {
        if (commandeRepository.findByNumeroCommande(commande.getNumeroCommande()).isPresent()) {
            throw new IllegalArgumentException("Numéro de commande déjà existant.");
        }
        ajouterCommande(commande);
        return commande;
    }

    public Commande updateCommande(String numeroCommande, Commande commande) {
        Commande existingCommande = commandeRepository.findByNumeroCommande(numeroCommande).orElse(null);

        if (existingCommande != null) {
            existingCommande.setNumeroCommande(commande.getNumeroCommande());
            existingCommande.setProduits(commande.getProduits());
            existingCommande.setEmailUtilisateur(commande.getEmailUtilisateur());
            ajouterCommande(existingCommande);
            return commandeRepository.save(existingCommande);
        } else {
            throw new IllegalArgumentException("Produit non trouvé avec numéro de commande : " + numeroCommande);
        }
    }

    public void deleteCommande(String numeroCommande) {
        Optional<Commande> existingCommande = commandeRepository.findByNumeroCommande(numeroCommande);

        if (existingCommande.isPresent()) {
            commandeRepository.delete(existingCommande.get());
        } else {
            throw new IllegalArgumentException("Produit non trouvé avec le numéro de commande : " + numeroCommande);
        }
    }

    //
    // Logique MONGODB POUR LES SHARDING
    //

    public void ajouterCommande(Commande commande) { saveToGlobal(commande); }

    private void saveToGlobal(Commande commande) { generalMongoTemplate.save(commande); }
}
