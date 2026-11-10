package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> retrieveAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation retrieveReservation(Long idReservation) {
        return reservationRepository.findById(idReservation)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", idReservation));
    }

    @Override
    @Transactional
    public Reservation addReservation(Reservation reservation) {
        reservation.setIdReservation(null); // l'identifiant est genere par la base (IDENTITY)
        if (reservation.getStatut() == null) {
            reservation.setStatut(StatutReservation.EN_ATTENTE);
        }
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation updateReservation(Long idReservation, Reservation reservation) {
        Reservation existant = retrieveReservation(idReservation);
        // On ne copie que les attributs simples : les associations se gerent via les services d'affectation.
        existant.setDateDebut(reservation.getDateDebut());
        existant.setDateFin(reservation.getDateFin());
        existant.setStatut(reservation.getStatut());
        return reservationRepository.save(existant);
    }

    @Override
    @Transactional
    public void removeReservation(Long idReservation) {
        Reservation existant = retrieveReservation(idReservation);
        reservationRepository.delete(existant);
    }
}
