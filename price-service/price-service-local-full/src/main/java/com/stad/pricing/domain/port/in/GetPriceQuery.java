package com.stad.pricing.domain.port.in;

import java.time.LocalDateTime;
import java.util.Optional;

import com.stad.pricing.domain.model.Price;

/**
 * Puerto de entrada (Use Case Input Port) para la consulta de precios.
 * Define el contrato que la capa de aplicación debe implementar para ejecutar la lógica de negocio.
 * Este patrón forma parte de la arquitectura hexagonal (Ports and Adapters).
 */
public interface GetPriceQuery {

 /**
  * Ejecuta el caso de uso para recuperar el precio aplicable a una combinación de
  * marca, producto y fecha/hora de aplicación.
  *
  * @param brandId          ID de la cadena comercial
  * @param productId        ID del producto
  * @param applicationDate  Fecha y hora en la que se desea conocer el precio
  * @return Optional con el precio si existe una tarifa válida, vacío en caso contrario
  */
 Optional<Price> execute(long brandId, long productId, LocalDateTime applicationDate);
}
