package com.mutantes.mutant_detector.service;

import org.springframework.stereotype.Service;

@Service
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;

    public boolean isMutant(String[] dna) {
        char[][] matrix = validateAndBuildMatrix(dna);
        int n = matrix.length;
        int sequences = 0;

        // Horizontal →
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n - SEQUENCE_LENGTH; j++) {
                char c = matrix[i][j];
                if (c == matrix[i][j + 1] &&
                        c == matrix[i][j + 2] &&
                        c == matrix[i][j + 3]) {

                    sequences++;
                    if (sequences > 1) return true;
                }
            }
        }

        // Vertical ↓
        for (int j = 0; j < n; j++) {
            for (int i = 0; i <= n - SEQUENCE_LENGTH; i++) {
                char c = matrix[i][j];
                if (c == matrix[i + 1][j] &&
                        c == matrix[i + 2][j] &&
                        c == matrix[i + 3][j]) {

                    sequences++;
                    if (sequences > 1) return true;
                }
            }
        }

        // Diagonal ↘
        for (int i = 0; i <= n - SEQUENCE_LENGTH; i++) {
            for (int j = 0; j <= n - SEQUENCE_LENGTH; j++) {
                char c = matrix[i][j];
                if (c == matrix[i + 1][j + 1] &&
                        c == matrix[i + 2][j + 2] &&
                        c == matrix[i + 3][j + 3]) {

                    sequences++;
                    if (sequences > 1) return true;
                }
            }
        }

        // Diagonal ↗
        for (int i = SEQUENCE_LENGTH - 1; i < n; i++) {
            for (int j = 0; j <= n - SEQUENCE_LENGTH; j++) {
                char c = matrix[i][j];
                if (c == matrix[i - 1][j + 1] &&
                        c == matrix[i - 2][j + 2] &&
                        c == matrix[i - 3][j + 3]) {

                    sequences++;
                    if (sequences > 1) return true;
                }
            }
        }

        return sequences > 1;
    }

    // Validación básica + construcción de matriz char[][]
    private char[][] validateAndBuildMatrix(String[] dna) {
        if (dna == null || dna.length == 0) {
            throw new IllegalArgumentException("DNA no puede ser nulo ni vacío.");
        }

        int n = dna.length;
        if (n < 4) {
            throw new IllegalArgumentException("La matriz ADN debe ser al menos de 4x4.");
        }

        char[][] matrix = new char[n][n];

        for (int i = 0; i < n; i++) {
            String row = dna[i];
            if (row == null) {
                throw new IllegalArgumentException("Fila ADN nula en índice " + i);
            }

            if (row.length() != n) {
                throw new IllegalArgumentException("La matriz debe ser NxN. Error en fila " + i);
            }

            for (int j = 0; j < n; j++) {
                char c = Character.toUpperCase(row.charAt(j));
                if (c != 'A' && c != 'T' && c != 'C' && c != 'G') {
                    throw new IllegalArgumentException(
                            "Carácter inválido '" + c + "' en (" + i + "," + j + "). Solo A,T,C,G."
                    );
                }
                matrix[i][j] = c;
            }
        }

        return matrix;
    }
}