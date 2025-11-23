package com.mutantes.mutant_detector.service;

import com.mutantes.mutant_detector.dto.StatsResponse;
import com.mutantes.mutant_detector.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final DnaRecordRepository dnaRecordRepository;

    public StatsResponse getStats() {
        long mutantCount = dnaRecordRepository.countByIsMutant(true);
        long humanCount = dnaRecordRepository.countByIsMutant(false);

        double ratio = 0.0;
        if (humanCount > 0) {
            ratio = (double) mutantCount / humanCount;
        }

        return new StatsResponse(mutantCount, humanCount, ratio);
    }
}
