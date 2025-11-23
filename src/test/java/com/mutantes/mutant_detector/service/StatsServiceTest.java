package com.mutantes.mutant_detector.service;

import com.mutantes.mutant_detector.dto.StatsResponse;
import com.mutantes.mutant_detector.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private StatsService statsService;

    @Test
    void getStats_calculaCorrectamente() {
        // Arrange
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(100L);

        // Act
        StatsResponse response = statsService.getStats();

        // Assert
        assertEquals(40, response.getCountMutantDna());
        assertEquals(100, response.getCountHumanDna());
        assertEquals(0.4, response.getRatio(), 0.001);
    }

    @Test
    void getStats_cuandoHumanosEsCero_ratioEsCero() {
        // Arrange
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(10L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        // Act
        StatsResponse response = statsService.getStats();

        // Assert
        assertEquals(10, response.getCountMutantDna());
        assertEquals(0, response.getCountHumanDna());
        assertEquals(0.0, response.getRatio(), 0.001);
    }
}
