package com.mutantes.mutant_detector.exception;

import com.mutantes.mutant_detector.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleValidationErrors_devuelve400YErrorResponse() {
        // Mock HttpServletRequest
        HttpServletRequest request = mock(HttpServletRequest.class);

        // Mock de la excepción y del BindingResult
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError(
                "dnaRequest",
                "dna",
                "mensaje de error"
        );

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // Ejecutar
        ResponseEntity<ErrorResponse> response =
                handler.handleValidationErrors(ex, request);

        // Validar solo status y que el body exista
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleIllegalArgument_devuelve400YErrorResponse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        IllegalArgumentException ex = new IllegalArgumentException("DNA inválido");

        ResponseEntity<ErrorResponse> response =
                handler.handleIllegalArgument(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleGeneral_devuelve500YErrorResponse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Exception ex = new Exception("cualquier cosa");

        ResponseEntity<ErrorResponse> response =
                handler.handleGeneral(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
