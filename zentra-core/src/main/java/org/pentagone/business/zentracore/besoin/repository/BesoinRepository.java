package org.pentagone.business.zentracore.besoin.repository;

import org.pentagone.business.zentracore.besoin.entity.Besoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BesoinRepository extends JpaRepository<Besoin, Long> {
    List<Besoin> findByStatut(String statut);
    List<Besoin> findByDepartement(String departement);
    List<Besoin> findByPriorite(String priorite);
}
