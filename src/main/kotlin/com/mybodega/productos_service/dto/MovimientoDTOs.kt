package com.mybodega.productos_service.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.*
import java.time.LocalDateTime

// DTO para crear movimiento
data class MovimientoCreateDTO(
    @field:NotBlank(message = "El tipo es obligatorio")
    @field:Size(max = 50, message = "El tipo no puede exceder 50 caracteres")
    val tipo: String,

    @field:NotBlank(message = "El nombre del producto es obligatorio")
    @field:Size(max = 100, message = "El nombre del producto no puede exceder 100 caracteres")
    val producto: String,

    val productoId: Long? = null,

    val cantidadAnterior: Int? = null,

    val cantidadNueva: Int? = null,

    @field:Size(max = 500, message = "Los detalles no pueden exceder 500 caracteres")
    val detalles: String? = null
)

// DTO de respuesta
data class MovimientoResponseDTO(
    val id: Long,
    val tipo: String,
    val producto: String,
    val productoId: Long?,
    val cantidadAnterior: Int?,
    val cantidadNueva: Int?,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val fecha: LocalDateTime,

    val detalles: String?
)

// DTO para estadísticas de movimientos
data class EstadisticasMovimientosDTO(
    val totalMovimientos: Long,
    val movimientosPorTipo: Map<String, Long>,
    val movimientosHoy: Long,
    val movimientosEstaSemana: Long,
    val movimientosEsteMes: Long
)