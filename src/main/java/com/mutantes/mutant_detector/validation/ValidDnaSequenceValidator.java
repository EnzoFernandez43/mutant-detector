package com.mutantes.mutant_detector.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, List<String>> {

    @Override
    public boolean isValid(List<String> dna, ConstraintValidatorContext context) {

        // Si es null o vacío, que lo manejen @NotNull/@NotEmpty
        if (dna == null || dna.isEmpty()) {
            return true;
        }

        int n = dna.size();
        if (n < 4) {
            buildMessage(context,
                    "La matriz de ADN debe ser al menos de 4x4.");
            return false;
        }

        // Todas las filas deben tener longitud n (NxN)
        for (int i = 0; i < n; i++) {
            String row = dna.get(i);

            if (row == null) {
                buildMessage(context,
                        "La fila de ADN en la posición " + i + " es nula.");
                return false;
            }

            if (row.length() != n) {
                buildMessage(context,
                        "La matriz de ADN debe ser NxN. La fila " + i +
                                " tiene longitud " + row.length() +
                                " y se esperaba " + n + ".");
                return false;
            }

            // Solo caracteres A, T, C, G
            for (int j = 0; j < row.length(); j++) {
                char c = Character.toUpperCase(row.charAt(j));
                if (c != 'A' && c != 'T' && c != 'C' && c != 'G') {
                    buildMessage(context,
                            "Carácter inválido '" + c + "' en la posición (" +
                                    i + "," + j + "). Solo se permiten A, T, C y G.");
                    return false;
                }
            }
        }

        return true;
    }

    private void buildMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}