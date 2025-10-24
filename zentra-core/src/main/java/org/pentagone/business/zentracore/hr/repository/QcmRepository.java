package org.pentagone.business.zentracore.hr.repository;

import org.pentagone.business.zentracore.hr.entity.Qcm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QcmRepository extends JpaRepository<Qcm, Long> {
    List<Qcm> findByTitleContainingIgnoreCase(String title);
}
