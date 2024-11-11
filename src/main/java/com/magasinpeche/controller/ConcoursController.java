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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/concours")
public class ConcoursController {

    @Autowired
    private ConcoursRepository concoursRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @GetMapping
    public String afficherConcours(Model model, Authentication authentication) {

        String emailClient = authentication.getName();
        Client client = clientRepository.findByEmail(emailClient)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        LocalDate today = LocalDate.now();

        List<Concours> concoursProchains = concoursRepository.findUpcomingConcoursSortedByDate(today);

        System.out.println("Concours à venir : " + concoursProchains);

        for (Concours concours : concoursProchains) {
            boolean dejaInscrit = concours.getParticipations().stream()
                    .anyMatch(participation -> participation.getClient().equals(client));
            concours.setDejaInscrit(dejaInscrit);
        }
        model.addAttribute("concoursList", concoursProchains);
        return "concours/liste";
    }


    @PostMapping("/inscrire/{id}")
    public String inscrireClient(@PathVariable Long id, Authentication authentication) {

        String emailClient = authentication.getName();
        Client client = clientRepository.findByEmail(emailClient).orElseThrow(() -> new RuntimeException("Client non trouvé"));

        Concours concours = concoursRepository.findById(id).orElseThrow(() -> new RuntimeException("Concours non trouvé"));

        boolean isAlreadyRegistered = concours.getParticipations().stream()
                .anyMatch(participation -> participation.getClient().getEmail().equals(emailClient));

        if (isAlreadyRegistered) {
            return "redirect:/concours?error=already_registered";
        }

        Participation participation = new Participation();
        participation.setConcours(concours);
        participation.setClient(client);
        participation.setStatut(Statut.EN_ATTENTE);

        participationRepository.save(participation);

        return "redirect:/concours?success=registration_success";
    }

    @GetMapping("/suivi")
    public String suiviParticipations(Model model, Authentication authentication) {
        String emailClient = authentication.getName();  // Récupère l'email du client connecté
        Client client = clientRepository.findByEmail(emailClient).orElseThrow(() -> new RuntimeException("Client non trouvé"));
        List<Concours> participations = concoursRepository.findConcoursByClient(client);
        model.addAttribute("participations", participations);
        return "concours/suivi-participations";
    }

    @GetMapping("/desinscrire/{id}")
    public String desinscrire(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        Client client = clientService.findByEmail(email).orElse(null);

        if (client != null) {
            Concours concours = concoursRepository.findById(id).orElse(null);
            if (concours != null) {
                Participation participation = participationRepository.findByConcoursAndClient(concours, client);
                if (participation != null) {
                    participationRepository.delete(participation);
                }
            }
        }
        return "redirect:/profil";
    }
}
