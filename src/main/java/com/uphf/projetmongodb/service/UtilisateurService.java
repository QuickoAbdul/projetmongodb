package com.uphf.projetmongodb.service;

import com.uphf.projetmongodb.MongoShardsPersonalizedService;
import com.uphf.projetmongodb.model.Utilisateur;
import com.uphf.projetmongodb.repository.UtilisateurRepository;
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
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    private MongoShardsPersonalizedService mongoShardsPersonalizedService;

    @Autowired
    @Qualifier("europe1MongoTemplate")
    private MongoTemplate europe1MongoTemplate;

    @Autowired
    @Qualifier("europe2MongoTemplate")
    private MongoTemplate europe2MongoTemplate;

    @Autowired
    @Qualifier("asia1MongoTemplate")
    private MongoTemplate asia1MongoTemplate;

    @Autowired
    @Qualifier("asia2MongoTemplate")
    private MongoTemplate asia2MongoTemplate;

    @Autowired
    @Qualifier("global1MongoTemplate")
    private MongoTemplate global1MongoTemplate;

    @Autowired
    @Qualifier("global2MongoTemplate")
    private MongoTemplate global2MongoTemplate;

    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<Utilisateur> getAllUtilisateurs() {
        List<Utilisateur> allUtilisateurs = new ArrayList<>();

        allUtilisateurs.addAll(europe1MongoTemplate.findAll(Utilisateur.class));
        allUtilisateurs.addAll(asia1MongoTemplate.findAll(Utilisateur.class));
        allUtilisateurs.addAll(global1MongoTemplate.findAll(Utilisateur.class));

        return allUtilisateurs.stream()
                .collect(Collectors.toMap(
                        Utilisateur::getEmail,
                        utilisateur -> utilisateur,
                        (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    public Optional<Utilisateur> getUtilisateurByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));

        Utilisateur utilisateur = europe1MongoTemplate.findOne(query, Utilisateur.class);
        if (utilisateur != null) {
            return Optional.of(utilisateur);
        }

        utilisateur = asia1MongoTemplate.findOne(query, Utilisateur.class);
        if (utilisateur != null) {
            return Optional.of(utilisateur);
        }

        return Optional.empty();
    }

    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        if (getUtilisateurByEmail(utilisateur.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec le même email existe déjà.");
        }

        String region = determineRegion(utilisateur.getPays());
        mongoShardsPersonalizedService.saveDataToShard(utilisateur, region);

        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur updateUtilisateur(String email, Utilisateur utilisateur) {
        Optional<Utilisateur> existingUtilisateur = getUtilisateurByEmail(email);

        if (existingUtilisateur.isPresent()) {
            Utilisateur updatedUtilisateur = existingUtilisateur.get();

            String oldRegion = determineRegion(updatedUtilisateur.getPays());
            mongoShardsPersonalizedService.deleteDataFromShard(updatedUtilisateur, oldRegion);

            updatedUtilisateur.setNom(utilisateur.getNom());
            updatedUtilisateur.setPrenom(utilisateur.getPrenom());
            updatedUtilisateur.setEmail(utilisateur.getEmail());
            updatedUtilisateur.setPays(utilisateur.getPays());

            String newRegion = determineRegion(utilisateur.getPays());
            mongoShardsPersonalizedService.saveDataToShard(updatedUtilisateur, newRegion);

            return utilisateurRepository.save(updatedUtilisateur);
        } else {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'email : " + email);
        }
    }

    public void deleteUtilisateur(String email) {
        Optional<Utilisateur> existingUtilisateur = getUtilisateurByEmail(email);

        if (existingUtilisateur.isPresent()) {
            Utilisateur utilisateur = existingUtilisateur.get();
            String region = determineRegion(utilisateur.getPays());

            mongoShardsPersonalizedService.deleteDataFromShard(utilisateur, region);

            utilisateurRepository.delete(utilisateur);
        } else {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'email : " + email);
        }
    }

    //
    // Logique MONGODB POUR LES SHARDING
    //

    private String determineRegion(String pays) {
        if (List.of("France", "Allemagne, Italie", "Espagne").contains(pays)) {
            return "europe";
        } else if (List.of("Chine", "Japon", "Corée").contains(pays)) {
            return "asia";
        } else {
            return "global";
        }
    }

    /* public void saveRegionUtilisateur(Utilisateur utilisateur) {
        String pays = utilisateur.getPays().toLowerCase();

        if (inEurope(pays))  {
            saveToEurope(utilisateur);
        } else if (inAsia(pays)) {
            saveToAsia(utilisateur);
        } else {
            saveToGlobal(utilisateur);
        }
    }

    private boolean inEurope(String pays) {
        return List.of("france", "allemagne", "italie", "espagne").contains(pays);
    }

    private boolean inAsia(String pays) {
        return List.of("japon", "chine", "inde", "cambodge").contains(pays);
    }

    private void saveToEurope(Utilisateur utilisateur) {
        europe1MongoTemplate.save(utilisateur);
        europe2MongoTemplate.save(utilisateur);
    }

    private void saveToAsia(Utilisateur utilisateur) {
        asia1MongoTemplate.save(utilisateur);
        asia2MongoTemplate.save(utilisateur);
    }

    private void saveToGlobal(Utilisateur utilisateur) {
        global1MongoTemplate.save(utilisateur);
        global2MongoTemplate.save(utilisateur);
    }*/
}
