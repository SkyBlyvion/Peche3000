package com.magasinpeche.controller;

import com.magasinpeche.model.Produit;
import com.magasinpeche.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// Contrôleur pour gérer les actions liées à la boutique en ligne
@Controller
@RequestMapping("/boutique")
public class ShopController {

    // Service pour les opérations liées aux produits
    private final ProduitService produitService;

    // Injection du service via le constructeur
    @Autowired
    public ShopController(ProduitService produitService) {
        this.produitService = produitService;
    }

    // Affiche la liste de tous les produits de la boutique
    @GetMapping
    public String getAllProduits(Model model) {
        // Récupère la liste de tous les produits via le service
        List<Produit> produits = produitService.getAllProduits();

        // Ajoute la liste des produits au modèle pour qu'elle soit accessible dans la vue
        model.addAttribute("produits", produits);

        // Retourne la vue correspondante pour afficher la liste des produits
        return "/boutique/liste"; // Fichier HTML : /templates/boutique/liste.html
    }

    // Affiche les détails d'un produit spécifique
    @GetMapping("/boutique/{id}")
    public String getProduit(@PathVariable("id") Long id, Model model) {
        // Récupère le produit par son ID via le service
        Produit produit = produitService.getProduitById(id);

        // Ajoute le produit au modèle pour qu'il soit accessible dans la vue
        model.addAttribute("produit", produit);

        // Retourne la vue correspondante pour afficher les détails du produit
        return "/boutique/detail"; // Fichier HTML : /templates/boutique/detail.html
    }
}

