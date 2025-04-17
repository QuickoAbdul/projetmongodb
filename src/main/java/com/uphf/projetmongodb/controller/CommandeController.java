package com.uphf.projetmongodb.controller;

import com.uphf.projetmongodb.model.Commande;
import com.uphf.projetmongodb.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    private final CommandeService commandeService;

    @Autowired
    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    @GetMapping
    public List<Commande> getAllCommandes() {
        return commandeService.getAllCommandes();
    }

    @GetMapping("/{numeroCommande}")
    public ResponseEntity<Commande> getCommandeByNumeroCommande(@PathVariable String numeroCommande) {
        return commandeService.getCommandeByNumeroCommande(numeroCommande)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Commande> createCommande(@RequestBody Commande commande) {
        try {
            Commande created = commandeService.createCommande(commande);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{numeroCommande}")
    public ResponseEntity<Commande> updateCommande(@PathVariable String numeroCommande, @RequestBody Commande commande) {
        try {
            Commande updated = commandeService.updateCommande(numeroCommande, commande);
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{numeroCommande}")
    public ResponseEntity<Void> deleteCommande(@PathVariable String numeroCommande) {
        try {
            commandeService.deleteCommande(numeroCommande);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
