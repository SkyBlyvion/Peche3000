package com.magasinpeche.controller;

import com.magasinpeche.model.Commande;
import com.magasinpeche.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Contrôleur REST pour gérer les commandes
@RestController
@RequestMapping("/commandes")
public class CommandeController {

    // Service pour gérer les opérations liées aux commandes
    @Autowired
    private CommandeService commandeService;

    // Récupère la liste de toutes les commandes
    @GetMapping
    public List<Commande> getAllCommandes() {
        return commandeService.getAllCommandes(); // Retourne toutes les commandes
    }

    // Récupère une commande spécifique par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Long id) {
        Commande commande = commandeService.getCommandeById(id); // Recherche la commande par son ID
        if (commande != null) { // Si la commande est trouvée
            return ResponseEntity.ok(commande); // Renvoie la commande avec un statut HTTP 200 (OK)
        } else {
            return ResponseEntity.notFound().build(); // Renvoie un statut HTTP 404 (Not Found) si non trouvée
        }
    }

    // Crée une nouvelle commande
    @PostMapping
    public Commande createCommande(@RequestBody Commande commande) {
        return commandeService.createCommande(commande); // Enregistre la commande et la retourne
    }

    // Met à jour une commande existante par son ID
    @PutMapping("/{id}")
    public ResponseEntity<Commande> updateCommande(@PathVariable Long id, @RequestBody Commande commandeDetails) {
        Commande updatedCommande = commandeService.updateCommande(id, commandeDetails); // Met à jour la commande
        if (updatedCommande != null) { // Si la commande est mise à jour avec succès
            return ResponseEntity.ok(updatedCommande); // Renvoie la commande mise à jour avec un statut HTTP 200 (OK)
        } else {
            return ResponseEntity.notFound().build(); // Renvoie un statut HTTP 404 si la commande n'existe pas
        }
    }

    // Supprime une commande par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        commandeService.deleteCommande(id); // Supprime la commande
        return ResponseEntity.noContent().build(); // Renvoie un statut HTTP 204 (No Content)
    }
}