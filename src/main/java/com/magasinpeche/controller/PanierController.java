package com.magasinpeche.controller;

import com.magasinpeche.model.Cart;
import com.magasinpeche.model.Produit;
import com.magasinpeche.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/panier")
@SessionAttributes("cart")
public class PanierController {

    @Autowired
    private ProduitService produitService;

    // Initialise le panier s'il n'existe pas
    @ModelAttribute("cart")
    public Cart initializeCart() {
        return new Cart();
    }

    @GetMapping
    public String showPanier(@ModelAttribute("cart") Cart cart, Model model) {
        model.addAttribute("items", cart.getItems());
        model.addAttribute("total", cart.getTotal());
        return "panier";
    }

    @PostMapping("/ajouter")
    public String addToPanier(@RequestParam Long produitId, @RequestParam int quantity,
                              @ModelAttribute("cart") Cart cart) {
        Produit produit = produitService.getProduitById(produitId);
        cart.addItem(produit, quantity);
        return "redirect:/panier";
    }

    @PostMapping("/supprimer")
    public String removeFromPanier(@RequestParam Long produitId,
                                   @ModelAttribute("cart") Cart cart) {
        cart.removeItem(produitId);
        return "redirect:/panier";
    }

    @PostMapping("/mettre-a-jour")
    public String updatePanier(@RequestParam Long produitId, @RequestParam int quantity,
                               @ModelAttribute("cart") Cart cart) {
        cart.updateItem(produitId, quantity);
        return "redirect:/panier";
    }

    @PostMapping("/vider")
    public String clearPanier(@ModelAttribute("cart") Cart cart, SessionStatus status) {
        cart.clear();
        status.setComplete();
        return "redirect:/panier";
    }
}
