package com.magasinpeche.controller;

import com.magasinpeche.model.Client;
import com.magasinpeche.model.Permis;
import com.magasinpeche.model.Statut;
import com.magasinpeche.service.ClientService;
import com.magasinpeche.service.PermisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

// Contrôleur pour gérer les demandes de permis
@Controller
@RequestMapping("/permis")
public class PermisController {

    // Service pour les opérations liées aux permis
    @Autowired
    private PermisService permisService;

    // Service pour les opérations liées aux clients
    @Autowired
    private ClientService clientService;

    // Affiche le formulaire pour demander un permis
    @GetMapping("/demande")
    public String demandePermisForm(Model model, Principal principal) {
        // Récupère l'utilisateur connecté grâce à son email
        Client client = clientService.findByEmail(principal.getName()).orElse(null);

        // Ajoute un objet vide Permis au modèle pour le formulaire
        model.addAttribute("permis", new Permis());

        // Ajoute l'ID du client au modèle pour le lier au permis
        model.addAttribute("clientId", client.getId());

        // Affiche la vue pour demander un permis
        return "permis/demande";
    }

    // Soumet une nouvelle demande de permis
    @PostMapping("/demande")
    public String soumettreDemande(@ModelAttribute Permis permis, @RequestParam Long clientId) {
        // Récupère le client par son ID
        Client client = clientService.findById(clientId).orElse(null);

        // Associe le client à la demande de permis
        permis.setClient(client);

        // Sauvegarde la demande de permis via le service
        permisService.save(permis);

        // Redirige l'utilisateur vers sa page de profil
        return "redirect:/profil";
    }

    // Affiche la liste des demandes de permis (accessible uniquement aux administrateurs)
    @PreAuthorize("hasRole('ADMIN')") // Vérifie que l'utilisateur a le rôle ADMIN
    @GetMapping("/liste")
    public String listDemandes(Model model) {
        // Récupère toutes les demandes de permis, triées par date
        model.addAttribute("demandes", permisService.findAllOrderedByDate());

        // Retourne la vue pour afficher la liste des demandes
        return "liste";
    }

    // Traite une demande de permis spécifique (accessible aux administrateurs)
    @PostMapping("/traiter/{id}")
    public String traiterDemande(@PathVariable Long id, @RequestParam Statut statut) {
        // Met à jour le statut de la demande via le service
        permisService.updateStatut(id, statut);

        // TODO : Appeler une méthode pour envoyer une notification par email au client
        // Exemple : notificationService.sendEmail(clientEmail, "Votre demande a été traitée.");

        // Redirige vers la liste des demandes après le traitement
        return "redirect:/permis/liste";
    }
}

