package com.magasinpeche.controller;

import com.magasinpeche.model.*;
import com.magasinpeche.repository.ConcoursRepository;
import com.magasinpeche.repository.ParticipationRepository;
import com.magasinpeche.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// Définition du contrôleur pour gérer les actions administratives.
@Controller
@RequestMapping("/admin")
public class AdminController {

    // Injection de dépendance pour le service Produit
    @Autowired
    private ProduitService produitService;

    // Chemin statique pour le répertoire de téléchargement des fichiers
    private static final String UPLOAD_DIR = "uploads/";

    // Affiche la liste de tous les produits dans l'interface admin
    @GetMapping("/produits")
    public String getAllProduits(Model model) {
        List<Produit> produits = produitService.getAllProduits(); // Récupère tous les produits
        model.addAttribute("produits", produits); // Ajoute les produits au modèle pour l'affichage
        return "admin/produits/liste_produits"; // Retourne la vue pour lister les produits
    }

    // Récupère un produit spécifique par son ID et le retourne en réponse JSON
    @GetMapping("/produits/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        Produit produit = produitService.getProduitById(id); // Recherche le produit par ID
        return produit != null ? ResponseEntity.ok(produit) : ResponseEntity.notFound().build(); // Renvoie 404 si non trouvé
    }

    // Affiche le formulaire pour ajouter un produit
    @GetMapping("/produits/ajouter")
    public String afficherFormulaireAjout(Model model) {
        model.addAttribute("produit", new Produit()); // Initialise un nouveau produit vide pour le formulaire
        return "admin/produits/ajouter_produit"; // Vue pour ajouter un produit
    }

    // Ajoute un nouveau produit avec la gestion de l'upload d'image
    @PostMapping("/produits/ajouter")
    public String createProduit(@ModelAttribute Produit produit, @RequestParam("imageFile") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) { // Vérifie si un fichier image a été uploadé
            try {
                String originalFileName = imageFile.getOriginalFilename(); // Nom original du fichier
                Path filePath = Paths.get(UPLOAD_DIR + originalFileName); // Chemin pour sauvegarder le fichier
                int count = 1;

                // Gestion des conflits de noms de fichiers
                while (Files.exists(filePath)) {
                    String newFileName = originalFileName.replaceFirst("(\\.[^\\.]+)$", "_" + count + "$1");
                    filePath = Paths.get(UPLOAD_DIR + newFileName);
                    count++;
                }

                Files.copy(imageFile.getInputStream(), filePath); // Sauvegarde du fichier
                produit.setImageUrl("/" + UPLOAD_DIR + filePath.getFileName().toString()); // Définit l'URL de l'image pour le produit
            } catch (Exception e) {
                e.printStackTrace(); // Gère les erreurs d'upload
            }
        }
        produitService.createProduit(produit); // Enregistre le produit dans la base de données
        return "redirect:/admin/produits"; // Redirection vers la liste des produits
    }

    // Affiche le formulaire pour modifier un produit
    @GetMapping("/produits/modifier/{id}")
    public String afficherFormulaireModification(@PathVariable("id") Long id, Model model) {
        Produit produit = produitService.getProduitById(id); // Recherche le produit par ID
        if (produit != null) {
            model.addAttribute("produit", produit); // Ajoute le produit au modèle
            model.addAttribute("categorie", Categorie.values()); // Ajoute les catégories disponibles
            return "admin/produits/modifier_produit"; // Vue pour modifier le produit
        }
        return "redirect:/admin/produits"; // Redirection si le produit n'existe pas
    }

    // Met à jour un produit existant avec gestion de l'upload d'image
    @PostMapping("/produits/{id}")
    public String updateProduit(@PathVariable("id") Long id,
                                @ModelAttribute Produit produitDetails,
                                @RequestParam("imageFile") MultipartFile imageFile) {
        Produit produitExist = produitService.getProduitById(id); // Recherche le produit existant

        if (produitExist != null) {
            if (!imageFile.isEmpty()) { // Vérifie si un nouveau fichier image a été uploadé
                try {
                    String originalFileName = imageFile.getOriginalFilename();
                    Path filePath = Paths.get(UPLOAD_DIR + originalFileName);
                    int count = 1;

                    while (Files.exists(filePath)) {
                        String newFileName = originalFileName.replaceFirst("(\\.[^\\.]+)$", "_" + count + "$1");
                        filePath = Paths.get(UPLOAD_DIR + newFileName);
                        count++;
                    }

                    Files.copy(imageFile.getInputStream(), filePath); // Sauvegarde du fichier
                    produitDetails.setImageUrl("/" + UPLOAD_DIR + filePath.getFileName().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                produitDetails.setImageUrl(produitExist.getImageUrl()); // Conserve l'image existante si aucune nouvelle image
            }

            produitService.updateProduit(id, produitDetails); // Met à jour le produit dans la base
        }

        return "redirect:/admin/produits"; // Redirection vers la liste des produits
    }

    // Supprime un produit par ID via une interface web
    @GetMapping("/produits/supprimer/{id}")
    public String supprimerProduit(@PathVariable Long id) {
        produitService.deleteProduit(id); // Supprime le produit
        return "redirect:/admin/produits"; // Redirection
    }

    // Supprime un produit par ID via une API REST
    @DeleteMapping("/produits/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id); // Supprime le produit
        return ResponseEntity.noContent().build(); // Renvoie une réponse vide (204)
    }

    // SECTION CONCOURS

    @Autowired
    private ConcoursRepository concoursRepository; // Injection du repository pour les concours

    // Affiche la liste des concours à venir
    @GetMapping("/concours")
    public String afficherConcours(Model model) {
        LocalDate today = LocalDate.now(); // Date actuelle
        model.addAttribute("concoursList", concoursRepository.findUpcomingConcoursSortedByDate(today)); // Concours à venir
        return "admin/concours/liste"; // Vue pour afficher les concours
    }

    // Affiche le formulaire pour ajouter un concours
    @GetMapping("/concours/ajouter")
    public String afficherFormulaireConcours(Model model) {
        model.addAttribute("concours", new Concours()); // Initialise un nouveau concours
        return "admin/concours/ajouter_concours"; // Vue pour ajouter un concours
    }

    // Affiche le formulaire pour modifier un concours
    @GetMapping("/concours/modifier/{id}")
    public String afficherFormulaireModificationConcours(@PathVariable Long id, Model model) {
        Concours concours = concoursRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Concours invalide"));
        model.addAttribute("concours", concours); // Ajoute le concours au modèle
        return "admin/concours/modifier_concours"; // Vue pour modifier un concours
    }

    // Soumet un nouveau concours ou met à jour un concours existant
    @PostMapping("/concours/soumettre")
    public String soumettreConcours(@RequestParam(required = false) Long id,
                                    @RequestParam String nom,
                                    @RequestParam String date,
                                    @RequestParam String lieu,
                                    @RequestParam String description) {
        Concours concours;
        if (id != null) {
            concours = concoursRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Concours invalide")); // Modification
        } else {
            concours = new Concours(); // Création
        }
        concours.setNom(nom);
        concours.setDate(LocalDate.parse(date));
        concours.setLieu(lieu);
        concours.setDescription(description);
        concoursRepository.save(concours); // Sauvegarde
        return "redirect:/admin/concours"; // Redirection
    }

    // Supprime un concours
    @GetMapping("/concours/supprimer/{id}")
    public String supprimerConcours(@PathVariable Long id) {
        Concours concours = concoursRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Concours invalide"));
        concoursRepository.delete(concours); // Supprime le concours
        return "redirect:/admin/concours"; // Redirection
    }
}
