package com.stad.pricing.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Clase inmutable que representa el modelo de dominio "Price" (Precio).
 * Se utiliza un Java record, que genera automáticamente constructor, getters, equals, hashCode y toString.
 * Esta clase encapsula todos los atributos necesarios para describir una tarifa de precio en vigor.
 */
public record Price(
 long brandId,                    // Identificador de la cadena comercial
 long productId,                  // Identificador del producto
 LocalDateTime startDate,        // Fecha y hora de inicio de validez de la tarifa
 LocalDateTime endDate,          // Fecha y hora de fin de validez de la tarifa
 int priceList,                  // Identificador de la tarifa (útil para trazabilidad)
 int priority,                   // Prioridad de la tarifa (mayor valor = más relevante)
 BigDecimal price,              // Precio final aplicable
 String currency                // Moneda del precio
) {}
