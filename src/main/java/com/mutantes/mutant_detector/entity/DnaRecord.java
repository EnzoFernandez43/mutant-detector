package com.mutantes.mutant_detector.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "dna_records",
        indexes = {
                @Index(name = "idx_dna_hash", columnList = "dnaHash"),
                @Index(name = "idx_is_mutant", columnList = "isMutant")
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DnaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String dnaHash;  // SHA-256 (64 caracteres hex)

    @Column(nullable = false)
    private boolean isMutant;
}