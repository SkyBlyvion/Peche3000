package com.magasinpeche.controller;

import com.magasinpeche.model.Cart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("cart")
public class GlobalController {

    @ModelAttribute("cart")
    public Cart initializeCart() {
        return new Cart(); // Renvoie un panier vide si non initialisé
    }
}
