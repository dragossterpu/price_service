package com.stad.pricing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Anotación que marca esta clase como punto de entrada principal de una aplicación Spring Boot.
 * Combina tres anotaciones comunes: @Configuration, @EnableAutoConfiguration y @ComponentScan.
 */
@SpringBootApplication
public class PricingApplication {

 
	/**
	 *  Método main: punto de arranque de la aplicación.
	 * SpringApplication.run() lanza el contexto de Spring, configura los beans y arranca el servidor embebido.
	 */
	 public static void main(String[] args) {
	     SpringApplication.run(PricingApplication.class, args);
	 }
}