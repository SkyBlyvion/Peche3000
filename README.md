# Peche3000

## Documentation 

### Creation du Projet 
Étape 1 : Analyse de la donnée (Dictionnaire des données)
Dans cette étape, nous allons identifier les différentes entités (objets métier) de l'application et leurs propriétés.

Dictionnaire des données (entités principales) :

Produit :
id : Identifiant unique (clé primaire)
nom : Nom du produit
categorie : Catégorie du produit (ex: cannes à pêche, appâts)
description : Description du produit
prix : Prix du produit
stock : Quantité disponible en stock
imageUrl : URL de l'image du produit

Client :
id : Identifiant unique (clé primaire)
nom : Nom du client
prenom : Prénom du client
email : Adresse email du client
adresse : Adresse postale du client
telephone : Numéro de téléphone
historiqueCommandes : Liste des commandes passées par le client

Commande :
id : Identifiant unique (clé primaire)
client : Référence au client (clé étrangère)
produits : Liste des produits commandés
dateCommande : Date de la commande
total : Montant total de la commande
statut : Statut de la commande (en attente, expédiée, livrée)

Permis de pêche :
id : Identifiant unique (clé primaire)
client : Référence au client (clé étrangère)
statut : Statut de la demande (en attente, approuvé, rejeté)
dateDemande : Date de la demande

Concours :
id : Identifiant unique (clé primaire)
nom : Nom du concours
date : Date du concours
lieu : Lieu du concours
description : Description du concours
participants : Liste des participants (référence à l'entité Client)

Relations identifiées :
Un Client peut passer plusieurs Commandes.
Un Client peut faire plusieurs demandes de Permis de pêche.
Un Client peut participer à plusieurs Concours.
Une Commande peut contenir plusieurs Produits.