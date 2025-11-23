package com.mutantes.mutant_detector.controller;

import com.mutantes.mutant_detector.dto.DnaRequest;
import com.mutantes.mutant_detector.service.MutantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mutant")
@RequiredArgsConstructor
@Tag(name = "Detección de Mutantes", description = "API para analizar una secuencia de ADN.")
public class MutantController {

    private final MutantService mutantService;

    @Operation(
            summary = "Verifica si un ADN pertenece a un mutante",
            description = "Recibe una matriz de ADN NxN. Retorna 200 OK si es mutante, 403 Forbidden si es humano."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El ADN corresponde a un mutante."),
            @ApiResponse(responseCode = "403", description = "El ADN corresponde a un humano."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. matriz no cuadrada, caracteres inválidos).", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> isMutant(@Valid @RequestBody DnaRequest request) {
        boolean isMutant = mutantService.isMutant(request.getDna().toArray(new String[0]));

        return isMutant
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
