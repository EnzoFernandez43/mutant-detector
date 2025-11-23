package com.mutantes.mutant_detector.service;

import com.mutantes.mutant_detector.entity.DnaRecord;
import com.mutantes.mutant_detector.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @Mock
    private MutantDetector mutantDetector;

    @InjectMocks
    private MutantService mutantService;

    private final String[] mutantDna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
    private final String[] humanDna = {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};

    @Test
    void isMutant_cuandoDnaYaExisteEnBD_retornaResultadoCacheado() {
        // Arrange
        DnaRecord record = new DnaRecord();
        record.setMutant(true);
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(record));

        // Act
        boolean result = mutantService.isMutant(mutantDna);

        // Assert
        assertTrue(result);
        verify(mutantDetector, never()).isMutant(any()); // Verifica que el detector NO fue llamado
        verify(dnaRecordRepository, never()).save(any()); // Verifica que NO se guardó nada
    }

    @Test
    void isMutant_cuandoDnaEsNuevoYMutante_analizaYGuarda() {
        // Arrange
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(mutantDna)).thenReturn(true);
        ArgumentCaptor<DnaRecord> recordCaptor = ArgumentCaptor.forClass(DnaRecord.class);

        // Act
        boolean result = mutantService.isMutant(mutantDna);

        // Assert
        assertTrue(result);
        verify(dnaRecordRepository).save(recordCaptor.capture()); // Verifica que se llamó a save
        DnaRecord savedRecord = recordCaptor.getValue();
        assertTrue(savedRecord.isMutant());
        assertNotNull(savedRecord.getDnaHash());
    }

    @Test
    void isMutant_cuandoDnaEsNuevoYHumano_analizaYGuarda() {
        // Arrange
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(humanDna)).thenReturn(false);
        ArgumentCaptor<DnaRecord> recordCaptor = ArgumentCaptor.forClass(DnaRecord.class);

        // Act
        boolean result = mutantService.isMutant(humanDna);

        // Assert
        assertFalse(result);
        verify(dnaRecordRepository).save(recordCaptor.capture());
        DnaRecord savedRecord = recordCaptor.getValue();
        assertFalse(savedRecord.isMutant());
    }

    @Test
    void calculateDnaHash_esConsistente() {
        // Act
        String hash1 = mutantService.calculateDnaHash(mutantDna);
        String hash2 = mutantService.calculateDnaHash(mutantDna);

        // Assert
        assertEquals(hash1, hash2);
        assertEquals(64, hash1.length());
    }
}
