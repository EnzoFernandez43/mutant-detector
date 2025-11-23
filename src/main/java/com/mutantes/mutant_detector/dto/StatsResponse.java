package com.mutantes.mutant_detector.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponse {
    private long count_mutant_dna;
    private long count_human_dna;
    private double ratio;
}
