package com.udev.hotel.service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udev.hotel.config.constants.ReservationStatus;
import com.udev.hotel.domain.entity.Chambre;
import com.udev.hotel.domain.entity.Reservation;
import com.udev.hotel.domain.entity.ReservationServiceAdd;
import com.udev.hotel.domain.entity.ServiceAdditionnel;
import com.udev.hotel.domain.entity.User;
import com.udev.hotel.domain.repository.ChambreRepository;
import com.udev.hotel.domain.repository.ReservationRepository;
import com.udev.hotel.domain.repository.ReservationServiceAddRepository;
import com.udev.hotel.domain.repository.ServiceAdditionnelRepository;
import com.udev.hotel.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ChambreRepository chambreRepository;
    private final UserRepository userRepository;
    private final ServiceAdditionnelRepository serviceAdditionnelRepository;
    private final ReservationServiceAddRepository reservationServiceAddRepository;

    @Transactional
    public Reservation createReservation(Long userId, Long chambreId, List<Long> serviceIds, LocalDate dateDebut, LocalDate dateFin) {
        Chambre chambre = chambreRepository.findById(chambreId)
                .orElseThrow(() -> new IllegalArgumentException("La chambre spécifiée est introuvable."));
        
        boolean isDisponible = reservationRepository.findByChambreAndDateOverlap(chambreId, dateDebut, dateFin).isEmpty();
        if (!isDisponible) {
            throw new IllegalArgumentException("La chambre n'est pas disponible pour les dates spécifiées.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setChambre(chambre);
        reservation.setDateDebut(dateDebut);
        reservation.setDateFin(dateFin);
        reservation.setStatus(ReservationStatus.EN_ATTENTE);
        Reservation savedReservation = reservationRepository.save(reservation);

        if (serviceIds != null && !serviceIds.isEmpty()) {
            List<ServiceAdditionnel> services = serviceAdditionnelRepository.findAllById(serviceIds);
            for (ServiceAdditionnel service : services) {
                ReservationServiceAdd reservationServiceAdd = new ReservationServiceAdd();
                reservationServiceAdd.setReservation(savedReservation);
                reservationServiceAdd.setServiceAdditionnel(service);
                reservationServiceAddRepository.save(reservationServiceAdd);
            }
        }

        return savedReservation;
    }
    
    
}