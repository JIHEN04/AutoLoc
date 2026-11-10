package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Employe> retrieveAllEmployes() {
        return employeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Employe retrieveEmploye(Long idEmploye) {
        return employeRepository.findById(idEmploye)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", idEmploye));
    }

    @Override
    @Transactional
    public Employe addEmploye(Employe employe) {
        employe.setIdEmploye(null); // l'identifiant est genere par la base (IDENTITY)
        return employeRepository.save(employe);
    }

    @Override
    @Transactional
    public Employe updateEmploye(Long idEmploye, Employe employe) {
        Employe existant = retrieveEmploye(idEmploye);
        // On ne copie que les attributs simples : les associations se gerent via les services d'affectation.
        existant.setNom(employe.getNom());
        existant.setPrenom(employe.getPrenom());
        existant.setRole(employe.getRole());
        return employeRepository.save(existant);
    }

    @Override
    @Transactional
    public void removeEmploye(Long idEmploye) {
        Employe existant = retrieveEmploye(idEmploye);
        employeRepository.delete(existant);
    }

    // =====================================================================
    //  Affectation / desaffectation : @ManyToOne Employe -> Agence
    // =====================================================================

    @Override
    @Transactional
    public Employe desaffecterEmploye(Long idEmploye) {
        Employe employe = retrieveEmploye(idEmploye);
        Agence ancienneAgence = employe.getAgence();
        if (ancienneAgence == null) {
            throw new IllegalStateException("L'employe " + idEmploye + " n'est affecte a aucune agence.");
        }
        // Coherence bidirectionnelle : on met a jour les deux cotes de l'association.
        ancienneAgence.getEmployes().remove(employe);
        employe.setAgence(null);
        return employeRepository.save(employe);
    }

    @Override
    @Transactional
    public Employe affecterEmploye(Long idEmploye, Long idAgence) {
        Employe employe = retrieveEmploye(idEmploye);
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", idAgence));

        // Regle metier : un employe n'appartient qu'a une seule agence a la fois.
        // Un transfert passe donc d'abord par une desaffectation.
        if (employe.getAgence() != null) {
            if (employe.getAgence().getIdAgence().equals(idAgence)) {
                return employe; // deja affecte a cette agence : rien a faire
            }
            throw new IllegalStateException("L'employe " + idEmploye + " appartient deja a l'agence "
                    + employe.getAgence().getIdAgence() + " : il faut d'abord le desaffecter.");
        }

        employe.setAgence(agence);          // cote proprietaire (cle etrangere agence_id)
        agence.getEmployes().add(employe);  // cote inverse (coherence en memoire)
        return employeRepository.save(employe);
    }
}
