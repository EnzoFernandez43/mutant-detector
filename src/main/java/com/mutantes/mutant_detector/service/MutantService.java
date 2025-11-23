package com.mutantes.mutant_detector.service;

import com.mutantes.mutant_detector.entity.DnaRecord;
import com.mutantes.mutant_detector.exception.DnaHashCalculationException;
import com.mutantes.mutant_detector.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MutantService {

    private final DnaRecordRepository dnaRecordRepository;
    private final MutantDetector mutantDetector;

    public boolean isMutant(String[] dna) {
        String dnaHash = calculateDnaHash(dna);

        Optional<DnaRecord> existing = dnaRecordRepository.findByDnaHash(dnaHash);
        if (existing.isPresent()) {
            return existing.get().isMutant();
        }

        boolean result = mutantDetector.isMutant(dna);

        DnaRecord record = new DnaRecord();
        record.setDnaHash(dnaHash);
        record.setMutant(result);
        record.setCreatedAt(LocalDateTime.now()); // Añadido createdAt
        dnaRecordRepository.save(record);

        return result;
    }

    public String calculateDnaHash(String[] dna) {
        try {
            String concatenated = String.join("", dna);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(concatenated.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new DnaHashCalculationException("Error calculando el hash SHA-256 del ADN.", e);
        }
    }
}
