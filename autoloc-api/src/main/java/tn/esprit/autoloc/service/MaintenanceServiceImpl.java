package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IMaintenanceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Maintenance> retrieveAllMaintenances() {
        return maintenanceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Maintenance retrieveMaintenance(Long idMaintenance) {
        return maintenanceRepository.findById(idMaintenance)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", idMaintenance));
    }

    @Override
    @Transactional
    public Maintenance addMaintenance(Maintenance maintenance) {
        maintenance.setIdMaintenance(null); // l'identifiant est genere par la base (IDENTITY)
        return maintenanceRepository.save(maintenance);
    }

    @Override
    @Transactional
    public Maintenance updateMaintenance(Long idMaintenance, Maintenance maintenance) {
        Maintenance existant = retrieveMaintenance(idMaintenance);
        // On ne copie que les attributs simples : les associations se gerent via les services d'affectation.
        existant.setDateDebut(maintenance.getDateDebut());
        existant.setDateFin(maintenance.getDateFin());
        existant.setDescription(maintenance.getDescription());
        return maintenanceRepository.save(existant);
    }

    @Override
    @Transactional
    public void removeMaintenance(Long idMaintenance) {
        Maintenance existant = retrieveMaintenance(idMaintenance);
        maintenanceRepository.delete(existant);
    }
}
