package com.magasinpeche.model;

public enum Categorie {
    // Catégories principales pour les articles de pêche
    CANNE_A_PECHE("Canne à pêche"),
    MOULINET("Moulinet"),
    FIL_DE_PECHE("Fil de pêche"),
    HAMECONS("Hameçons"),
    APPATS("Appâts"),

    // Accessoires
    EPUISETTE("Épuisette"),
    SEAU("Seau"),
    COUTEAU("Couteau"),

    // Vêtements et équipements
    CHAPEAU("Chapeau"),
    VETEMENT("Vêtement"),
    GANTS("Gants");

    private final String displayName;

    // Constructeur pour l'enum
    Categorie(String displayName) {
        this.displayName = displayName;
    }

    // Getter pour le nom affiché
    public String getDisplayName() {
        return displayName;
    }

    // Méthode utilitaire pour rechercher une catégorie par son nom affiché
    public static Categorie fromDisplayName(String displayName) {
        for (Categorie categorie : Categorie.values()) {
            if (categorie.getDisplayName().equalsIgnoreCase(displayName)) {
                return categorie;
            }
        }
        throw new IllegalArgumentException("Catégorie inconnue : " + displayName);
    }
}
