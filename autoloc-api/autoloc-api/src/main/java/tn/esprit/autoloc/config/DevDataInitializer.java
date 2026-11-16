package tn.esprit.autoloc.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.VehiculeRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Insere quelques vehicules de demonstration au demarrage, uniquement en profil "dev"
 * et uniquement si la table est vide (pas de doublons a chaque redemarrage).
 */
@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDataInitializer implements CommandLineRunner {

    private final VehiculeRepository vehiculeRepository;

    @Override
    public void run(String... args) {
        if (vehiculeRepository.count() > 0) {
            log.debug("Vehicules deja presents : aucune donnee de demonstration inseree.");
            return;
        }
        List<Vehicule> vehicules = List.of(
                new Vehicule(null, "215 TU 4521", "Peugeot", "208",
                        CategorieVehicule.CITADINE, new BigDecimal("90.00"), StatutVehicule.DISPONIBLE),
                new Vehicule(null, "228 TU 1187", "Volkswagen", "Passat",
                        CategorieVehicule.BERLINE, new BigDecimal("160.00"), StatutVehicule.DISPONIBLE),
                new Vehicule(null, "231 TU 7702", "Kia", "Sportage",
                        CategorieVehicule.SUV, new BigDecimal("200.00"), StatutVehicule.MAINTENANCE)
        );
        vehiculeRepository.saveAll(vehicules);
        log.info("{} vehicules de demonstration inseres.", vehicules.size());
    }
}
