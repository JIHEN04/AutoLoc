package tn.esprit.autoloc.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.AgenceRepository;
import tn.esprit.autoloc.repository.VehiculeRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Insere une agence et quelques vehicules de demonstration au demarrage,
 * uniquement en profil "dev" et uniquement si la base est vide.
 */
@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDataInitializer implements CommandLineRunner {

    private final AgenceRepository agenceRepository;
    private final VehiculeRepository vehiculeRepository;

    @Override
    public void run(String... args) {
        if (vehiculeRepository.count() > 0) {
            log.debug("Donnees deja presentes : aucune donnee de demonstration inseree.");
            return;
        }
        Agence agence = new Agence();
        agence.setNom("AutoLoc Centre");
        agence.setVille("Tunis");
        agence.setAdresse("12 Avenue Habib Bourguiba");
        agence.setTelephone("71000111");
        agenceRepository.save(agence);

        List<Vehicule> vehicules = List.of(
                vehicule("215 TU 4521", "Peugeot", "208", CategorieVehicule.CITADINE, "90.00", StatutVehicule.DISPONIBLE, agence),
                vehicule("228 TU 1187", "Volkswagen", "Passat", CategorieVehicule.BERLINE, "160.00", StatutVehicule.DISPONIBLE, agence),
                vehicule("231 TU 7702", "Kia", "Sportage", CategorieVehicule.SUV, "200.00", StatutVehicule.MAINTENANCE, agence)
        );
        vehiculeRepository.saveAll(vehicules);
        log.info("1 agence et {} vehicules de demonstration inseres.", vehicules.size());
    }

    private Vehicule vehicule(String immatriculation, String marque, String modele, CategorieVehicule categorie,
                              String tarif, StatutVehicule statut, Agence agence) {
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation(immatriculation);
        vehicule.setMarque(marque);
        vehicule.setModele(modele);
        vehicule.setCategorie(categorie);
        vehicule.setTarifJournalier(new BigDecimal(tarif));
        vehicule.setStatut(statut);
        vehicule.setAgence(agence);
        return vehicule;
    }
}
