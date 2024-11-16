package com.magasinpeche.controller;

import com.magasinpeche.model.Cart;
import com.magasinpeche.model.CartItem;
import com.magasinpeche.model.Client;
import com.magasinpeche.model.Commande;
import com.magasinpeche.model.LigneCommande;
import com.magasinpeche.service.ClientService;
import com.magasinpeche.service.CommandeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // Import correct
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/paiement")
@SessionAttributes("cart")
public class PaiementController {

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private ClientService utilisateurService;

    @GetMapping
    public String paiement(@ModelAttribute("cart") Cart cart, Model model) throws StripeException {
        // Configurer Stripe avec votre clé secrète
        com.stripe.Stripe.apiKey = "sk_test_51QLsEbFdUMS6Sv5v0nIV0rEzYHZkJ46YjsCLbXRScnmOyiaQAKFAXvpor7nfjlA3kLU4hbxtG9SGea5F4QXbqCkt00tEX289R6";

        // Créer une session de paiement
        SessionCreateParams.Builder builder = new SessionCreateParams.Builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/paiement/success")
                .setCancelUrl("http://localhost:8080/paiement/cancel");

        for (CartItem item : cart.getItems()) {
            builder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) item.getQuantity())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("eur")
                                            .setUnitAmount((long) (item.getProduit().getPrix() * 100))
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(item.getProduit().getNom())
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }

        SessionCreateParams params = builder.build();
        Session session = Session.create(params);

        model.addAttribute("sessionId", session.getId());
        return "paiement";
    }

    @GetMapping("/success")
    public String paiementSuccess(@ModelAttribute("cart") Cart cart, SessionStatus status, Authentication authentication) {
        // Récupérer l'email de l'utilisateur connecté
        String email = authentication.getName();

        // Charger le Client depuis la base de données
        Optional<Client> clientOpt = utilisateurService.findByEmail(email);

        if (!clientOpt.isPresent()) {
            // Gérer le cas où le client n'est pas trouvé (redirection, message d'erreur, etc.)
            return "redirect:/login";
        }

        Client client = clientOpt.get();

        // Créer la commande
        Commande commande = new Commande();
        commande.setClient(client);
        commande.setDateCommande(LocalDateTime.now());
        commande.setStatut("payée");
        commande.setTotal(cart.getTotal());

        // Créer les lignes de commande
        List<LigneCommande> lignesCommande = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            LigneCommande ligne = new LigneCommande();
            ligne.setCommande(commande); // Associe la ligne à la commande
            ligne.setProduit(item.getProduit());
            ligne.setQuantite(item.getQuantity());
            ligne.setPrixUnitaire(item.getProduit().getPrix());
            lignesCommande.add(ligne);
        }
        commande.setLignesCommande(lignesCommande);

        // Enregistrer la commande
        commandeService.createCommande(commande);

        // Vider le panier
        cart.clear();
        status.setComplete();

        return "paiement_success";
    }


    @GetMapping("/cancel")
    public String paiementCancel() {
        return "paiement_cancel";
    }
}
