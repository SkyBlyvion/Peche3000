package com.magasinpeche.controller;

import com.magasinpeche.model.Categorie;
import com.magasinpeche.model.Concours;
import com.magasinpeche.model.Produit;
import com.magasinpeche.repository.ConcoursRepository;
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

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ProduitService produitService;

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/produits")
    public String getAllProduits(Model model) {

        return "admin/produits/liste_produits";
    }

    @GetMapping("/produits/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {

        return produit != null ? ResponseEntity.ok(produit) : ResponseEntity.notFound().build();
    }

    @GetMapping("/produits/ajouter")
    public String afficherFormulaireAjout(Model model) {

        return "admin/produits/ajouter_produit";
    }

    @PostMapping("/produits/ajouter")
    public String createProduit(@ModelAttribute Produit produit, @RequestParam("imageFile") MultipartFile imageFile) {


        return "redirect:/admin/produits"; // Redirection vers la liste des produits dans le sous-chemin admin
    }

    @GetMapping("/produits/modifier/{id}")
    public String afficherFormulaireModification(@PathVariable("id") Long id, Model model) {

        return "redirect:/admin/produits";
    }

    @PostMapping("/produits/{id}")
    public String updateProduit(@PathVariable("id") Long id,
                                @ModelAttribute Produit produitDetails,
                                @RequestParam("imageFile") MultipartFile imageFile) {

        return "redirect:/admin/produits";
    }

    @GetMapping("/produits/supprimer/{id}")
    public String supprimerProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return "admin/produits/confirmation_suppression";
    }

    @DeleteMapping("/produits/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }


// OtherPage: Concour

    @Autowired
    private ConcoursRepository concoursRepository;


    @GetMapping("/concours")
    public String afficherConcours(Model model) {

        return "admin/concours/liste";
    }


    @GetMapping("/concours/ajouter")
    public String afficherFormulaireConcours(Model model) {

        return "admin/concours/form";
    }


    @GetMapping("/concours/modifier/{id}")
    public String afficherFormulaireModificationConcours(@PathVariable Long id, Model model) {

        return "admin/concours/form";
    }


    @PostMapping("/concours/soumettre")
    public String soumettreConcours(@RequestParam(required = false) Long id,
                                    @RequestParam String nom,
                                    @RequestParam String date,
                                    @RequestParam String lieu,
                                    @RequestParam String description) {

        return "redirect:/admin/concours";
    }


    @GetMapping("/concours/supprimer/{id}")
    public String supprimerConcours(@PathVariable Long id) {
        return "redirect:/admin/concours";
    }
}
