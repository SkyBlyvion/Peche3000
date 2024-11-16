package com.magasinpeche.controller;

import com.magasinpeche.model.Client;
import com.magasinpeche.model.Concours;
import com.magasinpeche.model.Participation;
import com.magasinpeche.model.Statut;
import com.magasinpeche.repository.ClientRepository;
import com.magasinpeche.repository.ConcoursRepository;
import com.magasinpeche.repository.ParticipationRepository;
import com.magasinpeche.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/concours") // Définit la route de base pour ce contrôleur : "/concours".
public class ConcoursController {

    @Autowired
    private ConcoursRepository concoursRepository; // Injecte le dépôt pour gérer les concours.

    @Autowired
    private ParticipationRepository participationRepository; // Injecte le dépôt pour gérer les participations.

    @Autowired
    private ClientRepository clientRepository; // Injecte le dépôt pour gérer les clients.

    @Autowired
    private ClientService clientService; // Injecte le service pour les clients.

    /**
     * Affiche la liste des concours accessibles.
     * @param model Objet Model pour passer les données à la vue.
     * @param authentication Objet pour récupérer les informations de l'utilisateur connecté.
     * @return La vue "concours/liste" contenant les concours à venir.
     */
    @GetMapping
    public String afficherConcours(Model model, Authentication authentication) {
        // Récupère l'email du client actuellement connecté.
        String emailClient = authentication.getName();
        Client client = clientRepository.findByEmail(emailClient)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        // Récupère la date actuelle pour filtrer les concours à venir.
        LocalDate today = LocalDate.now();
        List<Concours> concoursProchains = concoursRepository.findUpcomingConcoursSortedByDate(today);

        // Affiche les concours récupérés pour le debug.
        System.out.println("Concours à venir : " + concoursProchains);

        // Vérifie si le client est déjà inscrit à chaque concours.
        for (Concours concours : concoursProchains) {
            boolean dejaInscrit = concours.getParticipations().stream()
                    .anyMatch(participation -> participation.getClient().equals(client));
            concours.setDejaInscrit(dejaInscrit); // Marque le concours comme "déjà inscrit".
        }

        // Ajoute la liste des concours au modèle pour l'afficher dans la vue.
        model.addAttribute("concoursList", concoursProchains);
        return "concours/liste"; // Retourne la vue pour afficher les concours.
    }

    /**
     * Inscrit un client à un concours spécifique.
     *
     * @param id             L'identifiant du concours.
     * @param authentication Objet pour récupérer les informations de l'utilisateur connecté.
     * @return Redirige vers la liste des concours avec un message de succès ou d'erreur.
     */
    @PostMapping("/inscrire/{id}")
    public String inscrireClient(@PathVariable Long id, Authentication authentication) {
        // Récupère l'email du client actuellement connecté.
        String emailClient = authentication.getName();
        Client client = clientRepository.findByEmail(emailClient)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        // Récupère le concours correspondant à l'identifiant.
        Concours concours = concoursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concours non trouvé"));

        // Vérifie si le client est déjà inscrit.
        boolean isAlreadyRegistered = concours.getParticipations().stream()
                .anyMatch(participation -> participation.getClient().getEmail().equals(emailClient));

        if (isAlreadyRegistered) {
            return "redirect:/concours?error=already_registered"; // Redirige avec un message d'erreur.
        }

        // Crée une nouvelle participation pour le client.
        Participation participation = new Participation();
        participation.setConcours(concours);
        participation.setClient(client);
        participation.setStatut(Statut.EN_ATTENTE); // Définit le statut initial.

        // Sauvegarde la participation dans le dépôt.
        participationRepository.save(participation);

        return "redirect:/concours?success=registration_success"; // Redirige avec un message de succès.
    }

    /**
     * Affiche le suivi des participations de l'utilisateur connecté.
     * @param model Objet Model pour passer les données à la vue.
     * @param authentication Objet pour récupérer les informations de l'utilisateur connecté.
     * @return La vue "concours/suivi-participations".
     */
    @GetMapping("/suivi")
    public String suiviParticipations(Model model, Authentication authentication) {
        // Récupère l'email du client actuellement connecté.
        String emailClient = authentication.getName();
        Client client = clientRepository.findByEmail(emailClient)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        // Récupère les concours auxquels le client participe.
        List<Concours> participations = concoursRepository.findConcoursByClient(client);

        // Ajoute les participations au modèle pour les afficher dans la vue.
        model.addAttribute("participations", participations);
        return "concours/suivi-participations"; // Retourne la vue pour le suivi des participations.
    }

    /**
     * Désinscrit un client d'un concours spécifique.
     * @param id L'identifiant du concours.
     * @param principal Objet pour récupérer les informations de l'utilisateur connecté.
     * @return Redirige vers le profil utilisateur après la désinscription.
     */
    @GetMapping("/desinscrire/{id}")
    public String desinscrire(@PathVariable Long id, Principal principal) {
        // Récupère l'email du client actuellement connecté.
        String email = principal.getName();
        Client client = clientService.findByEmail(email).orElse(null);

        if (client != null) {
            // Récupère le concours correspondant.
            Concours concours = concoursRepository.findById(id).orElse(null);
            if (concours != null) {
                // Recherche la participation du client au concours.
                Participation participation = participationRepository.findByConcoursAndClient(concours, client);
                if (participation != null) {
                    // Supprime la participation si elle existe.
                    participationRepository.delete(participation);
                }
            }
        }
        return "redirect:/profil"; // Redirige vers la page de profil.
    }
}