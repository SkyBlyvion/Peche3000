# Peche3000

## Documentation 
    - PreSetup 2h:
        - Dictionnaire des données
        - MCD, MLD

    - Setup 1h: 
        - creation repo, git init, install deps, configure bdd
        - SDK : correto-17; Amazon Corretto 17.0.13

    - Backend Dev 3h:
        - créer les entités et enums
        - créer les services, repositories et controllers
        - implementer Login et Register

    - Frontend Dev 2h:
        - design templates
        - style templates

    - Navigation 1h:
        - navigation
        
    - Reste des pages et customLogic ...h until 18/11/24

### Creation du Projet :
    GroupID: com.magasinpeche
    ArtifactID: SpringBootPeche3000LN

    sudo mysql -u root -p

    créer la bdd DBMagasinPeche :
        CREATE DATABASE DBMagasinPeche;

    créer un user :
        CREATE USER 'user'@'localhost' IDENTIFIED BY 'admin';

    donner les droits :
        GRANT ALL PRIVILEGES ON DBMagasinPeche.* TO 'user'@'localhost';

    Flusher :   
        FLUSH PRIVILEGES;

#### Installer Maven : 
``sudo apt install maven`` 
puis check la version
``mvn -v``

#### installer les dépendences :
    - spring-boot-starter-data-jpa
    - spring-boot-starter-security
    - spring-boot-starter-thymeleaf
    - spring-boot-starter-web
    - thymeleaf-extras-springsecurity6
    - mariadb-java-client
    - spring-boot-starter-test
    - spring-security-test
    - spring-boot-devtools

### Entités, enums, services, repositories, controllers, etc.

### Templates, styles, js etc.



## Ressources 
### Dictionnaire des données (entités principales) :

Produit :
id : Identifiant unique (clé primaire)
nom : Nom du produit
description : Description du produit
prix : Prix du produit
stock : Quantité disponible en stock
categorie : Catégorie du produit (ex: cannes à pêche, appâts)
imageUrl : URL de l'image du produit
DateCreation : Date de création du produit

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
statut : Statut de la demande (en attente, approuve, rejeté)
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