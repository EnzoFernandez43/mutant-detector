package com.mutantes.mutant_detector.service;

import com.mutantes.mutant_detector.dto.StatsResponse;
import com.mutantes.mutant_detector.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private final DnaRecordRepository repository;

    public StatsService(DnaRecordRepository repository) {
        this.repository = repository;
    }

    public StatsResponse getStats() {
        long mutants = repository.countByIsMutant(true);
        long humans  = repository.countByIsMutant(false);

        double ratio;
        if (humans == 0) {
            // Definición estándar del desafío: si no hay humanos, ratio = 0.0
            ratio = 0.0;
        } else {
            ratio = (double) mutants / humans;
        }

        return new StatsResponse(mutants, humans, ratio);
    }
}