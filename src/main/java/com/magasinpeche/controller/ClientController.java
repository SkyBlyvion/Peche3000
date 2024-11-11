package com.magasinpeche.controller;

import com.magasinpeche.model.Client;
import com.magasinpeche.model.Concours;
import com.magasinpeche.model.Permis;
import com.magasinpeche.repository.ConcoursRepository;
import com.magasinpeche.service.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ConcoursRepository concoursRepository;

    // register
    @GetMapping("/register")
    public String showInscriptionForm(Model model, HttpServletRequest request) {

        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Client client) {

        return "redirect:/login";
    }

    // login
    @GetMapping("/login")
    public String showConnexionForm(HttpServletRequest request) {

        return "login";
    }

    // page de profil
    @GetMapping("/profil")
    public String profil(Model model, Principal principal) {

        return "profil/profil";
    }


    // page de logout
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}
