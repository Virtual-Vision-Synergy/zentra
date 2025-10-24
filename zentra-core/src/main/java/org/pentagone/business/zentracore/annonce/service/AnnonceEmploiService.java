package org.pentagone.business.zentracore.annonce.service;

import lombok.RequiredArgsConstructor;
import org.pentagone.business.zentracore.annonce.entity.AnnonceEmploi;
import org.pentagone.business.zentracore.annonce.repository.AnnonceEmploiRepository;
import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnonceEmploiService {

    private final AnnonceEmploiRepository annonceEmploiRepository;

    public List<AnnonceEmploi> getAllAnnonces() {
        return annonceEmploiRepository.findAll();
    }

    public AnnonceEmploi getAnnonceById(Integer id) {
        return annonceEmploiRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Annonce d'emploi non trouvée avec l'id: " + id));
    }

    public List<AnnonceEmploi> getAnnoncesByStatut(String statut) {
        return annonceEmploiRepository.findByStatut(statut);
    }

    public List<AnnonceEmploi> getAnnoncesByPoste(Integer idPoste) {
        return annonceEmploiRepository.findByIdPoste(idPoste);
    }

    public AnnonceEmploi createAnnonce(AnnonceEmploi annonce) {
        return annonceEmploiRepository.save(annonce);
    }

    public AnnonceEmploi updateAnnonce(Integer id, AnnonceEmploi annonceDetails) {
        AnnonceEmploi annonce = getAnnonceById(id);
        
        annonce.setIdPoste(annonceDetails.getIdPoste());
        annonce.setTitreAnnonce(annonceDetails.getTitreAnnonce());
        annonce.setDescription(annonceDetails.getDescription());
        annonce.setTypeContrat(annonceDetails.getTypeContrat());
        annonce.setLieuTravail(annonceDetails.getLieuTravail());
        annonce.setDatePublication(annonceDetails.getDatePublication());
        annonce.setDateCloture(annonceDetails.getDateCloture());
        annonce.setNombrePostes(annonceDetails.getNombrePostes());
        annonce.setStatut(annonceDetails.getStatut());
        annonce.setCanauxPublication(annonceDetails.getCanauxPublication());
        
        return annonceEmploiRepository.save(annonce);
    }

    public void deleteAnnonce(Integer id) {
        AnnonceEmploi annonce = getAnnonceById(id);
        annonceEmploiRepository.delete(annonce);
    }
}
