package com.mutantes.mutant_detector.repository;

import com.mutantes.mutant_detector.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {

    Optional<DnaRecord> findByDnaHash(String dnaHash);

    long countByIsMutant(boolean isMutant);
}