package com.uphf.projetmongodb.service;

import com.uphf.projetmongodb.MongoShardsPersonalizedService;
import com.uphf.projetmongodb.model.Commande;
import com.uphf.projetmongodb.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommandeService {

    private final CommandeRepository commandeRepository;

    @Autowired
    private MongoShardsPersonalizedService mongoShardsPersonalizedService;

    @Autowired
    @Qualifier("global1MongoTemplate")
    private MongoTemplate global1MongoTemplate;

    @Autowired
    public CommandeService(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public Optional<Commande> getCommandeByNumeroCommande(String numeroCommande) {
        Query query = new Query(Criteria.where("numeroCommande").is(numeroCommande));

        Commande commande = global1MongoTemplate.findOne(query, Commande.class);
        if (commande != null) {
            return Optional.of(commande);
        }
        return Optional.empty();

    }

    public Commande createCommande(Commande commande) {
        if(getCommandeByNumeroCommande(commande.getNumeroCommande()).isPresent()) {
            throw new IllegalArgumentException("Produit déjà existant avec le numéro de commande : " + commande.getNumeroCommande());
        }

        String region = "global";
        mongoShardsPersonalizedService.saveDataToShard(commande, region);

        return commandeRepository.save(commande);
    }

    public Commande updateCommande(String numeroCommande, Commande commande) {
        Optional<Commande> existingCommande = getCommandeByNumeroCommande(numeroCommande);

        if (existingCommande.isPresent()) {
            Commande updatedCommande = existingCommande.get();
            String region = "global";
            // mongoShardsPersonalizedService.deleteDataFromShard(commande, region);
            updatedCommande.setNumeroCommande(commande.getNumeroCommande());
            updatedCommande.setProduits(commande.getProduits());
            updatedCommande.setEmailUtilisateur(commande.getEmailUtilisateur());

            mongoShardsPersonalizedService.saveDataToShard(updatedCommande, region);
            return commandeRepository.save(updatedCommande);
        } else {
            throw new IllegalArgumentException("Produit non trouvé avec numéro de commande : " + numeroCommande);
        }
    }

    public void deleteCommande(String numeroCommande) {
        Optional<Commande> existingCommande = getCommandeByNumeroCommande(numeroCommande);

        if (existingCommande.isPresent()) {
            String region = "global";
            Commande commande = existingCommande.get();

            mongoShardsPersonalizedService.deleteDataFromShard(commande, region);

            commandeRepository.delete(commande);
        } else {
            throw new IllegalArgumentException("Produit non trouvé avec le numéro de commande : " + numeroCommande);
        }
    }
    //Utilise ppas mais laisse pour apres
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
