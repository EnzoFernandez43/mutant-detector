package com.mutantes.mutant_detector.service;

import com.mutantes.mutant_detector.entity.DnaRecord;
import com.mutantes.mutant_detector.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class MutantService {

    private final DnaRecordRepository dnaRecordRepository;
    private final MutantDetector mutantDetector;

    public MutantService(DnaRecordRepository dnaRecordRepository,
                         MutantDetector mutantDetector) {
        this.dnaRecordRepository = dnaRecordRepository;
        this.mutantDetector = mutantDetector;
    }

    public boolean isMutant(String[] dna) {

        // 1) Hash SHA-256 del ADN completo
        String dnaHash = calculateDnaHash(dna);

        // 2) Cache en BD: si ya existe, devolvemos el resultado guardado
        Optional<DnaRecord> existing = dnaRecordRepository.findByDnaHash(dnaHash);
        if (existing.isPresent()) {
            return existing.get().isMutant();
        }

        // 3) Ejecutar algoritmo
        boolean result = mutantDetector.isMutant(dna);

        // 4) Persistir resultado
        DnaRecord record = new DnaRecord();
        record.setDnaHash(dnaHash);
        record.setMutant(result);
        dnaRecordRepository.save(record);

        return result;
    }

    private String calculateDnaHash(String[] dna) {
        try {
            String concatenated = String.join("", dna);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(concatenated.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString(); // 64 caracteres hex
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No se pudo inicializar SHA-256", e);
        }
    }
}