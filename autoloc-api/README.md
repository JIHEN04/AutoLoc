# AutoLoc — Plateforme de location de véhicules multi-agences

Projet fil rouge du module ASI (Architecture des Systèmes d'Information) — ESPRIT 2026-2027.

## Objectif du projet
AutoLoc est une entreprise de location de véhicules disposant de plusieurs agences.
L'objectif est de numériser l'ensemble du processus : réservation, contractualisation,
facturation, suivi de flotte et relances automatiques, via une API REST Spring Boot.

## Acteurs et cas d'utilisation

### Client
- Consulter les véhicules disponibles (par ville, catégorie, période)
- Créer / annuler une réservation
- Consulter ses contrats

### Agent d'agence
- Gérer les véhicules de l'agence
- Valider une réservation
- Établir un contrat
- Enregistrer un paiement

### Responsable d'agence (Manager)
- Tous les droits de l'agent
- Gérer les employés de l'agence
- Consulter les statistiques de l'agence

### Administrateur
- Gérer les agences et les catégories de véhicules
- Consulter les statistiques globales
- Configurer la plateforme

## Stack technique
Java 17+ · Spring Boot · Spring Data JPA · MariaDB/MySQL (XAMPP) · Lombok · Maven · Git

## Avancement
- [x] Atelier 0 : environnement de développement
- [x] Atelier 1 : projet Spring Boot + 9 entités JPA (sans associations)
- [x] Atelier 2 : associations, cascade et fetch
- [x] Atelier 3 : repositories, services (interfaces + implémentations) et CRUD complet de Vehicule et Client

## Lancer le projet
1. Démarrer MySQL (XAMPP)
2. Lancer `AutolocApiApplication` (serveur sur le port 8081)
3. La base `autoloc_db` et ses tables sont créées automatiquement

## Auteur
Prénom Nom — Classe