package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Equipement> retrieveAllEquipements() {
        return equipementRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Equipement retrieveEquipement(Long idEquipement) {
        return equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", idEquipement));
    }

    @Override
    @Transactional
    public Equipement addEquipement(Equipement equipement) {
        equipement.setIdEquipement(null); // l'identifiant est genere par la base (IDENTITY)
        return equipementRepository.save(equipement);
    }

    @Override
    @Transactional
    public Equipement updateEquipement(Long idEquipement, Equipement equipement) {
        Equipement existant = retrieveEquipement(idEquipement);
        // On ne copie que les attributs simples : les associations se gerent via les services d'affectation.
        existant.setLibelle(equipement.getLibelle());
        return equipementRepository.save(existant);
    }

    @Override
    @Transactional
    public void removeEquipement(Long idEquipement) {
        Equipement existant = retrieveEquipement(idEquipement);
        // Cote inverse du ManyToMany : on retire l'equipement de chaque vehicule (cote proprietaire).
        existant.getVehicules().forEach(v -> v.getEquipements().remove(existant));
        equipementRepository.delete(existant);
    }
}
