package com.mutantes.mutant_detector.controller;

import com.mutantes.mutant_detector.dto.DnaRequest;
import com.mutantes.mutant_detector.service.MutantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mutant")
public class MutantController {

    private final MutantService mutantService;

    public MutantController(MutantService mutantService) {
        this.mutantService = mutantService;
    }

    @Operation(
            summary = "Verifica si un ADN pertenece a un mutante",
            description = "Recibe una matriz de ADN NxN (mínimo 4x4) con caracteres A,T,C,G. " +
                    "Retorna 200 si es mutante, 403 si NO es mutante, 400 si el DNA es inválido."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El ADN corresponde a un mutante"),
            @ApiResponse(responseCode = "403", description = "El ADN NO corresponde a un mutante"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<Void> isMutant(@Valid @RequestBody DnaRequest request) {

        boolean isMutant = mutantService.isMutant(
                request.getDna().toArray(new String[0])
        );

        if (isMutant) {
            return ResponseEntity.ok().build();   // 200
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
        }
    }
}