package com.mutantes.mutant_detector.controller;

import com.mutantes.mutant_detector.dto.StatsResponse;
import com.mutantes.mutant_detector.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@Tag(name = "Estadísticas", description = "API para consultar estadísticas de ADN verificado.")
public class StatsController {

    private final StatsService statsService;

    @Operation(
            summary = "Obtiene las estadísticas de verificaciones de ADN.",
            description = "Retorna un JSON con la cantidad de ADN mutante, ADN humano y el ratio."
    )
    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente.")
    @GetMapping
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }
}
