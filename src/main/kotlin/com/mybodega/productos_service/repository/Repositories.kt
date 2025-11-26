package com.mybodega.productos_service.repository

import com.mybodega.productos_service.model.MovimientoEntity
import com.mybodega.productos_service.model.ProductoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface ProductoRepository : JpaRepository<ProductoEntity, Long> {

    // Buscar por nombre (case insensitive)
    fun findByNombreContainingIgnoreCase(nombre: String): List<ProductoEntity>

    // Buscar por categoría
    fun findByCategoria(categoria: String): List<ProductoEntity>

    // Buscar productos con stock bajo
    fun findByCantidadLessThanEqual(cantidad: Int): List<ProductoEntity>

    // Buscar productos agotados
    fun findByCantidad(cantidad: Int): List<ProductoEntity>

    // Contar productos por categoría
    fun countByCategoria(categoria: String): Long

    // Verificar si existe producto con ese nombre
    fun existsByNombreIgnoreCase(nombre: String): Boolean

    // Obtener todas las categorías únicas
    @Query("SELECT DISTINCT p.categoria FROM ProductoEntity p ORDER BY p.categoria")
    fun findAllCategorias(): List<String>

    // Productos más recientes
    fun findTop10ByOrderByFechaCreacionDesc(): List<ProductoEntity>
}

@Repository
interface MovimientoRepository : JpaRepository<MovimientoEntity, Long> {

    // Buscar por tipo de movimiento
    fun findByTipo(tipo: String): List<MovimientoEntity>

    // Buscar por producto ID
    fun findByProductoId(productoId: Long): List<MovimientoEntity>

    // Movimientos ordenados por fecha
    fun findAllByOrderByFechaDesc(): List<MovimientoEntity>

    // Movimientos recientes (últimos 50)
    fun findTop50ByOrderByFechaDesc(): List<MovimientoEntity>

    // Movimientos entre fechas
    fun findByFechaBetween(
        fechaInicio: LocalDateTime,
        fechaFin: LocalDateTime
    ): List<MovimientoEntity>

    // Contar movimientos por tipo
    fun countByTipo(tipo: String): Long

    // Movimientos de hoy - CORREGIDO
    @Query("""
        SELECT m FROM MovimientoEntity m 
        WHERE m.fecha >= :fechaInicio 
        ORDER BY m.fecha DESC
    """)
    fun findMovimientosHoy(@Param("fechaInicio") fechaInicio: LocalDateTime): List<MovimientoEntity>

    // Movimientos de la semana
    @Query("""
        SELECT m FROM MovimientoEntity m 
        WHERE m.fecha >= :fechaInicio 
        ORDER BY m.fecha DESC
    """)
    fun findMovimientosSemana(@Param("fechaInicio") fechaInicio: LocalDateTime): List<MovimientoEntity>

    // Estadísticas por tipo
    @Query("""
        SELECT m.tipo, COUNT(m) 
        FROM MovimientoEntity m 
        GROUP BY m.tipo
    """)
    fun countByTipoGrouped(): List<Array<Any>>
}