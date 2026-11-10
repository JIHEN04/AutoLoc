package tn.esprit.autoloc.exception;

/** Levee quand une entite recherchee par son identifiant n'existe pas (HTTP 404). */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String entite, Long id) {
        super(entite + " introuvable avec l'id " + id);
    }
}
