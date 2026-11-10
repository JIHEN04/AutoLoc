package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Vehicule;

import java.util.List;

public interface IVehiculeService {

    List<Vehicule> retrieveAllVehicules();

    Vehicule retrieveVehicule(Long idVehicule);

    Vehicule addVehicule(Vehicule vehicule);

    Vehicule updateVehicule(Long idVehicule, Vehicule vehicule);

    void removeVehicule(Long idVehicule);

    // ===== Affectation / desaffectation =====

    /** Scenario 2 (@ManyToOne) : un vehicule neuf est rattache a une agence (ex. Sfax). */
    Vehicule affecterVehicule(Long idVehicule, Long idAgence);

    /** Scenario 3 (@ManyToMany) : l'agent installe un equipement (ex. siege bebe) sur le vehicule. */
    Vehicule affecterEquipement(Long idVehicule, Long idEquipement);

    /** Scenario 3 (@ManyToMany) : l'agent retire l'equipement au retour du vehicule. */
    Vehicule desaffecterEquipement(Long idVehicule, Long idEquipement);
}
