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

@Controller
@RequestMapping("/permis")
public class PermisController {
    @Autowired
    private PermisService permisService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/demande")
    public String demandePermisForm(Model model, Principal principal) {

        return "permis/demande";
    }

    @PostMapping("/demande")
    public String soumettreDemande(@ModelAttribute Permis permis, @RequestParam Long clientId) {

        return "redirect:/profil";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/liste")
    public String listDemandes(Model model) {

        return "permis/list"; /
    }

    @PostMapping("/traiter/{id}")
    public String traiterDemande(@PathVariable Long id, @RequestParam Statut statut) {

        return "redirect:/permis/liste";
    }
}
