package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Maintenance;

import java.util.List;

public interface IMaintenanceService {

    List<Maintenance> retrieveAllMaintenances();

    Maintenance retrieveMaintenance(Long idMaintenance);

    Maintenance addMaintenance(Maintenance maintenance);

    Maintenance updateMaintenance(Long idMaintenance, Maintenance maintenance);

    void removeMaintenance(Long idMaintenance);
}
