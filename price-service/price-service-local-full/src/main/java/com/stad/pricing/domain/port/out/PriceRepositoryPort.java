package com.stad.pricing.domain.port.out;

import java.time.LocalDateTime;
import java.util.Optional;

import com.stad.pricing.domain.model.Price;

/**
 * Puerto de salida (Output Port) para acceder a los datos de precios.
 * Define la abstracción que debe ser implementada por cualquier mecanismo de persistencia (JDBC, JPA, API externa, etc.).
 * Forma parte del patrón Ports & Adapters en la arquitectura hexagonal.
 */
public interface PriceRepositoryPort {

 /**
  * Recupera el precio aplicable para una combinación específica de marca, producto y fecha/hora.
  * La lógica debe aplicar criterios de prioridad y vigencia (startDate <= applicationDate <= endDate).
  *
  * @param brandId          ID de la cadena comercial
  * @param productId        ID del producto
  * @param applicationDate  Fecha y hora en que se desea conocer el precio
  * @return Optional con el precio más relevante según las reglas de negocio, o vacío si no existe
  */
 Optional<Price> findApplicablePrice(long brandId, long productId, LocalDateTime applicationDate);
}
