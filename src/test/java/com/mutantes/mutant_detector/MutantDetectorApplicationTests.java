package com.mutantes.mutant_detector;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MutantDetectorApplicationTests {

	@Test
	void contextLoads() {
		// Solo verifica que el contexto de Spring levante
	}

	@Test
	void mainEjecutaSinErrores() {
		// Ejecuta el método main para cubrir esa rama
		MutantDetectorApplication.main(new String[]{});
	}
}