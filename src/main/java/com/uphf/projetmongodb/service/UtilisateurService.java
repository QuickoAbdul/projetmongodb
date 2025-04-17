package com.uphf.projetmongodb.service;

import com.uphf.projetmongodb.model.Utilisateur;
import com.uphf.projetmongodb.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        // Check si le email existe déjà
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec le même email existe déjà.");
        }
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur updateUtilisateur(String email, Utilisateur utilisateur) {
        Optional<Utilisateur> existingUtilisateur = utilisateurRepository.findByEmail(email);

        if (existingUtilisateur.isPresent()) {
            Utilisateur updatedUtilisateur = existingUtilisateur.get();
            updatedUtilisateur.setNom(utilisateur.getNom());
            updatedUtilisateur.setPrenom(utilisateur.getPrenom());
            updatedUtilisateur.setEmail(utilisateur.getEmail());
            updatedUtilisateur.setPays(utilisateur.getPays());
           return utilisateurRepository.save(updatedUtilisateur);
        } else {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'email : " + email);
        }
    }

    public void deleteUtilisateur(String email) {
        Optional<Utilisateur> existingUtilisateur = utilisateurRepository.findByEmail(email);

        if (existingUtilisateur.isPresent()) {
            utilisateurRepository.delete(existingUtilisateur.get());
        } else {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'email : " + email);
        }
    }
}
