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

Tables attendues (9) : `agence`, `client`, `contrat`, `employe`, `equipement`,
`maintenance`, `paiement`, `reservation`, `vehicule`.

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
