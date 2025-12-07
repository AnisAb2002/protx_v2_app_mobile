---------------------------------------------------------------
-- ProtX – Application Mobile de Vente de Produits Sportifs  --
---------------------------------------------------------------

ProtX est une application Android permettant aux utilisateurs de parcourir des produits sportifs, les ajouter au panier,
passer des commandes et gérer leur profil. Le projet utilise Firebase Firestore comme base de données ainsi que Room pour la gestion locale du panier.

Fonctionnalités :
- un panier.
- ajout des produits au panier
- modifier la quantité
- calcul du total
- suppression d’un article
- vider entièrement le panier
- passer une commande via un paiement simulé
- sauvegarde des commandes dans Firestore
- affichage de l’historique des commandes
- consultation des détails d'une commande
- s'inscrire / s'authentifier
- se déconnecter
- modifier le mot de passe
- stockage des données utilisateur via SharedPreferences
- modifier les informations de l'utilisateur
- supprimer son compte
- Rechercher un produit.
- Demander une assistance.
- changer la langue
  
Base de données :
- Firebase Firestore : stockage des produits, utilisateurs et commandes.
- Room : panier en local

Vérification de connexion :
avec des messages d’erreur en cas de perte de connexion

Multilangue :
- Français
- Anglais

Technologies utilisées :
- Kotlin	Développement Android
- Android Jetpack (Room, Fragment, ViewBinding)	Architecture locale
- Firebase Firestore	Base de données cloud
- Coroutines	Traitement en arrière-plan
- Material Design	Interface utilisateur

- Cloner le projet :
git clone https://github.com/ton-compte/protx.git
Puis ouvrir le projet dans Android Studio et ajouter le fichier google-services.json dans app/
Vérifier que Firestore est activé dans la console Firebase.
Lancer l'application sur un appareil physique ou un émulateur.


Fonctionnement :
--- Firestore :
- les produits chargés depuis collection("produits")
- les commandes sont stockées dans collection("commandes")
- liens avec l’utilisateur via id du client

--- Room (Panier) :
- stocke localement les ids produits + quantités

--- Paiement :
- le paiement est simulé avec un vérification de :
- Numéro de carte
- date d'expiration de la carte au format MM/YY
- CVC
- ajout de la commande dans Firestore

Application disponible en :
- Français
- Anglais

Améliorations possibles :
- authentification Firebase Auth
- Paiement avec API Stripe
- Notifications (commandes, promos)
- système de favoris

Projet réalisé par Anis ABDAT, étudiant en licence 3 Informatique des Systèmes Embarqués et Interactifs.
Université Paris 8 Vincennes - Saint-Denis
