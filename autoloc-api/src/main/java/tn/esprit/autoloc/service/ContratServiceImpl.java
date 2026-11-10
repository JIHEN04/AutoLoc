package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> retrieveAllContrats() {
        return contratRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Contrat retrieveContrat(Long idContrat) {
        return contratRepository.findById(idContrat)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", idContrat));
    }

    @Override
    @Transactional
    public Contrat addContrat(Contrat contrat) {
        contrat.setIdContrat(null); // l'identifiant est genere par la base (IDENTITY)
        // Composition : chaque paiement recu doit connaitre son contrat (cascade ALL).
        contrat.getPaiements().forEach(p -> p.setContrat(contrat));
        return contratRepository.save(contrat);
    }

    @Override
    @Transactional
    public Contrat updateContrat(Long idContrat, Contrat contrat) {
        Contrat existant = retrieveContrat(idContrat);
        // On ne copie que les attributs simples : les associations se gerent via les services d'affectation.
        existant.setDateSignature(contrat.getDateSignature());
        existant.setMontantTotal(contrat.getMontantTotal());
        existant.setValide(contrat.isValide());
        return contratRepository.save(existant);
    }

    @Override
    @Transactional
    public void removeContrat(Long idContrat) {
        Contrat existant = retrieveContrat(idContrat);
        contratRepository.delete(existant);
    }
}
