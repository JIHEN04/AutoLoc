# AutoLoc API — UP ASI (Esprit)

Plateforme de gestion de location de véhicules multi-agences (Spring Boot 3 / Java 17 / MySQL).

## Contenu par atelier

| Atelier | Travail réalisé | Où le trouver |
|---|---|---|
| Atelier 1 | 9 entités JPA + 5 énumérations (Lombok ciblé, IDENTITY) | `domain/` |
| Atelier 2 | Associations (@OneToMany, @ManyToOne, @ManyToMany, @OneToOne), cascade et fetch | `domain/` |
| Atelier 3 | 9 repositories, 9 interfaces de service, 9 implémentations (CRUD complet) | `repository/`, `service/` |
| Atelier 4 | RestControllers Agence, Vehicule, Client (+ Employe, Equipement), Swagger, @Operation / @ApiResponse | `web/controller/`, `config/OpenApiConfig` |
| Affectations | `affecterEmploye`, `desaffecterEmploye`, `affecterVehicule`, `affecterEquipement`, `desaffecterEquipement` avec `@Transactional` | `EmployeServiceImpl`, `VehiculeServiceImpl` |

## Tables générées (Atelier 2)

10 tables : `agence`, `vehicule`, `employe`, `equipement`, `client`, `reservation`, `contrat`, `paiement`, `maintenance` (9 entités) + `vehicule_equipement` (table de jointure du @ManyToMany).

Vérification dans MySQL :
```sql
USE autoloc_db;
SHOW TABLES;
```

## Lancer le projet

1. Démarrer MySQL (XAMPP, WAMP ou MySQL Server).
2. IntelliJ : **File → Open** → choisir le dossier `autoloc-api` → **Open as Project**. Attendre la fin de l'import Maven.
3. Activer Lombok : **Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing**.
4. Mot de passe MySQL dans `src/main/resources/application.properties` :
   `spring.datasource.password=${DB_PASSWORD:}` → mettre votre mot de passe après les `:` (vide pour XAMPP).
5. Lancer `AutolocApiApplication`. La base `autoloc_db` et les tables sont créées automatiquement, puis des données de démonstration sont insérées.

## Tester

- Swagger : http://localhost:8080/swagger-ui.html
- Postman : importer `postman/AutoLoc.postman_collection.json`

## Scénarios d'affectation (données de démonstration)

| Scénario | Requêtes |
|---|---|
| 1. Mutation de l'agent Ahmed (Sousse) vers Tunis | `PUT /api/employes/1/desaffecter` puis `PUT /api/employes/1/affecter/1` |
| 2. Véhicule neuf (Kia Sportage) rattaché à Sfax | `PUT /api/vehicules/3/affecter-agence/2` |
| 3. Siège bébé installé puis retiré | `PUT /api/vehicules/1/affecter-equipement/2` puis `PUT /api/vehicules/1/desaffecter-equipement/2` |

Règle métier du scénario 1 : un employé n'appartient qu'à une seule agence. Si on l'affecte alors qu'il est déjà dans une autre agence, l'API répond **409** et demande de le désaffecter d'abord.

Les codes d'erreur : 404 (élément introuvable), 409 (règle métier ou contrainte d'intégrité).
