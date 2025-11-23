package com.mutantes.mutant_detector.controller;

import com.mutantes.mutant_detector.dto.StatsResponse;
import com.mutantes.mutant_detector.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @Operation(
            summary = "Obtiene las estadísticas de análisis de ADN",
            description = "Retorna la cantidad de ADN mutante, humano y el ratio mutantes/humanos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas correctamente")
    })
    @GetMapping("/stats")
    public StatsResponse getStats() {
        return statsService.getStats();
    }
}