package com.magasinpeche.model;

import jakarta.persistence.*;

@Entity
public class LigneCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation avec la commande
    @ManyToOne
    @JoinColumn(name="commande_id")
    private Commande commande;

    // Relation avec le produit
    @ManyToOne
    @JoinColumn(name="produit_id")
    private Produit produit;

    private int quantite;
    private double prixUnitaire;

    // Constructeur par défaut
    public LigneCommande() {}

    // Constructeur avec paramètres (facultatif)
    public LigneCommande(Commande commande, Produit produit, int quantite, double prixUnitaire) {
        this.commande = commande;
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    // Pas de setter pour 'id' car il est généré automatiquement

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
}
