package com.mybodega.productos_service.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "usuarios")
data class UsuarioEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true, length = 100)
    var email: String,

    @Column(nullable = false)
    var password: String, // Debe estar hasheada con BCrypt

    @Column(nullable = false, length = 100)
    var nombre: String,

    @Column(length = 20)
    var rol: String = "USUARIO", // ADMIN, GERENTE, EMPLEADO, USUARIO

    @Column(nullable = false)
    var activo: Boolean = true,

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    val fechaCreacion: LocalDateTime = LocalDateTime.now(),

    @Column(name = "ultimo_acceso")
    var ultimoAcceso: LocalDateTime? = null
)
