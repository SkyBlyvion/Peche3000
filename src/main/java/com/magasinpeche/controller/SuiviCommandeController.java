package com.magasinpeche.controller;

import com.magasinpeche.model.Client;
import com.magasinpeche.model.Commande;
import com.magasinpeche.service.ClientService;
import com.magasinpeche.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class SuiviCommandeController {

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/mes-commandes")
    public String mesCommandes(Model model, Authentication authentication) {
        // Récupérer l'email du client connecté
        String email = authentication.getName();
        Optional<Client> clientOpt = clientService.findByEmail(email);

        if (!clientOpt.isPresent()) {
            return "redirect:/login"; // Rediriger si le client n'est pas trouvé
        }

        Client client = clientOpt.get();

        // Récupérer les commandes du client
        List<Commande> commandes = commandeService.getCommandesByClient(client.getId());
        model.addAttribute("commandes", commandes);
        return "mes_commandes";
    }

    @GetMapping("/mes-commandes/{id}")
    public String detailCommande(@PathVariable Long id, Model model, Authentication authentication) {
        // Récupérer l'email du client connecté
        String email = authentication.getName();
        Optional<Client> clientOpt = clientService.findByEmail(email);

        if (!clientOpt.isPresent()) {
            return "redirect:/login"; // Rediriger si le client n'est pas trouvé
        }

        Client client = clientOpt.get();

        // Récupérer la commande par ID
        Commande commande = commandeService.getCommandeById(id);

        // Vérifier que la commande appartient au client
        if (commande == null || !commande.getClient().getId().equals(client.getId())) {
            return "redirect:/mes-commandes";
        }

        model.addAttribute("commande", commande);
        return "detail_commande";
    }
}
