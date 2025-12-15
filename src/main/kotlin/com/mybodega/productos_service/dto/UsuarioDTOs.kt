package com.mybodega.productos_service.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.*
import java.time.LocalDateTime

// DTO para login
data class LoginRequestDTO(
    @field:NotBlank(message = "El email es obligatorio")
    @field:Email(message = "Email inválido")
    val email: String,

    @field:NotBlank(message = "La contraseña es obligatoria")
    @field:Size(min = 4, message = "Mínimo 4 caracteres")
    val password: String
)

// DTO para registro
data class UsuarioCreateDTO(
    @field:NotBlank(message = "El email es obligatorio")
    @field:Email(message = "Email inválido")
    val email: String,

    @field:NotBlank(message = "La contraseña es obligatoria")
    @field:Size(min = 6, message = "Mínimo 6 caracteres")
    val password: String,

    @field:NotBlank(message = "El nombre es obligatorio")
    @field:Size(max = 100, message = "Máximo 100 caracteres")
    val nombre: String,

    @field:Size(max = 20)
    val rol: String = "USUARIO"
)

// DTO de respuesta (sin password)
data class UsuarioResponseDTO(
    val id: Long,
    val email: String,
    val nombre: String,
    val rol: String,
    val activo: Boolean,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val fechaCreacion: LocalDateTime,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val ultimoAcceso: LocalDateTime?
)

// DTO para respuesta de login
data class LoginResponseDTO(
    val success: Boolean,
    val message: String,
    val usuario: UsuarioResponseDTO? = null,
    val token: String? = null // Opcional para JWT futuro
)