package com.udev.hotel.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udev.hotel.config.security.AuthoritiesConstants;
import com.udev.hotel.domain.entity.Reservation;
import com.udev.hotel.domain.repository.ReservationRepository;
import com.udev.hotel.service.ChambreService;
import com.udev.hotel.service.ReservationService;
import com.udev.hotel.service.dto.ReservationRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

	private final Logger log = LoggerFactory.getLogger(ReservationController.class);

	@Autowired
    private ReservationService reservationService;
	@Autowired
	private ReservationRepository reservationRepository;
	
    @PostMapping
    @Secured({AuthoritiesConstants.USER , AuthoritiesConstants.ADMIN})
    public ResponseEntity<Reservation> reserver(@RequestBody ReservationRequest request) {
    	String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info(currentUsername);
        Reservation reservation = reservationService.createReservation(
                currentUsername,
                request.getChambreId(),
                request.getServiceIds(),
                request.getDateDebut(),
                request.getDateFin()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }
    
    @GetMapping
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<Reservation>> getAllReservation(){
    	List<Reservation> reservation = reservationRepository.findAll(); 
		return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
}