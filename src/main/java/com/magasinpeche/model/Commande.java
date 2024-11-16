package com.magasinpeche.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private LocalDateTime dateCommande;
    private String statut; // 'en_attente', 'envoyee', 'livree', 'annulee'
    private Double total;

    // Liste des lignes de commande
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<LigneCommande> lignesCommande;

    // Constructeurs
    public Commande() {
    }

    public Commande(Client client, LocalDateTime dateCommande, String statut, Double total) {
        this.client = client;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.total = total;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    //public void setId(Long id) {
    //    this.id = id;
    //}

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<LigneCommande> getLignesCommande() {
        return lignesCommande;
    }

    public void setLignesCommande(List<LigneCommande> lignesCommande) {
        this.lignesCommande = lignesCommande;
    }
}
