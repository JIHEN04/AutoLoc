package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Employe;

import java.util.List;

public interface IEmployeService {

    List<Employe> retrieveAllEmployes();

    Employe retrieveEmploye(Long idEmploye);

    Employe addEmploye(Employe employe);

    Employe updateEmploye(Long idEmploye, Employe employe);

    void removeEmploye(Long idEmploye);

    // ===== Affectation / desaffectation (@ManyToOne Employe -> Agence) =====

    /** Scenario 1 : retire l'employe de son agence actuelle (etape obligatoire avant une mutation). */
    Employe desaffecterEmploye(Long idEmploye);

    /** Scenario 1 : rattache l'employe a une agence (ex. mutation vers l'agence de Tunis). */
    Employe affecterEmploye(Long idEmploye, Long idAgence);
}
