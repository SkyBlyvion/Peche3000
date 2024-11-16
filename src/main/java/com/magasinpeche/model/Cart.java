package com.magasinpeche.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items = new ArrayList<>();

    // Méthodes pour gérer le panier
    public void addItem(Produit produit, int quantity) {
        for (CartItem item : items) {
            if (item.getProduit().getId().equals(produit.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(produit, quantity));
    }

    public void removeItem(Long produitId) {
        items.removeIf(item -> item.getProduit().getId().equals(produitId));
    }

    public void updateItem(Long produitId, int quantity) {
        for (CartItem item : items) {
            if (item.getProduit().getId().equals(produitId)) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    public double getTotal() {
        return items.stream()
                .mapToDouble(item -> item.getProduit().getPrix() * item.getQuantity())
                .sum();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }
}
