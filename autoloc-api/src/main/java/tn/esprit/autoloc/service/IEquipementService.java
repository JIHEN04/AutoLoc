package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Equipement;

import java.util.List;

public interface IEquipementService {

    List<Equipement> retrieveAllEquipements();

    Equipement retrieveEquipement(Long idEquipement);

    Equipement addEquipement(Equipement equipement);

    Equipement updateEquipement(Long idEquipement, Equipement equipement);

    void removeEquipement(Long idEquipement);
}
