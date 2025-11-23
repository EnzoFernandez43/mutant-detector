package com.mutantes.mutant_detector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estadísticas de verificaciones de ADN realizadas.")
public class StatsResponse {

    @JsonProperty("count_mutant_dna")
    @Schema(description = "Cantidad total de ADN mutante verificado.", example = "40")
    private long countMutantDna;

    @JsonProperty("count_human_dna")
    @Schema(description = "Cantidad total de ADN humano verificado.", example = "100")
    private long countHumanDna;

    @Schema(description = "Ratio de mutantes sobre humanos (mutantes / humanos).", example = "0.4")
    private double ratio;
}
