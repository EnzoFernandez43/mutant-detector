package com.mutantes.mutant_detector.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mutantes.mutant_detector.dto.DnaRequest;
import com.mutantes.mutant_detector.service.MutantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MutantController.class)
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MutantService mutantService;

    @Test
    void cuandoEsMutante_debeResponder200() throws Exception {
        DnaRequest request = new DnaRequest(
                List.of(
                        "ATGCGA","CAGTGC","TTATGT",
                        "AGAAGG","CCCCTA","TCACTG"
                )
        );

        when(mutantService.isMutant(any(String[].class))).thenReturn(true);

        mockMvc.perform(
                post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    void cuandoNoEsMutante_debeResponder403() throws Exception {
        DnaRequest request = new DnaRequest(
                List.of(
                        "ATGCGA","CAGTGC","TTATTT",
                        "AGACGG","GCGTCA","TCACTG"
                )
        );

        when(mutantService.isMutant(any(String[].class))).thenReturn(false);

        mockMvc.perform(
                post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isForbidden());
    }

    @Test
    void cuandoServicioLanzaIllegalArgument_debeResponder400() throws Exception {
        DnaRequest request = new DnaRequest(
                List.of("ATG","CAG","TTT")
        );

        when(mutantService.isMutant(any(String[].class)))
                .thenThrow(new IllegalArgumentException("DNA inválido"));

        mockMvc.perform(
                post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadRequest());
    }
}