package com.mybodega.productos_service.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "productos")
data class ProductoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 100)
    var nombre: String,

    @Column(nullable = false, length = 50)
    var categoria: String,

    @Column(nullable = false)
    var cantidad: Int = 0,

    @Column(length = 500)
    var descripcion: String? = null,

    @Column(length = 200)
    var ubicacion: String? = null,

    @Column(length = 500)
    var imagenUrl: String? = null,

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    val fechaCreacion: LocalDateTime = LocalDateTime.now(),

    @Column(name = "fecha_actualizacion")
    var fechaActualizacion: LocalDateTime = LocalDateTime.now()
) {
    @PreUpdate
    fun onUpdate() {
        fechaActualizacion = LocalDateTime.now()
    }
}