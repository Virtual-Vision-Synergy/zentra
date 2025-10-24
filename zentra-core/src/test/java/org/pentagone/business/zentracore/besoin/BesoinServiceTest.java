package org.pentagone.business.zentracore.besoin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pentagone.business.zentracore.besoin.dto.BesoinRequestDTO;
import org.pentagone.business.zentracore.besoin.dto.BesoinResponseDTO;
import org.pentagone.business.zentracore.besoin.entity.Besoin;
import org.pentagone.business.zentracore.besoin.repository.BesoinRepository;
import org.pentagone.business.zentracore.besoin.service.BesoinService;
import org.pentagone.business.zentracore.common.exception.EntityNotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BesoinServiceTest {

    @Mock
    private BesoinRepository besoinRepository;

    @InjectMocks
    private BesoinService besoinService;

    private Besoin besoin;
    private BesoinRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        besoin = new Besoin();
        besoin.setIdBesoin(1L);
        besoin.setTitre("Développeur Java");
        besoin.setDescription("Besoin de développeur Java senior");
        besoin.setDepartement("IT");
        besoin.setNombrePostes(2);
        besoin.setTypeContrat("CDI");
        besoin.setDateCreation(LocalDate.now());
        besoin.setStatut("Nouveau");
        besoin.setPriorite("Haute");

        requestDTO = new BesoinRequestDTO();
        requestDTO.setTitre("Développeur Java");
        requestDTO.setDescription("Besoin de développeur Java senior");
        requestDTO.setDepartement("IT");
        requestDTO.setNombrePostes(2);
        requestDTO.setTypeContrat("CDI");
        requestDTO.setStatut("Nouveau");
        requestDTO.setPriorite("Haute");
    }

    @Test
    void testGetAllBesoins() {
        List<Besoin> besoins = Arrays.asList(besoin);
        when(besoinRepository.findAll()).thenReturn(besoins);

        List<BesoinResponseDTO> result = besoinService.getAllBesoins();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Développeur Java", result.get(0).getTitre());
        verify(besoinRepository, times(1)).findAll();
    }

    @Test
    void testGetBesoinById() {
        when(besoinRepository.findById(1L)).thenReturn(Optional.of(besoin));

        BesoinResponseDTO result = besoinService.getBesoinById(1L);

        assertNotNull(result);
        assertEquals("Développeur Java", result.getTitre());
        assertEquals("IT", result.getDepartement());
        verify(besoinRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBesoinByIdNotFound() {
        when(besoinRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> besoinService.getBesoinById(999L));
        verify(besoinRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateBesoin() {
        when(besoinRepository.save(any(Besoin.class))).thenReturn(besoin);

        BesoinResponseDTO result = besoinService.createBesoin(requestDTO);

        assertNotNull(result);
        assertEquals("Développeur Java", result.getTitre());
        verify(besoinRepository, times(1)).save(any(Besoin.class));
    }

    @Test
    void testCreateBesoinWithoutTitre() {
        requestDTO.setTitre(null);

        assertThrows(IllegalArgumentException.class, () -> besoinService.createBesoin(requestDTO));
        verify(besoinRepository, never()).save(any(Besoin.class));
    }

    @Test
    void testCreateBesoinWithInvalidNombrePostes() {
        requestDTO.setNombrePostes(0);

        assertThrows(IllegalArgumentException.class, () -> besoinService.createBesoin(requestDTO));
        verify(besoinRepository, never()).save(any(Besoin.class));
    }

    @Test
    void testUpdateBesoin() {
        when(besoinRepository.findById(1L)).thenReturn(Optional.of(besoin));
        when(besoinRepository.save(any(Besoin.class))).thenReturn(besoin);

        BesoinResponseDTO result = besoinService.updateBesoin(1L, requestDTO);

        assertNotNull(result);
        verify(besoinRepository, times(1)).findById(1L);
        verify(besoinRepository, times(1)).save(any(Besoin.class));
    }

    @Test
    void testUpdateBesoinNotFound() {
        when(besoinRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> besoinService.updateBesoin(999L, requestDTO));
        verify(besoinRepository, times(1)).findById(999L);
        verify(besoinRepository, never()).save(any(Besoin.class));
    }

    @Test
    void testDeleteBesoin() {
        when(besoinRepository.existsById(1L)).thenReturn(true);
        doNothing().when(besoinRepository).deleteById(1L);

        besoinService.deleteBesoin(1L);

        verify(besoinRepository, times(1)).existsById(1L);
        verify(besoinRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBesoinNotFound() {
        when(besoinRepository.existsById(999L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> besoinService.deleteBesoin(999L));
        verify(besoinRepository, times(1)).existsById(999L);
        verify(besoinRepository, never()).deleteById(999L);
    }

    @Test
    void testGetBesoinsByStatut() {
        List<Besoin> besoins = Arrays.asList(besoin);
        when(besoinRepository.findByStatut("Nouveau")).thenReturn(besoins);

        List<BesoinResponseDTO> result = besoinService.getBesoinsByStatut("Nouveau");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(besoinRepository, times(1)).findByStatut("Nouveau");
    }

    @Test
    void testGetBesoinsByDepartement() {
        List<Besoin> besoins = Arrays.asList(besoin);
        when(besoinRepository.findByDepartement("IT")).thenReturn(besoins);

        List<BesoinResponseDTO> result = besoinService.getBesoinsByDepartement("IT");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(besoinRepository, times(1)).findByDepartement("IT");
    }
}
