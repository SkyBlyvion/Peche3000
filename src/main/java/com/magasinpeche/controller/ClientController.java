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

// Définition du contrôleur pour gérer les interactions des clients.
@Controller
public class ClientController {

    // Service pour gérer les clients
    @Autowired
    private ClientService clientService;

    // Repository pour gérer les concours
    @Autowired
    private ConcoursRepository concoursRepository;

    // Affiche le formulaire d'inscription
    @GetMapping("/register")
    public String showInscriptionForm(Model model, HttpServletRequest request) {
        model.addAttribute("client", new Client()); // Initialise un nouvel objet Client pour le formulaire
        if (request.getUserPrincipal() != null) { // Vérifie si l'utilisateur est déjà connecté
            return "redirect:/"; // Redirige vers la page d'accueil si l'utilisateur est connecté
        }
        return "register"; // Affiche la vue d'inscription
    }

    // Gère la soumission du formulaire d'inscription
    @PostMapping("/register")
    public String register(@ModelAttribute Client client) {
        clientService.save(client); // Enregistre le client dans la base de données
        return "redirect:/login"; // Redirige vers la page de connexion
    }

    // Affiche le formulaire de connexion
    @GetMapping("/login")
    public String showConnexionForm(HttpServletRequest request) {
        if (request.getUserPrincipal() != null) { // Vérifie si l'utilisateur est déjà connecté
            return "redirect:/"; // Redirige vers la page d'accueil
        }
        return "login"; // Affiche la vue de connexion
    }

    // Affiche la page de profil pour l'utilisateur connecté
    @GetMapping("/profil")
    public String profil(Model model, Principal principal) {
        if (principal != null) { // Vérifie si l'utilisateur est connecté
            String email = principal.getName(); // Récupère l'email de l'utilisateur connecté
            Client client = clientService.findByEmail(email).orElse(null); // Recherche le client par email
            model.addAttribute("client", client); // Ajoute le client au modèle

            // Récupère le permis du client
            Permis permis = (client != null) ? client.getPermis() : null;
            model.addAttribute("permis", permis);

            // Récupère les concours auxquels le client a participé
            List<Concours> participations = concoursRepository.findConcoursByClient(client);
            model.addAttribute("concours", participations);
        }
        return "profil/profil"; // Affiche la vue du profil
    }

    // Gère la déconnexion de l'utilisateur
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login"; // Redirige vers la page de connexion après la déconnexion
    }

    /* MISE À JOUR DU PROFIL (section commentée) */
    /*
     * Ce bloc de code gère la mise à jour des informations du profil client.
     * Il utilise un DTO pour valider et transmettre les données avant de les enregistrer.
     * */

    /* DTO EXPLICATIF :
     * ClientUpdateDto est utilisé pour valider les champs modifiés par le client.
     * Il contient des getters et setters pour les champs modifiables :
     *  - prénom, nom, téléphone, email, adresse.
     */

    /*
    // Exemple : Mise à jour du profil utilisateur
    @PostMapping("/profil")
    public String updateProfil(
            @ModelAttribute("clientUpdateDto") ClientUpdateDto updatedClientDto,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (principal != null) { // Vérifie si l'utilisateur est connecté
            String currentEmail = principal.getName(); // Récupère l'email actuel
            Client client = clientService.findByEmail(currentEmail)
                    .orElseThrow(() -> new NoSuchElementException("Client non trouvé pour l'email : " + currentEmail));

            // Sauvegarde le mot de passe existant pour éviter de l'écraser
            String existingPassword = client.getPassword();

            // Vérifie si l'email est modifié et s'il est déjà utilisé
            if (!client.getEmail().equals(updatedClientDto.getEmail())) {
                Optional<Client> clientWithEmail = clientService.findByEmail(updatedClientDto.getEmail());
                if (clientWithEmail.isPresent()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Cet email est déjà utilisé par un autre utilisateur.");
                    return "redirect:/profil";
                } else {
                    client.setEmail(updatedClientDto.getEmail());
                    // Mettez à jour l'authentification si nécessaire
                }
            }

            // Met à jour les autres informations du client
            client.setPrenom(updatedClientDto.getPrenom());
            client.setNom(updatedClientDto.getNom());
            client.setTelephone(updatedClientDto.getTelephone());
            client.setAdresse(updatedClientDto.getAdresse());

            // Réassigner le mot de passe existant pour éviter de l'écraser
            client.setPassword(existingPassword);

            // Enregistrer les modifications
            clientService.save(client);

            redirectAttributes.addFlashAttribute("successMessage", "Vos informations ont été mises à jour avec succès !");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Vous devez être connecté pour effectuer cette action.");
            return "redirect:/login";
        }
        return "redirect:/profil";
    }*/

    /* DTO */
    /* package com.magasinpeche.dto;

    public class ClientUpdateDto {
    private String prenom;
    private String nom;
    private String telephone;
    private String email;
    private String adresse;

    // Getters et setters

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
*/

}

