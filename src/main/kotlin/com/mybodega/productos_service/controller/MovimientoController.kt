package com.mybodega.productos_service.controller

import com.mybodega.productos_service.dto.EstadisticasMovimientosDTO
import com.mybodega.productos_service.dto.MovimientoCreateDTO
import com.mybodega.productos_service.dto.MovimientoResponseDTO
import com.mybodega.productos_service.service.MovimientoService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = ["*"]) // Para desarrollo, limita en producción
class MovimientoController(
    private val movimientoService: MovimientoService
) {

    // GET /api/movimientos - Obtener todos los movimientos
    @GetMapping
    fun getAllMovimientos(): ResponseEntity<List<MovimientoResponseDTO>> {
        return ResponseEntity.ok(movimientoService.findAll())
    }

    // GET /api/movimientos/{id} - Obtener movimiento por ID
    @GetMapping("/{id}")
    fun getMovimientoById(@PathVariable id: Long): ResponseEntity<MovimientoResponseDTO> {
        return ResponseEntity.ok(movimientoService.findById(id))
    }

    // POST /api/movimientos - Crear movimiento manualmente
    @PostMapping
    fun createMovimiento(
        @Valid @RequestBody dto: MovimientoCreateDTO
    ): ResponseEntity<MovimientoResponseDTO> {
        val created = movimientoService.create(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    // GET /api/movimientos/tipo/{tipo} - Filtrar por tipo
    @GetMapping("/tipo/{tipo}")
    fun getMovimientosByTipo(
        @PathVariable tipo: String
    ): ResponseEntity<List<MovimientoResponseDTO>> {
        return ResponseEntity.ok(movimientoService.findByTipo(tipo))
    }

    // GET /api/movimientos/producto/{productoId} - Filtrar por producto
    @GetMapping("/producto/{productoId}")
    fun getMovimientosByProducto(
        @PathVariable productoId: Long
    ): ResponseEntity<List<MovimientoResponseDTO>> {
        return ResponseEntity.ok(movimientoService.findByProductoId(productoId))
    }

    // GET /api/movimientos/recientes - Últimos 50 movimientos
    @GetMapping("/recientes")
    fun getMovimientosRecientes(): ResponseEntity<List<MovimientoResponseDTO>> {
        return ResponseEntity.ok(movimientoService.findRecientes())
    }

    // GET /api/movimientos/hoy - Movimientos de hoy
    @GetMapping("/hoy")
    fun getMovimientosHoy(): ResponseEntity<List<MovimientoResponseDTO>> {
        return ResponseEntity.ok(movimientoService.findMovimientosHoy())
    }

    // GET /api/movimientos/semana - Movimientos de la semana
    @GetMapping("/semana")
    fun getMovimientosSemana(): ResponseEntity<List<MovimientoResponseDTO>> {
        return ResponseEntity.ok(movimientoService.findMovimientosSemana())
    }

    // GET /api/movimientos/rango?inicio=...&fin=... - Movimientos entre fechas
    @GetMapping("/rango")
    fun getMovimientosByRango(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) inicio: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) fin: LocalDateTime
    ): ResponseEntity<List<MovimientoResponseDTO>> {
        return ResponseEntity.ok(movimientoService.findByFechas(inicio, fin))
    }

    // GET /api/movimientos/estadisticas - Obtener estadísticas
    @GetMapping("/estadisticas")
    fun getEstadisticas(): ResponseEntity<EstadisticasMovimientosDTO> {
        return ResponseEntity.ok(movimientoService.getEstadisticas())
    }

    // DELETE /api/movimientos/limpiar - Limpiar todo el historial
    @DeleteMapping("/limpiar")
    fun clearAllMovimientos(): ResponseEntity<Void> {
        movimientoService.clearAll()
        return ResponseEntity.noContent().build()
    }
}