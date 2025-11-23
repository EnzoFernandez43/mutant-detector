package com.mutantes.mutant_detector.dto;

import com.mutantes.mutant_detector.validation.ValidDnaSequence;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRequest {

    @NotNull(message = "El campo dna no puede ser nulo.")
    @NotEmpty(message = "El campo dna no puede estar vacío.")
    @ValidDnaSequence
    private List<String> dna;
}
