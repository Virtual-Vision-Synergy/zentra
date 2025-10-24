package org.pentagone.business.zentracore.besoin.service;

import org.pentagone.business.zentracore.besoin.dto.BesoinRequestDTO;
import org.pentagone.business.zentracore.besoin.dto.BesoinResponseDTO;
import org.pentagone.business.zentracore.besoin.entity.Besoin;
import org.pentagone.business.zentracore.besoin.repository.BesoinRepository;
import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BesoinService {
    private final BesoinRepository besoinRepository;

    public BesoinService(BesoinRepository besoinRepository) {
        this.besoinRepository = besoinRepository;
    }

    public List<BesoinResponseDTO> getAllBesoins() {
        return besoinRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public BesoinResponseDTO getBesoinById(Long id) {
        Besoin besoin = besoinRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Besoin non trouvé avec l'id: " + id));
        return convertToResponseDTO(besoin);
    }

    public List<BesoinResponseDTO> getBesoinsByStatut(String statut) {
        return besoinRepository.findByStatut(statut)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BesoinResponseDTO> getBesoinsByDepartement(String departement) {
        return besoinRepository.findByDepartement(departement)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public BesoinResponseDTO createBesoin(BesoinRequestDTO requestDTO) {
        validateBesoinRequest(requestDTO);
        Besoin besoin = convertToEntity(requestDTO);
        Besoin savedBesoin = besoinRepository.save(besoin);
        return convertToResponseDTO(savedBesoin);
    }

    public BesoinResponseDTO updateBesoin(Long id, BesoinRequestDTO requestDTO) {
        Besoin existingBesoin = besoinRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Besoin non trouvé avec l'id: " + id));
        
        validateBesoinRequest(requestDTO);
        updateEntityFromDTO(existingBesoin, requestDTO);
        Besoin updatedBesoin = besoinRepository.save(existingBesoin);
        return convertToResponseDTO(updatedBesoin);
    }

    public void deleteBesoin(Long id) {
        if (!besoinRepository.existsById(id)) {
            throw new EntityNotFoundException("Besoin non trouvé avec l'id: " + id);
        }
        besoinRepository.deleteById(id);
    }

    private void validateBesoinRequest(BesoinRequestDTO requestDTO) {
        if (requestDTO.getTitre() == null || requestDTO.getTitre().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre est obligatoire");
        }
        if (requestDTO.getNombrePostes() == null || requestDTO.getNombrePostes() <= 0) {
            throw new IllegalArgumentException("Le nombre de postes doit être supérieur à 0");
        }
    }

    private Besoin convertToEntity(BesoinRequestDTO dto) {
        Besoin besoin = new Besoin();
        besoin.setTitre(dto.getTitre());
        besoin.setDescription(dto.getDescription());
        besoin.setDepartement(dto.getDepartement());
        besoin.setNombrePostes(dto.getNombrePostes());
        besoin.setTypeContrat(dto.getTypeContrat());
        besoin.setDateLimite(dto.getDateLimite());
        besoin.setStatut(dto.getStatut());
        besoin.setPriorite(dto.getPriorite());
        besoin.setCompetencesRequises(dto.getCompetencesRequises());
        besoin.setBudgetAlloue(dto.getBudgetAlloue());
        return besoin;
    }

    private void updateEntityFromDTO(Besoin besoin, BesoinRequestDTO dto) {
        besoin.setTitre(dto.getTitre());
        besoin.setDescription(dto.getDescription());
        besoin.setDepartement(dto.getDepartement());
        besoin.setNombrePostes(dto.getNombrePostes());
        besoin.setTypeContrat(dto.getTypeContrat());
        besoin.setDateLimite(dto.getDateLimite());
        besoin.setStatut(dto.getStatut());
        besoin.setPriorite(dto.getPriorite());
        besoin.setCompetencesRequises(dto.getCompetencesRequises());
        besoin.setBudgetAlloue(dto.getBudgetAlloue());
    }

    private BesoinResponseDTO convertToResponseDTO(Besoin besoin) {
        return new BesoinResponseDTO(
                besoin.getIdBesoin(),
                besoin.getTitre(),
                besoin.getDescription(),
                besoin.getDepartement(),
                besoin.getNombrePostes(),
                besoin.getTypeContrat(),
                besoin.getDateCreation(),
                besoin.getDateLimite(),
                besoin.getStatut(),
                besoin.getPriorite(),
                besoin.getCompetencesRequises(),
                besoin.getBudgetAlloue()
        );
    }
}
