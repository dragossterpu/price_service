package com.stad.pricing.adapter.out.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.stad.pricing.domain.model.Price;
import com.stad.pricing.domain.port.out.PriceRepositoryPort;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Indica que esta clase es un componente de tipo repositorio en Spring y será detectada automáticamente para inyección de dependencias.
 */
@Repository
public class JdbcPriceRepository implements PriceRepositoryPort {

 // JdbcTemplate permite interactuar con bases de datos relacionales de forma segura y concisa.
 private final JdbcTemplate jdbcTemplate;

 // Constructor con inyección de dependencias: Spring inyectará automáticamente el JdbcTemplate configurado.
 public JdbcPriceRepository(JdbcTemplate jdbcTemplate) {
     this.jdbcTemplate = jdbcTemplate;
 }

 /**
  * Implementación del puerto de salida PriceRepositoryPort.
  * Consulta la base de datos para encontrar el precio aplicable a una combinación de marca, producto y fecha.
  *
  * @param brandId       ID de la cadena comercial
  * @param productId     ID del producto
  * @param dateTime      Fecha y hora de aplicación
  * @return              Un Optional con el precio aplicable, o vacío si no se encuentra
  */
 @Override
 public Optional<Price> findApplicablePrice(long brandId, long productId, LocalDateTime dateTime) {

     // Consulta SQL parametrizada:
     // Filtra por brandId, productId y validez temporal (start_date <= fecha <= end_date)
     // Aplica orden por prioridad DESC y start_date más reciente para obtener el precio más relevante.
     // LIMIT 1 garantiza retorno único.
     String sql =
         "SELECT brand_id, product_id, start_date, end_date, price_list, priority, price, currency " +
         "FROM prices " +
         "WHERE brand_id = ? " +
         "AND product_id = ? " +
         "AND start_date <= ? " +
         "AND end_date >= ? " +
         "ORDER BY priority DESC, start_date DESC " +
         "LIMIT 1";

     // Ejecuta la consulta con los parámetros requeridos.
     // Mapea el resultado de la consulta a objetos Price del dominio.
     @SuppressWarnings("deprecation") // Se usa 'query' con lambda, considerada segura en este contexto.
     List<Price> results = jdbcTemplate.query(
             sql,
             new Object[]{
                 brandId,
                 productId,
                 Timestamp.valueOf(dateTime),
                 Timestamp.valueOf(dateTime)
             },
             (rs, rowNum) -> new Price(
                 rs.getLong("brand_id"),
                 rs.getLong("product_id"),
                 rs.getTimestamp("start_date").toLocalDateTime(),
                 rs.getTimestamp("end_date").toLocalDateTime(),
                 rs.getInt("price_list"),
                 rs.getInt("priority"),
                 rs.getBigDecimal("price"),
                 rs.getString("currency")
             )
     );

     // Devuelve el primer resultado si existe, o un Optional vacío si no hay coincidencias.
     return results.stream().findFirst();
 }
}

