package com.mybodega.productos_service.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.*
import java.time.LocalDateTime

data class ProductoCreateDTO(
    @field:NotBlank(message = "El nombre es obligatorio")
    @field:Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    val nombre: String,

    @field:NotBlank(message = "La categoría es obligatoria")
    @field:Size(max = 50, message = "La categoría no puede exceder 50 caracteres")
    val categoria: String,

    @field:Min(value = 0, message = "La cantidad no puede ser negativa")
    val cantidad: Int = 0,

    @field:Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    val descripcion: String? = null,

    @field:Size(max = 200, message = "La ubicación no puede exceder 200 caracteres")
    val ubicacion: String? = null,

    @field:Size(max = 500, message = "La URL de imagen no puede exceder 500 caracteres")
    val imagenUrl: String? = null
)

// DTO para actualizar producto
data class ProductoUpdateDTO(
    @field:Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    val nombre: String? = null,

    @field:Size(max = 50, message = "La categoría no puede exceder 50 caracteres")
    val categoria: String? = null,

    @field:Min(value = 0, message = "La cantidad no puede ser negativa")
    val cantidad: Int? = null,

    @field:Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    val descripcion: String? = null,

    @field:Size(max = 200, message = "La ubicación no puede exceder 200 caracteres")
    val ubicacion: String? = null,

    @field:Size(max = 500, message = "La URL de imagen no puede exceder 500 caracteres")
    val imagenUrl: String? = null
)

// DTO de respuesta
data class ProductoResponseDTO(
    val id: Long,
    val nombre: String,
    val categoria: String,
    val cantidad: Int,
    val descripcion: String?,
    val ubicacion: String?,
    val imagenUrl: String?,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val fechaCreacion: LocalDateTime,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val fechaActualizacion: LocalDateTime
)

// DTO para operaciones de stock
data class StockOperationDTO(
    @field:NotNull(message = "El ID del producto es obligatorio")
    val productoId: Long,

    @field:Min(value = 1, message = "La cantidad debe ser al menos 1")
    val cantidad: Int = 1
)