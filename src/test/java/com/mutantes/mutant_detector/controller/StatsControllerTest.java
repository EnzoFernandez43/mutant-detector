package com.mutantes.mutant_detector.controller;

import com.mutantes.mutant_detector.dto.StatsResponse;
import com.mutantes.mutant_detector.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatsService statsService;

    @Test
    void debeDevolverEstadisticasCorrectas() throws Exception {
        StatsResponse stats = new StatsResponse(3, 6, 0.5);
        when(statsService.getStats()).thenReturn(stats);

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(3))
                .andExpect(jsonPath("$.count_human_dna").value(6))
                .andExpect(jsonPath("$.ratio").value(0.5));
    }

    @Test
    void cuandoNoHayHumanos_ratioDebeSerCero() throws Exception {
        StatsResponse stats = new StatsResponse(5, 0, 0.0);
        when(statsService.getStats()).thenReturn(stats);

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(5))
                .andExpect(jsonPath("$.count_human_dna").value(0))
                .andExpect(jsonPath("$.ratio").value(0.0));
    }
}