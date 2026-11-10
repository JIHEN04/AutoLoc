package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Agence> retrieveAllAgences() {
        return agenceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Agence retrieveAgence(Long idAgence) {
        return agenceRepository.findById(idAgence)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", idAgence));
    }

    @Override
    @Transactional
    public Agence addAgence(Agence agence) {
        agence.setIdAgence(null); // l'identifiant est genere par la base (IDENTITY)
        return agenceRepository.save(agence);
    }

    @Override
    @Transactional
    public Agence updateAgence(Long idAgence, Agence agence) {
        Agence existant = retrieveAgence(idAgence);
        // On ne copie que les attributs simples : les associations se gerent via les services d'affectation.
        existant.setNom(agence.getNom());
        existant.setVille(agence.getVille());
        existant.setAdresse(agence.getAdresse());
        existant.setTelephone(agence.getTelephone());
        return agenceRepository.save(existant);
    }

    @Override
    @Transactional
    public void removeAgence(Long idAgence) {
        Agence existant = retrieveAgence(idAgence);
        // Pas de cascade : on detache d'abord les vehicules et employes de l'agence.
        existant.getVehicules().forEach(v -> v.setAgence(null));
        existant.getEmployes().forEach(e -> e.setAgence(null));
        agenceRepository.delete(existant);
    }
}
