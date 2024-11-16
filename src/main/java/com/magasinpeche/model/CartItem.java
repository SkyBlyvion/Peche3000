package com.magasinpeche.model;

public class CartItem {
    private Produit produit;
    private int quantity;

    // Constructeurs
    public CartItem(Produit produit, int quantity) {
        this.produit = produit;
        this.quantity = quantity;
    }

    // Getters et Setters
    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
