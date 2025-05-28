package com.stad.pricing.adapter.in.web;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stad.pricing.domain.port.in.GetPriceQuery;

import java.time.LocalDateTime;


/**
 * Declara esta clase como un controlador REST de Spring.
 * Todos los métodos devuelven directamente datos JSON (no vistas HTML).
 */
@RestController


/**
 * Define la ruta base para todos los endpoints dentro de este controlador.
 * Todas las rutas expuestas comenzarán con "/api/prices".
 */
@RequestMapping("/api/prices")
public class PriceController {

	 // Inyección del caso de uso encargado de obtener el precio.
	 // Sigue el patrón Command/Query (en este caso, una Query).
	 private final GetPriceQuery getPriceQuery;
	
	 // Constructor con inyección de dependencias por constructor.
	 // Permite que Spring inyecte automáticamente una implementación de GetPriceQuery.
	 public PriceController(GetPriceQuery getPriceQuery) {
	     this.getPriceQuery = getPriceQuery;
	 }
	
	 
	 /**
	  * Define un endpoint GET accesible mediante /api/prices/search
	  * Maneja la lógica de consulta del precio vigente para un producto y una marca en una fecha dada.
	  */
	 @GetMapping("/search")
	 public ResponseEntity<PriceResponse> getPrice(
	         // Extrae el parámetro brandId de la URL como tipo long
	         @RequestParam long brandId,
	         
	         // Extrae el parámetro productId de la URL como tipo long
	         @RequestParam long productId,
	         
	         // Extrae el parámetro applicationDate como LocalDateTime y lo interpreta en formato ISO 8601
	         @RequestParam(name = "applicationDate") 
	         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
	         LocalDateTime applicationDate
	 ) {
	     // Ejecuta la lógica de negocio usando la clase GetPriceQuery (caso de uso).
	     // Devuelve un Optional<Price>, que se transforma en PriceResponse si existe.
	     return getPriceQuery.execute(brandId, productId, applicationDate)
	             .map(PriceResponse::from) // Mapea el resultado de dominio a DTO (respuesta REST).
	             .map(ResponseEntity::ok) // Devuelve 200 OK si se encuentra un precio aplicable.
	             .orElse(ResponseEntity.notFound().build()); // Devuelve 404 Not Found si no hay coincidencias.
	 }
}