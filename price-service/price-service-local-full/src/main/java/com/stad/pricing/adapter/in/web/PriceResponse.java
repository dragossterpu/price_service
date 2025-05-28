package com.stad.pricing.adapter.in.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.stad.pricing.domain.model.Price;


/**
 * Define un record de Java para representar de forma inmutable la respuesta de precios del servicio REST.
 * Los records son ideales para estructuras de datos que solo contienen valores y no requieren lógica interna compleja.
*/
public record PriceResponse(
 long brandId,                 // ID de la cadena comercial
 long productId,               // ID del producto consultado
 int priceList,                // Identificador de la tarifa aplicada
 LocalDateTime startDate,      // Fecha y hora de inicio de validez del precio
 LocalDateTime endDate,        // Fecha y hora de fin de validez del precio
 BigDecimal price,             // Precio aplicable
 String currency               // Moneda del precio
) {

 /**
  * Método de fábrica estático para construir un PriceResponse a partir de un objeto de dominio Price.
  * Este método sirve de mapeador entre la capa de dominio (entidad interna) y la capa de presentación (respuesta REST).
  *
  * @param price objeto de dominio Price (normalmente retornado desde el caso de uso o repositorio)
  * @return instancia inmutable de PriceResponse lista para ser enviada al cliente
  */
 public static PriceResponse from(Price price) {
     return new PriceResponse(
         price.brandId(),
         price.productId(),
         price.priceList(),
         price.startDate(),
         price.endDate(),
         price.price(),
         price.currency()
     );
 }
}
