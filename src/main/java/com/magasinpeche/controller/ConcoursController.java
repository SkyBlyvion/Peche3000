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

        return "concours/liste";
    }


    @PostMapping("/inscrire/{id}")
    public String inscrireClient(@PathVariable Long id, Authentication authentication) {
        return "redirect:/concours?success=registration_success";
    }


    @GetMapping("/suivi")
    public String suiviParticipations(Model model, Authentication authentication) {
        return "concours/suivi-participations";
    }


    @GetMapping("/desinscrire/{id}")
    public String desinscrire(@PathVariable Long id, Principal principal) {

        return "redirect:/profil";
    }
}
