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

// Contrôleur principal pour gérer la page d'accueil de l'application
@Controller
public class HomeController {

    // Injection du service des produits
    @Autowired
    private ProduitService produitService;

    // Injection du repository des concours
    @Autowired
    private ConcoursRepository concoursRepository;

    // Méthode pour afficher la page d'accueil
    @GetMapping("/")
    public String afficherAccueil(Model model) {
        // Récupère la liste des derniers produits ajoutés via le service
        List<Produit> derniersProduits = produitService.getDerniersProduits();

        // Limite la liste des produits à 3 éléments pour l'affichage sur la page d'accueil
        List<Produit> produitsLimites = derniersProduits.stream().limit(3).toList();

        // Ajoute la liste des produits au modèle pour qu'ils soient accessibles dans la vue
        model.addAttribute("produits", produitsLimites);

        // Récupère la date actuelle
        LocalDate today = LocalDate.now();

        // Récupère les 3 prochains concours triés par date ascendante
        List<Concours> concoursProchains = concoursRepository.findTop3ByDateGreaterThanOrderByDateAsc(today);

        // Ajoute la liste des concours au modèle pour qu'ils soient accessibles dans la vue
        model.addAttribute("concours", concoursProchains);

        // Retourne le nom de la vue correspondant à la page d'accueil
        return "home"; // Fichier de vue `home.html` dans le répertoire des templates
    }
}

