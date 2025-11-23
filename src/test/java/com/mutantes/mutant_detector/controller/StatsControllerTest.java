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
    void getStats_debeRetornarOkYJsonCorrecto() throws Exception {
        // Arrange
        StatsResponse stats = new StatsResponse(50, 150, 0.33);
        when(statsService.getStats()).thenReturn(stats);

        // Act & Assert
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(50))
                .andExpect(jsonPath("$.count_human_dna").value(150))
                .andExpect(jsonPath("$.ratio").value(0.33));
    }
}
