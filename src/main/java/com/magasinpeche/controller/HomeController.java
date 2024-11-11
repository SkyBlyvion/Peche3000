package com.magasinpeche.controller;

import com.magasinpeche.model.Concours;
import com.magasinpeche.model.Produit;
import com.magasinpeche.repository.ConcoursRepository;
import com.magasinpeche.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProduitService produitService;

    @Autowired
    private ConcoursRepository concoursRepository;

    @GetMapping("/")
    public String afficherAccueil(Model model) {
        List<Produit> derniersProduits = produitService.getDerniersProduits();
        List<Produit> produitsLimites = derniersProduits.stream().limit(3).toList();
        model.addAttribute("produits", produitsLimites);
        LocalDate today = LocalDate.now();
        List<Concours> concoursProchains = concoursRepository.findTop3ByDateGreaterThanOrderByDateAsc(today);
        model.addAttribute("concours", concoursProchains);
        return "home";
    }
}
