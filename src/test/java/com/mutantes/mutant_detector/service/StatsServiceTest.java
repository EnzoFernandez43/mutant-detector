package com.mutantes.mutant_detector.service;

import com.mutantes.mutant_detector.dto.StatsResponse;
import com.mutantes.mutant_detector.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private DnaRecordRepository repository;

    @InjectMocks
    private StatsService statsService;

    @Test
    void calcularStatsCorrectamente() {
        when(repository.countByIsMutant(true)).thenReturn(4L);
        when(repository.countByIsMutant(false)).thenReturn(8L);

        StatsResponse result = statsService.getStats();

        assertEquals(4, result.getCount_mutant_dna());
        assertEquals(8, result.getCount_human_dna());
        assertEquals(0.5, result.getRatio());
    }

    @Test
    void cuandoNoHayHumanos_ratioEsCero() {
        when(repository.countByIsMutant(true)).thenReturn(5L);
        when(repository.countByIsMutant(false)).thenReturn(0L);

        StatsResponse result = statsService.getStats();

        assertEquals(5, result.getCount_mutant_dna());
        assertEquals(0, result.getCount_human_dna());
        assertEquals(0.0, result.getRatio());
    }
}