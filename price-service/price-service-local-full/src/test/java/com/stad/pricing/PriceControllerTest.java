package com.stad.pricing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Clase de pruebas de integración para el controlador REST PriceController.
 * Utiliza SpringBootTest para levantar el contexto completo de la aplicación.
 * AutoConfigureMockMvc permite realizar peticiones HTTP simuladas sin necesidad de desplegar el servidor.
 * Cada test verifica que el endpoint /api/prices/search responde correctamente ante distintos escenarios de entrada.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PriceControllerTest {

 // Cliente HTTP simulado para probar el endpoint sin levantar servidor real
 @Autowired
 private MockMvc mockMvc; 

 /**
  * Test 1: Consulta el 14 de junio a las 10:00.
  * Se espera un precio de 50.00 EUR.
  */
 @Test
 void test1_requestAt10AMOn14th() throws Exception {
     mockMvc.perform(get("/api/prices/search")
             .param("brandId", "1")
             .param("productId", "35455")
             .param("applicationDate", "2020-06-14T10:00:00"))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.price").value(50.00));
 }

 /**
  * Test 2: Consulta el 14 de junio a las 16:00.
  * Aplica una tarifa diferente con mayor prioridad: 44.00 EUR.
  */
 @Test
 void test2_requestAt16PMOn14th() throws Exception {
     mockMvc.perform(get("/api/prices/search")
             .param("brandId", "1")
             .param("productId", "35455")
             .param("applicationDate", "2020-06-14T16:00:00"))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.price").value(44.00));
 }

 /**
  * Test 3: Consulta el 14 de junio a las 21:00.
  * Se espera una tarifa nocturna con precio 39.99 EUR.
  */
 @Test
 void test3_requestAt21PMOn14th() throws Exception {
     mockMvc.perform(get("/api/prices/search")
             .param("brandId", "1")
             .param("productId", "35455")
             .param("applicationDate", "2020-06-14T21:00:00"))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.price").value(39.99));
 }

 /**
  * Test 4: Consulta el 15 de junio a las 10:00.
  * Se espera una nueva tarifa diaria: 30.50 EUR.
  */
 @Test
 void test4_requestAt10AMOn15th() throws Exception {
     mockMvc.perform(get("/api/prices/search")
             .param("brandId", "1")
             .param("productId", "35455")
             .param("applicationDate", "2020-06-15T10:00:00"))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.price").value(30.50));
 }

 /**
  * Test 5: Consulta el 16 de junio a las 21:00.
  * Se espera que aplique la tarifa de continuidad: 41.00 EUR.
  */
 @Test
 void test5_requestAt21PMOn16th() throws Exception {
     mockMvc.perform(get("/api/prices/search")
             .param("brandId", "1")
             .param("productId", "35455")
             .param("applicationDate", "2020-06-16T21:00:00"))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.price").value(41.00));
 }
}
