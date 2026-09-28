# AutoLoc — autoloc-api

UP ASI — Atelier 1 (Séance 2) : démarrage du projet Spring Boot + Maven et entités JPA.

Stack : Java 17, Maven, Spring Boot 3.5, Spring Data JPA (Hibernate), MySQL, Lombok, Validation, DevTools.

## Ouvrir le projet dans IntelliJ IDEA

1. `File → Open…` → sélectionner le dossier `autoloc-api` (celui qui contient `pom.xml`) → **Open as Project**.
2. Attendre la fin de l'import Maven (barre de progression en bas à droite).
3. `File → Project Structure → Project` : SDK = JDK 17 (ou supérieur).
4. `Settings → Build, Execution, Deployment → Compiler → Annotation Processors` : cocher **Enable annotation processing**.
5. Vérifier que le plugin **Lombok** est installé (`Settings → Plugins`).

## Base de données

MySQL doit être démarré sur `localhost:3306`. La base `autoloc_db` est créée automatiquement
grâce à `createDatabaseIfNotExist=true`.

Le mot de passe n'est pas écrit en clair dans `application.properties` : il est lu depuis
la variable d'environnement `DB_PASSWORD` (utilisateur : `DB_USERNAME`, `root` par défaut).

Dans IntelliJ : `Run → Edit Configurations… → AutolocApiApplication → Environment variables` :

```
DB_PASSWORD=votre_mot_de_passe
```

## Lancer l'application

Ouvrir `AutolocApiApplication.java` → bouton ▶. La console doit afficher
`Started AutolocApiApplication in … seconds` ainsi que les requêtes `create table …`.

Profil **dev** (facultatif) : dans la Run Configuration, `Active profiles` = `dev`.
Il active des logs plus détaillés et insère 3 véhicules de démonstration si la table est vide.

## Vérification en base

```sql
USE autoloc_db;
SHOW TABLES;
DESCRIBE vehicule;
SELECT * FROM vehicule;
```

Tables attendues (10) : `agence`, `client`, `contrat`, `employe`, `equipement`,
`maintenance`, `paiement`, `reservation`, `vehicule` + la table de jointure `vehicule_equipement`.

```sql
SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'autoloc_db';  -- 10
```

## Structure des packages

```
tn.esprit.autoloc
├── config          DevDataInitializer (profil dev)
├── domain          9 entités JPA + 5 énumérations
├── repository      VehiculeRepository
├── service         (Atelier 4)
├── web.controller  (Atelier 5)
└── web.dto         (Atelier 6)
```

## Atelier 2 — Associations

| Association | Mapping JPA | Clé étrangère / table |
|---|---|---|
| Agence 1 — * Vehicule | `@ManyToOne` (Vehicule) / `@OneToMany(mappedBy)` (Agence) | `vehicule.id_agence` |
| Agence 1 — * Employe | `@ManyToOne` (Employe) / `@OneToMany(mappedBy)` (Agence) | `employe.id_agence` |
| Vehicule 1 — * Maintenance | `@ManyToOne` (Maintenance) / `@OneToMany(cascade = ALL)` (Vehicule) | `maintenance.id_vehicule` |
| Vehicule * — * Equipement | `@ManyToMany` + `@JoinTable` (Vehicule) / `mappedBy` (Equipement) | table `vehicule_equipement` |
| Vehicule 1 — * Reservation | `@ManyToOne` (Reservation) / `@OneToMany(mappedBy)` (Vehicule) | `reservation.id_vehicule` |
| Client 1 — * Reservation | `@ManyToOne` (Reservation) / `@OneToMany(mappedBy)` (Client) | `reservation.id_client` |
| Reservation 1 — 1 Contrat | `@OneToOne` (Contrat) / `mappedBy` (Reservation) | `contrat.id_reservation` (unique) |
| Contrat 1 ◆— * Paiement | `@ManyToOne` (Paiement) / `@OneToMany(cascade = ALL, orphanRemoval = true)` (Contrat) | `paiement.id_contrat` |

- Tous les `@ManyToOne` / `@OneToOne` propriétaires sont en `FetchType.LAZY` (par défaut ils sont EAGER).
- Cascade uniquement quand l'enfant ne peut pas exister sans son parent : composition Contrat–Paiement,
  historique de maintenance d'un véhicule.
- `mappedBy` désigne le côté inverse (sans clé étrangère) et évite des tables de jointure inutiles.

Procédure : `ddl-auto=create` → lancer l'application (drop + create des tables) → vérifier 10 tables →
remettre `ddl-auto=update`.

## Choix techniques

- `@Getter` / `@Setter` / `@NoArgsConstructor` / `@AllArgsConstructor` au lieu de `@Data`,
  pour éviter les boucles `equals/hashCode/toString` quand les associations bidirectionnelles
  seront ajoutées (Atelier 2).
- `GenerationType.IDENTITY` : colonne `AUTO_INCREMENT` native de MySQL.
- `@Enumerated(EnumType.STRING)` : la valeur texte de l'énumération est stockée (et non l'ordinal),
  ce qui reste correct si l'ordre des constantes change.
- `BigDecimal` pour les montants (pas de `double`, source d'erreurs d'arrondi).
- `LocalDate` / `LocalDateTime` (API `java.time`) pour les dates.
- `ddl-auto=update` : adapté au développement uniquement.

## Commits Git

```
git add .
git commit -m "Atelier 1 : init projet Spring Boot + entité Vehicule"
git commit -m "Prépa Atelier 2 : entités restantes sans associations"
git push
```
