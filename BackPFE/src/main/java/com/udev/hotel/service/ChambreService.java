package com.udev.hotel.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.udev.hotel.config.constants.EtatChambre;
import com.udev.hotel.domain.entity.Chambre;
import com.udev.hotel.domain.repository.ChambreRepository;

@Service
public class ChambreService {

	@Autowired
	private ChambreRepository chambreRepository;

	private final String uploadDir = System.getProperty("user.home") + "/Documents/hotel-photos/";

	public List<Chambre> getAllChambres() {
		return chambreRepository.findAll();
	}

	 @Transactional(readOnly = true)
	    public Optional<Chambre> getChambreById(Long id) {
	        Optional<Chambre> chambre = chambreRepository.findByIdWithPhotos(id);
	        chambre.ifPresent(c -> Hibernate.initialize(c.getPhotos()));
	        return chambre;
	    }
	 
	public Chambre createChambre(List<MultipartFile> photos, Chambre chambre) throws IOException {
		for (MultipartFile photo : photos) {
			chambre.getPhotos().add(photo.getBytes());
		}
		return chambreRepository.save(chambre);
	}

	public Chambre updateChambre(Long id, Chambre updatedChambre) {
		return chambreRepository.findById(id).map(existingChambre -> {
			existingChambre.setNumeroChambre(updatedChambre.getNumeroChambre());
			existingChambre.setType(updatedChambre.getType());
			existingChambre.setPrix(updatedChambre.getPrix());
			existingChambre.setEtat(updatedChambre.getEtat());
			existingChambre.setDescription(updatedChambre.getDescription());
			existingChambre.setPhotos(updatedChambre.getPhotos());
			return chambreRepository.save(existingChambre);
		}).orElseThrow(() -> new RuntimeException("Chambre not found with id " + id));
	}

	public byte[] getPhoto(String fileName) throws IOException {
		Path filePath = Paths.get(uploadDir + fileName);
		return Files.readAllBytes(filePath);
	}

	public void deleteChambre(Long id) {
		chambreRepository.deleteById(id);
	}

	public List<Chambre> getAvailableChambres() {
		return chambreRepository.findByEtat(EtatChambre.DISPONIBLE);
	}

	
}