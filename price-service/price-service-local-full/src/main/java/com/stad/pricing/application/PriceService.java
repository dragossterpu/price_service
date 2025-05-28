package com.stad.pricing.application;

import org.springframework.stereotype.Service;

import com.stad.pricing.domain.model.Price;
import com.stad.pricing.domain.port.in.GetPriceQuery;
import com.stad.pricing.domain.port.out.PriceRepositoryPort;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Indica que esta clase es un componente de servicio en Spring. Se detecta automáticamente para inyección de dependencias.
 */
@Service
public class PriceService implements GetPriceQuery {

 // Puerto de salida: abstracción que representa la fuente de datos de precios.
 // Permite desacoplar la lógica de negocio de la implementación específica del repositorio.
 private final PriceRepositoryPort priceRepositoryPort;

 // Inyección de dependencias a través del constructor.
 // Recibe un componente que implementa el puerto de salida, como JdbcPriceRepository.
 public PriceService(PriceRepositoryPort priceRepositoryPort) {
     this.priceRepositoryPort = priceRepositoryPort;
 }

 /**
  * Método principal del caso de uso: consulta el precio aplicable a partir de los parámetros de entrada.
  * Implementa el puerto de entrada GetPriceQuery, siguiendo los principios de arquitectura hexagonal.
  *
  * @param brandId          ID de la cadena comercial
  * @param productId        ID del producto
  * @param applicationDate  Fecha y hora de aplicación del precio
  * @return                 Un Optional con el precio aplicable o vacío si no se encuentra
  */
 @Override
 public Optional<Price> execute(long brandId, long productId, LocalDateTime applicationDate) {
     // Delegación de la lógica de búsqueda al repositorio, manteniendo este servicio como una capa de orquestación.
     return priceRepositoryPort.findApplicablePrice(brandId, productId, applicationDate);
 }
}
