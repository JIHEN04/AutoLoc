package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Paiement;

import java.util.List;

public interface IPaiementService {

    List<Paiement> retrieveAllPaiements();

    Paiement retrievePaiement(Long idPaiement);

    Paiement addPaiement(Paiement paiement);

    Paiement updatePaiement(Long idPaiement, Paiement paiement);

    void removePaiement(Long idPaiement);
}
