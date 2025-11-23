package com.mutantes.mutant_detector.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private final MutantDetector detector = new MutantDetector();

    // ==========================
    // CASOS MUTANTES (TRUE)
    // ==========================

    @Test
    void mutanteHorizontal_conDosFilasConSecuencia() {
        String[] dna = {
                "AAAACC", // fila 0 → AAAA
                "GGGGTT", // fila 1 → GGGG  (ya hay 2 secuencias)
                "TCTCTC",
                "CTCTCT",
                "ATATAT",
                "CGCGCG"
        };

        assertTrue(detector.isMutant(dna));
    }

    @Test
    void mutanteVertical_conDosColumnasConSecuencia() {
        String[] dna = {
                "ATGCAC", // col0=A, col5=C
                "AGTCAC", // col0=A, col5=C
                "ATACAC", // col0=A, col5=C
                "AGGAAC", // col0=A, col5=C  → 4 A en col0 y 4 C en col5
                "TTTTGT",
                "GGGGGT"
        };

        assertTrue(detector.isMutant(dna));
    }

    @Test
    void mutanteDiagonales_descendenteYascendente() {
        // Matriz 4x4 especialmente construida:
        // Diagonal ↘ : A A A A
        // Diagonal ↗ : G G G G
        String[] dna = {
                "ATCG",
                "CAGT",
                "TGAC",
                "GCTA"
        };

        assertTrue(detector.isMutant(dna));
    }

    // ==========================
    // CASOS HUMANOS (FALSE)
    // ==========================

    @Test
    void humanoSinSecuencias() {
        // Matriz 4x4 sin ninguna secuencia de 4 iguales
        String[] dna = {
                "ATCG",
                "CAAT",
                "TGTC",
                "GCTA"
        };

        assertFalse(detector.isMutant(dna));
    }

    @Test
    void humanoConUnaSolaSecuencia() {
        // Solo una fila con AAAA, nada más
        String[] dna = {
                "AAAA",   // única secuencia
                "CTGT",
                "TGCT",
                "GTCG"
        };

        assertFalse(detector.isMutant(dna));
    }

    // ==========================
    // ERRORES DE VALIDACIÓN
    // ==========================

    @Test
    void matrizNoCuadradaLanzaError() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT"   // 3x6 → no es NxN
        };

        assertThrows(IllegalArgumentException.class,
                () -> detector.isMutant(dna));
    }

    @Test
    void filaNulaLanzaError() {
        String[] dna = {
                "ATGCGA",
                null,
                "TTATGT",
                "AGAAGC",
                "CTCCTA",
                "TCACTG"
        };

        assertThrows(IllegalArgumentException.class,
                () -> detector.isMutant(dna));
    }

    @Test
    void caracterInvalidoLanzaError() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTXTGT", // X inválida
                "AGAAGC",
                "CTCCTA",
                "TCACTG"
        };

        assertThrows(IllegalArgumentException.class,
                () -> detector.isMutant(dna));
    }

    @Test
    void mutanteSecuenciasAlFinalDeLaMatriz() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG", // diagonal ↘ G en (3,3)-(4,4)-(5,5) con la siguiente fila
                "CCCCTA", // horizontal CCCC en fila 4
                "TCACTG"  // diagonal ↘ continue
        };

        // CCCC horizontal en fila 4
        // GGGG diagonal en posiciones tardías
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void mutanteSecuenciasAlFinal_delMetodo() {
        // Diseñada para forzar la detección de 2 secuencias
        // recién cerca del final de la matriz.
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGACGG",
                "CTCCTA",  // CTCCTA → contiene "CTCC" al final
                "AAAAGG"   // AAAAGG → AAAA cerca del final
        };

        // Esta configuración hace que:
        // - La primera secuencia AAAA aparezca al final (fila 5)
        // - La segunda secuencia CCCC aparezca finalizando la fila 4
        // De modo que el algoritmo llega al return final.
        assertTrue(detector.isMutant(dna));
    }


}