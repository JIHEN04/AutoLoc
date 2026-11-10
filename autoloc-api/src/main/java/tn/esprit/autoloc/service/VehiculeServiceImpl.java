package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> retrieveAllVehicules() {
        return vehiculeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicule retrieveVehicule(Long idVehicule) {
        return vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", idVehicule));
    }

    @Override
    @Transactional
    public Vehicule addVehicule(Vehicule vehicule) {
        vehicule.setIdVehicule(null); // l'identifiant est genere par la base (IDENTITY)
        if (vehicule.getStatut() == null) {
            vehicule.setStatut(StatutVehicule.DISPONIBLE);
        }
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional
    public Vehicule updateVehicule(Long idVehicule, Vehicule vehicule) {
        Vehicule existant = retrieveVehicule(idVehicule);
        // On ne copie que les attributs simples : les associations se gerent via les services d'affectation.
        existant.setImmatriculation(vehicule.getImmatriculation());
        existant.setMarque(vehicule.getMarque());
        existant.setModele(vehicule.getModele());
        existant.setCategorie(vehicule.getCategorie());
        existant.setTarifJournalier(vehicule.getTarifJournalier());
        existant.setStatut(vehicule.getStatut());
        return vehiculeRepository.save(existant);
    }

    @Override
    @Transactional
    public void removeVehicule(Long idVehicule) {
        Vehicule existant = retrieveVehicule(idVehicule);
        vehiculeRepository.delete(existant);
    }

    // =====================================================================
    //  Affectation : @ManyToOne Vehicule -> Agence
    // =====================================================================

    @Override
    @Transactional
    public Vehicule affecterVehicule(Long idVehicule, Long idAgence) {
        Vehicule vehicule = retrieveVehicule(idVehicule);
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", idAgence));

        if (vehicule.getAgence() != null) {
            vehicule.getAgence().getVehicules().remove(vehicule); // ancien rattachement
        }
        vehicule.setAgence(agence);          // cote proprietaire
        agence.getVehicules().add(vehicule); // cote inverse
        return vehiculeRepository.save(vehicule);
    }

    // =====================================================================
    //  Affectation / desaffectation : @ManyToMany Vehicule <-> Equipement
    // =====================================================================

    @Override
    @Transactional
    public Vehicule affecterEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = retrieveVehicule(idVehicule);
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", idEquipement));

        vehicule.getEquipements().add(equipement);  // cote proprietaire : insertion dans vehicule_equipement
        equipement.getVehicules().add(vehicule);    // cote inverse
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional
    public Vehicule desaffecterEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = retrieveVehicule(idVehicule);
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", idEquipement));

        if (!vehicule.getEquipements().contains(equipement)) {
            throw new IllegalStateException("L'equipement " + idEquipement
                    + " n'est pas installe sur le vehicule " + idVehicule + ".");
        }
        vehicule.getEquipements().remove(equipement); // suppression de la ligne dans vehicule_equipement
        equipement.getVehicules().remove(vehicule);
        return vehiculeRepository.save(vehicule);
    }
}
