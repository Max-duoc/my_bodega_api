package com.mybodega.productos_service.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "movimientos")
data class MovimientoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 50)
    val tipo: String, // "Agregar", "Editar", "Eliminar", "Consumo", "Reabastecimiento"

    @Column(nullable = false, length = 100)
    val producto: String,

    @Column(name = "producto_id")
    val productoId: Long? = null,

    @Column(name = "cantidad_anterior")
    val cantidadAnterior: Int? = null,

    @Column(name = "cantidad_nueva")
    val cantidadNueva: Int? = null,

    @Column(nullable = false)
    val fecha: LocalDateTime = LocalDateTime.now(),

    @Column(length = 500)
    val detalles: String? = null
)