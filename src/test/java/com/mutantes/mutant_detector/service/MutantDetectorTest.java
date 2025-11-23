package com.mutantes.mutant_detector.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private final MutantDetector detector = new MutantDetector();

    // =================================
    // CASOS MUTANTES (DEBE SER TRUE)
    // =================================

    @Test
    void esMutante_dosSecuenciasHorizontales() {
        String[] dna = {"AAAA", "CCCC", "GTGT", "GTGT"};
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void esMutante_dosSecuenciasVerticales() {
        String[] dna = {"ACGT", "ACGT", "ACGT", "ACGT"};
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void esMutante_dosSecuenciasDiagonalesDescendentes() {
        String[] dna = {"AGGG", "GAGG", "GGAG", "GGGA"};
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void esMutante_dosSecuenciasDiagonalesAscendentes() {
        String[] dna = {"GGGA", "GGAG", "GAGG", "AGGG"};
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void esMutante_horizontalYVertical() {
        String[] dna = {"AAAA", "C...", "C...", "C..."};
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void esMutante_horizontalYDiagonal() {
        String[] dna = {"AAAA", ".C..", "..C.", "...C"};
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void esMutante_verticalYDiagonal() {
        String[] dna = {"C...", "C.G.", "C..G", "C...G"};
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void esMutante_matrizGrandeConSecuencias() {
        String[] dna = new String[10];
        for (int i = 0; i < 10; i++) dna[i] = "..........";
        dna[1] = "..AAAA....";
        dna[5] = "....CCCC..";
        assertTrue(detector.isMutant(dna));
    }

    // =================================
    // CASOS HUMANOS (DEBE SER FALSE)
    // =================================

    @Test
    void noEsMutante_sinSecuencias() {
        String[] dna = {"ATCG", "CAGT", "TTGA", "GGCT"};
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void noEsMutante_unaSolaSecuenciaHorizontal() {
        String[] dna = {"AAAA", "CTGT", "TGCT", "GTCG"};
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void noEsMutante_unaSolaSecuenciaVertical() {
        String[] dna = {"C...", "C...", "C...", "C..."};
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void noEsMutante_unaSolaSecuenciaDiagonal() {
        String[] dna = {"C...", ".C..", "..C.", "...C"};
        assertFalse(detector.isMutant(dna));
    }

    // =================================
    // CASOS DE VALIDACIÓN (DEBE LANZAR ERROR)
    // =================================

    @Test
    void valida_matrizNula() {
        assertThrows(IllegalArgumentException.class, () -> detector.isMutant(null));
    }

    @Test
    void valida_matrizVacia() {
        assertThrows(IllegalArgumentException.class, () -> detector.isMutant(new String[]{}));
    }

    @Test
    void valida_matrizNoCuadrada() {
        String[] dna = {"ATGC", "CAGT"}; // 2x4
        assertThrows(IllegalArgumentException.class, () -> detector.isMutant(dna));
    }

    @Test
    void valida_matrizPequeña() {
        String[] dna = {"A"};
        assertThrows(IllegalArgumentException.class, () -> detector.isMutant(dna));
    }

    @ParameterizedTest
    @ValueSource(chars = {'X', 'Z', ' ', '1'})
    void valida_caracterInvalido(char invalidChar) {
        String[] dna = {"ATGC", "CAGT", "TTG" + invalidChar, "GGCT"};
        assertThrows(IllegalArgumentException.class, () -> detector.isMutant(dna));
    }
    
    @Test
    void valida_filaNula() {
        String[] dna = {"ATGC", null, "TTGA", "GGCT"};
        assertThrows(IllegalArgumentException.class, () -> detector.isMutant(dna));
    }
}
