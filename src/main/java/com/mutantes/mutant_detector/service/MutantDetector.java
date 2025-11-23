package com.mutantes.mutant_detector.service;

import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private static final int MIN_SEQUENCES_FOR_MUTANT = 2;
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');

    public boolean isMutant(String[] dna) {
        validateDna(dna);

        final int n = dna.length;
        final char[][] matrix = buildMatrix(dna, n);
        int sequenceCount = 0;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (col <= n - SEQUENCE_LENGTH) {
                    if (isHorizontal(matrix, row, col)) {
                        sequenceCount++;
                    }
                }
                if (row <= n - SEQUENCE_LENGTH) {
                    if (isVertical(matrix, row, col)) {
                        sequenceCount++;
                    }
                }
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (isDiagonal(matrix, row, col)) {
                        sequenceCount++;
                    }
                }
                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH) {
                    if (isAntiDiagonal(matrix, row, col)) {
                        sequenceCount++;
                    }
                }

                if (sequenceCount >= MIN_SEQUENCES_FOR_MUTANT) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isHorizontal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row][col + 1] == base && matrix[row][col + 2] == base && matrix[row][col + 3] == base;
    }

    private boolean isVertical(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row + 1][col] == base && matrix[row + 2][col] == base && matrix[row + 3][col] == base;
    }

    private boolean isDiagonal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row + 1][col + 1] == base && matrix[row + 2][col + 2] == base && matrix[row + 3][col + 3] == base;
    }

    private boolean isAntiDiagonal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row - 1][col + 1] == base && matrix[row - 2][col + 2] == base && matrix[row - 3][col + 3] == base;
    }

    private void validateDna(String[] dna) {
        if (dna == null || dna.length < SEQUENCE_LENGTH) {
            throw new IllegalArgumentException("El ADN debe ser una matriz de al menos 4x4.");
        }
        int n = dna.length;
        for (String row : dna) {
            if (row == null || row.length() != n) {
                throw new IllegalArgumentException("La matriz de ADN debe ser cuadrada (NxN).");
            }
            for (char c : row.toCharArray()) {
                if (!VALID_BASES.contains(Character.toUpperCase(c))) {
                    throw new IllegalArgumentException("El ADN contiene caracteres inválidos.");
                }
            }
        }
    }

    private char[][] buildMatrix(String[] dna, int n) {
        char[][] matrix = new char[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toUpperCase().toCharArray();
        }
        return matrix;
    }
}
