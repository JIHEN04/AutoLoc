package tn.esprit.autoloc.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Insere des donnees de demonstration au premier demarrage (base vide),
 * pour pouvoir tester directement les scenarios d'affectation dans Swagger / Postman.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final IAgenceRepository agenceRepository;
    private final IEmployeRepository employeRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IEquipementRepository equipementRepository;
    private final IClientRepository clientRepository;
    private final IReservationRepository reservationRepository;
    private final IContratRepository contratRepository;
    private final IMaintenanceRepository maintenanceRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (agenceRepository.count() > 0) {
            log.info("Donnees deja presentes : initialisation ignoree.");
            return;
        }

        // Agences
        Agence tunis = agenceRepository.save(agence("AutoLoc Tunis Centre", "Tunis", "Avenue Habib Bourguiba", "71000001"));
        Agence sfax = agenceRepository.save(agence("AutoLoc Sfax", "Sfax", "Route de Tunis km 2", "74000002"));
        Agence sousse = agenceRepository.save(agence("AutoLoc Sousse", "Sousse", "Boulevard du 14 Janvier", "73000003"));

        // Employes (Ahmed est a Sousse : il sera mute vers Tunis - scenario 1)
        employeRepository.save(employe("Ben Ali", "Ahmed", RoleEmploye.AGENT, sousse));
        employeRepository.save(employe("Trabelsi", "Sana", RoleEmploye.MANAGER, tunis));
        employeRepository.save(employe("Jaziri", "Karim", RoleEmploye.AGENT, sfax));

        // Equipements
        Equipement gps = equipementRepository.save(equipement("GPS"));
        equipementRepository.save(equipement("Siege bebe"));
        equipementRepository.save(equipement("Coffre de toit"));

        // Vehicules (le Kia Sportage est neuf, sans agence : il sera rattache a Sfax - scenario 2)
        Vehicule peugeot = vehicule("123 TU 4567", "Peugeot", "208", CategorieVehicule.CITADINE, "80.00", StatutVehicule.DISPONIBLE, tunis);
        peugeot.getEquipements().add(gps);
        vehiculeRepository.save(peugeot);
        Vehicule tucson = vehiculeRepository.save(vehicule("215 TU 8890", "Hyundai", "Tucson", CategorieVehicule.SUV, "150.00", StatutVehicule.MAINTENANCE, tunis));
        vehiculeRepository.save(vehicule("240 TU 1001", "Kia", "Sportage", CategorieVehicule.SUV, "160.00", StatutVehicule.DISPONIBLE, null));
        Vehicule kangoo = vehiculeRepository.save(vehicule("198 TU 3321", "Renault", "Kangoo", CategorieVehicule.UTILITAIRE, "110.00", StatutVehicule.LOUE, sfax));

        // Clients
        Client mohamed = clientRepository.save(client("Gharbi", "Mohamed", "mohamed.gharbi@mail.tn", "20111222", "PERM-1001"));
        clientRepository.save(client("Hamdi", "Amira", "amira.hamdi@mail.tn", "50333444", "PERM-1002"));

        // Reservation -> Contrat -> Paiement (composition, cascade ALL)
        Reservation reservation = new Reservation();
        reservation.setDateDebut(LocalDate.now());
        reservation.setDateFin(LocalDate.now().plusDays(3));
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setClient(mohamed);
        reservation.setVehicule(kangoo);
        reservationRepository.save(reservation);

        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("330.00"));
        contrat.setValide(true);
        contrat.setReservation(reservation);
        Paiement acompte = new Paiement();
        acompte.setMontant(new BigDecimal("100.00"));
        acompte.setDatePaiement(LocalDate.now());
        acompte.setModePaiement(ModePaiement.CARTE);
        acompte.setContrat(contrat);
        contrat.getPaiements().add(acompte);
        contratRepository.save(contrat); // le paiement est enregistre en cascade

        // Maintenance
        Maintenance vidange = new Maintenance();
        vidange.setDateDebut(LocalDate.now());
        vidange.setDescription("Vidange et controle des freins");
        vidange.setVehicule(tucson);
        maintenanceRepository.save(vidange);

        log.info("Donnees de demonstration AutoLoc inserees.");
    }

    private Agence agence(String nom, String ville, String adresse, String telephone) {
        Agence a = new Agence();
        a.setNom(nom);
        a.setVille(ville);
        a.setAdresse(adresse);
        a.setTelephone(telephone);
        return a;
    }

    private Employe employe(String nom, String prenom, RoleEmploye role, Agence agence) {
        Employe e = new Employe();
        e.setNom(nom);
        e.setPrenom(prenom);
        e.setRole(role);
        e.setAgence(agence);
        return e;
    }

    private Equipement equipement(String libelle) {
        Equipement e = new Equipement();
        e.setLibelle(libelle);
        return e;
    }

    private Vehicule vehicule(String immat, String marque, String modele, CategorieVehicule categorie,
                              String tarif, StatutVehicule statut, Agence agence) {
        Vehicule v = new Vehicule();
        v.setImmatriculation(immat);
        v.setMarque(marque);
        v.setModele(modele);
        v.setCategorie(categorie);
        v.setTarifJournalier(new BigDecimal(tarif));
        v.setStatut(statut);
        v.setAgence(agence);
        return v;
    }

    private Client client(String nom, String prenom, String email, String telephone, String numPermis) {
        Client c = new Client();
        c.setNom(nom);
        c.setPrenom(prenom);
        c.setEmail(email);
        c.setTelephone(telephone);
        c.setNumPermis(numPermis);
        c.setDateInscription(LocalDate.now());
        return c;
    }
}
