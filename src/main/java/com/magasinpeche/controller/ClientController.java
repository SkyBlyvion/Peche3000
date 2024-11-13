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

    @GetMapping("/register")
    public String showInscriptionForm(Model model, HttpServletRequest request) {
        model.addAttribute("client", new Client());
        if (request.getUserPrincipal() != null) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Client client) {
        clientService.save(client);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showConnexionForm(HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/profil")
    public String profil(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            Client client = clientService.findByEmail(email).orElse(null);
            model.addAttribute("client", client);

            Permis permis = (client != null) ? client.getPermis() : null;
            model.addAttribute("permis", permis);

            List<Concours> participations = concoursRepository.findConcoursByClient(client);
            model.addAttribute("concours", participations);
        }
        return "profil/profil";
    }
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    /* UPDATE PROFILE */
    /*
    * @PostMapping("/profil")
    public String updateProfil(
            @ModelAttribute("clientUpdateDto") ClientUpdateDto updatedClientDto,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (principal != null) {
            String currentEmail = principal.getName();
            Client client = clientService.findByEmail(currentEmail)
                    .orElseThrow(() -> new NoSuchElementException("Client non trouvé pour l'email : " + currentEmail));

            // Sauvegarder le mot de passe existant pour éviter de l'écraser
            String existingPassword = client.getPassword();

            // Vérifier si l'email est modifié et s'il est déjà utilisé
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

            // Mettre à jour les autres informations du client
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
