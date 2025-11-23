package com.mutantes.mutant_detector.service;

import com.mutantes.mutant_detector.entity.DnaRecord;
import com.mutantes.mutant_detector.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    void dadoDnaMutante_debeDevolverTrueYGuardarEnBd() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        // No está en BD → se analiza y se guarda
        when(dnaRecordRepository.findByDnaHash(anyString()))
                .thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(true);
        when(dnaRecordRepository.save(any(DnaRecord.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        boolean result = mutantService.isMutant(dna);

        assertTrue(result);
        verify(mutantDetector).isMutant(dna);
        verify(dnaRecordRepository).save(any(DnaRecord.class));
    }

    @Test
    void dadoDnaHumano_debeDevolverFalseYGuardarEnBd() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };

        when(dnaRecordRepository.findByDnaHash(anyString()))
                .thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(false);
        when(dnaRecordRepository.save(any(DnaRecord.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        boolean result = mutantService.isMutant(dna);

        assertFalse(result);
        verify(mutantDetector).isMutant(dna);
        verify(dnaRecordRepository).save(any(DnaRecord.class));
    }

    @Test
    void cuandoHashYaExiste_noAnalizaNiGuardaYDevuelveValorDeLaBd() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        DnaRecord record = new DnaRecord();
        record.setId(1L);
        record.setDnaHash("hash-falso");
        record.setMutant(true); // valor ya calculado en BD

        // Simulamos cache hit
        when(dnaRecordRepository.findByDnaHash(anyString()))
                .thenReturn(Optional.of(record));

        boolean result = mutantService.isMutant(dna);

        assertTrue(result);
        // No debe analizar ni guardar otra vez
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any());
    }

    @Test
    void cuandoDetectorLanzaIllegalArgument_propagamosExcepcion() {
        String[] dna = {
                "ATG",
                "CAG",
                "TTT"
        };

        when(dnaRecordRepository.findByDnaHash(anyString()))
                .thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna))
                .thenThrow(new IllegalArgumentException("DNA inválido"));

        assertThrows(IllegalArgumentException.class,
                () -> mutantService.isMutant(dna));
    }
}