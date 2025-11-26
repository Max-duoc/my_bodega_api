package com.mybodega.productos_service.service

import com.mybodega.productos_service.dto.EstadisticasMovimientosDTO
import com.mybodega.productos_service.dto.MovimientoCreateDTO
import com.mybodega.productos_service.dto.MovimientoResponseDTO
import com.mybodega.productos_service.model.MovimientoEntity
import com.mybodega.productos_service.exception.ResourceNotFoundException
import com.mybodega.productos_service.repository.MovimientoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class MovimientoService(
    private val movimientoRepository: MovimientoRepository
) {

    // Obtener todos los movimientos
    fun findAll(): List<MovimientoResponseDTO> {
        return movimientoRepository.findAllByOrderByFechaDesc()
            .map { it.toResponseDTO() }
    }

    // Obtener movimiento por ID
    fun findById(id: Long): MovimientoResponseDTO {
        val movimiento = movimientoRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Movimiento no encontrado con ID: $id") }
        return movimiento.toResponseDTO()
    }

    // Crear movimiento
    fun create(dto: MovimientoCreateDTO): MovimientoResponseDTO {
        val movimiento = MovimientoEntity(
            tipo = dto.tipo,
            producto = dto.producto,
            productoId = dto.productoId,
            cantidadAnterior = dto.cantidadAnterior,
            cantidadNueva = dto.cantidadNueva,
            detalles = dto.detalles
        )

        val saved = movimientoRepository.save(movimiento)
        return saved.toResponseDTO()
    }

    // Registrar movimiento (usado internamente por ProductoService)
    fun registrarMovimiento(
        tipo: String,
        producto: String,
        productoId: Long? = null,
        cantidadAnterior: Int? = null,
        cantidadNueva: Int? = null,
        detalles: String? = null
    ): MovimientoResponseDTO {
        val movimiento = MovimientoEntity(
            tipo = tipo,
            producto = producto,
            productoId = productoId,
            cantidadAnterior = cantidadAnterior,
            cantidadNueva = cantidadNueva,
            detalles = detalles
        )

        val saved = movimientoRepository.save(movimiento)
        return saved.toResponseDTO()
    }

    // Obtener movimientos por tipo
    fun findByTipo(tipo: String): List<MovimientoResponseDTO> {
        return movimientoRepository.findByTipo(tipo)
            .map { it.toResponseDTO() }
    }

    // Obtener movimientos por producto ID
    fun findByProductoId(productoId: Long): List<MovimientoResponseDTO> {
        return movimientoRepository.findByProductoId(productoId)
            .map { it.toResponseDTO() }
    }

    // Movimientos recientes (últimos 50)
    fun findRecientes(): List<MovimientoResponseDTO> {
        return movimientoRepository.findTop50ByOrderByFechaDesc()
            .map { it.toResponseDTO() }
    }

    // Movimientos de hoy
    fun findMovimientosHoy(): List<MovimientoResponseDTO> {
        return movimientoRepository.findMovimientosHoy()
            .map { it.toResponseDTO() }
    }

    // Movimientos de la semana
    fun findMovimientosSemana(): List<MovimientoResponseDTO> {
        val fechaInicio = LocalDateTime.now().minusWeeks(1)
        return movimientoRepository.findMovimientosSemana(fechaInicio)
            .map { it.toResponseDTO() }
    }

    // Movimientos entre fechas
    fun findByFechas(
        fechaInicio: LocalDateTime,
        fechaFin: LocalDateTime
    ): List<MovimientoResponseDTO> {
        return movimientoRepository.findByFechaBetween(fechaInicio, fechaFin)
            .map { it.toResponseDTO() }
    }

    // Obtener estadísticas
    fun getEstadisticas(): EstadisticasMovimientosDTO {
        val total = movimientoRepository.count()

        // Movimientos por tipo
        val porTipo = movimientoRepository.countByTipoGrouped()
            .associate { it[0] as String to it[1] as Long }

        // Movimientos de hoy
        val hoy = movimientoRepository.findMovimientosHoy().size.toLong()

        // Movimientos de la semana
        val fechaSemana = LocalDateTime.now().minusWeeks(1)
        val semana = movimientoRepository.findMovimientosSemana(fechaSemana).size.toLong()

        // Movimientos del mes
        val fechaMes = LocalDateTime.now().minusMonths(1)
        val mes = movimientoRepository.findMovimientosSemana(fechaMes).size.toLong()

        return EstadisticasMovimientosDTO(
            totalMovimientos = total,
            movimientosPorTipo = porTipo,
            movimientosHoy = hoy,
            movimientosEstaSemana = semana,
            movimientosEsteMes = mes
        )
    }

    // Limpiar historial
    fun clearAll() {
        movimientoRepository.deleteAll()
    }

    // Extension function para convertir Entity a DTO
    private fun MovimientoEntity.toResponseDTO() = MovimientoResponseDTO(
        id = this.id,
        tipo = this.tipo,
        producto = this.producto,
        productoId = this.productoId,
        cantidadAnterior = this.cantidadAnterior,
        cantidadNueva = this.cantidadNueva,
        fecha = this.fecha,
        detalles = this.detalles
    )
}