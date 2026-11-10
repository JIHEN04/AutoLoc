package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IPaiementRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Paiement> retrieveAllPaiements() {
        return paiementRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Paiement retrievePaiement(Long idPaiement) {
        return paiementRepository.findById(idPaiement)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement", idPaiement));
    }

    @Override
    @Transactional
    public Paiement addPaiement(Paiement paiement) {
        paiement.setIdPaiement(null); // l'identifiant est genere par la base (IDENTITY)
        if (paiement.getDatePaiement() == null) {
            paiement.setDatePaiement(LocalDate.now());
        }
        return paiementRepository.save(paiement);
    }

    @Override
    @Transactional
    public Paiement updatePaiement(Long idPaiement, Paiement paiement) {
        Paiement existant = retrievePaiement(idPaiement);
        // On ne copie que les attributs simples : les associations se gerent via les services d'affectation.
        existant.setMontant(paiement.getMontant());
        existant.setDatePaiement(paiement.getDatePaiement());
        existant.setModePaiement(paiement.getModePaiement());
        return paiementRepository.save(existant);
    }

    @Override
    @Transactional
    public void removePaiement(Long idPaiement) {
        Paiement existant = retrievePaiement(idPaiement);
        paiementRepository.delete(existant);
    }
}
