package com.mybodega.productos_service.service

import com.mybodega.productos_service.model.ProductoEntity
import com.mybodega.productos_service.exception.ResourceNotFoundException
import com.mybodega.productos_service.exception.DuplicateResourceException
import com.mybodega.productos_service.repository.ProductoRepository
import com.mybodega.productos_service.dto.ProductoCreateDTO
import com.mybodega.productos_service.dto.ProductoResponseDTO
import com.mybodega.productos_service.dto.ProductoUpdateDTO
import com.mybodega.productos_service.dto.StockOperationDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductoService(
    private val productoRepository: ProductoRepository,
    private val movimientoService: MovimientoService
) {

    // Obtener todos los productos
    fun findAll(): List<ProductoResponseDTO> {
        return productoRepository.findAll()
            .map { it.toResponseDTO() }
    }

    // Obtener producto por ID
    fun findById(id: Long): ProductoResponseDTO {
        val producto = productoRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Producto no encontrado con ID: $id") }
        return producto.toResponseDTO()
    }

    // Crear producto
    fun create(dto: ProductoCreateDTO): ProductoResponseDTO {
        // Validar que no exista producto con el mismo nombre
        if (productoRepository.existsByNombreIgnoreCase(dto.nombre)) {
            throw DuplicateResourceException("Ya existe un producto con el nombre: ${dto.nombre}")
        }

        val producto = ProductoEntity(
            nombre = dto.nombre,
            categoria = dto.categoria,
            cantidad = dto.cantidad,
            descripcion = dto.descripcion,
            ubicacion = dto.ubicacion,
            imagenUrl = dto.imagenUrl
        )

        val saved = productoRepository.save(producto)

        // Registrar movimiento
        movimientoService.registrarMovimiento(
            tipo = "Agregar",
            producto = saved.nombre,
            productoId = saved.id,
            cantidadAnterior = 0,
            cantidadNueva = saved.cantidad,
            detalles = "Producto creado con ${saved.cantidad} unidades"
        )

        return saved.toResponseDTO()
    }

    // Actualizar producto
    fun update(id: Long, dto: ProductoUpdateDTO): ProductoResponseDTO {
        val producto = productoRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Producto no encontrado con ID: $id") }

        val cantidadAnterior = producto.cantidad

        dto.nombre?.let { producto.nombre = it }
        dto.categoria?.let { producto.categoria = it }
        dto.cantidad?.let { producto.cantidad = it }
        dto.descripcion?.let { producto.descripcion = it }
        dto.ubicacion?.let { producto.ubicacion = it }
        dto.imagenUrl?.let { producto.imagenUrl = it }

        val updated = productoRepository.save(producto)

        // Registrar movimiento si cambió la cantidad
        if (dto.cantidad != null && cantidadAnterior != dto.cantidad) {
            movimientoService.registrarMovimiento(
                tipo = "Editar",
                producto = updated.nombre,
                productoId = updated.id,
                cantidadAnterior = cantidadAnterior,
                cantidadNueva = updated.cantidad,
                detalles = "Cantidad actualizada de $cantidadAnterior a ${updated.cantidad}"
            )
        } else {
            movimientoService.registrarMovimiento(
                tipo = "Editar",
                producto = updated.nombre,
                productoId = updated.id,
                detalles = "Información del producto actualizada"
            )
        }

        return updated.toResponseDTO()
    }

    // Eliminar producto
    fun delete(id: Long) {
        val producto = productoRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Producto no encontrado con ID: $id") }

        // Registrar movimiento antes de eliminar
        movimientoService.registrarMovimiento(
            tipo = "Eliminar",
            producto = producto.nombre,
            productoId = producto.id,
            cantidadAnterior = producto.cantidad,
            cantidadNueva = 0,
            detalles = "Producto eliminado del inventario"
        )

        productoRepository.delete(producto)
    }

    // Consumir producto (restar stock)
    fun consumir(dto: StockOperationDTO): ProductoResponseDTO {
        val producto = productoRepository.findById(dto.productoId)
            .orElseThrow { ResourceNotFoundException("Producto no encontrado con ID: ${dto.productoId}") }

        if (producto.cantidad < dto.cantidad) {
            throw IllegalStateException("Stock insuficiente. Disponible: ${producto.cantidad}, Requerido: ${dto.cantidad}")
        }

        val cantidadAnterior = producto.cantidad
        producto.cantidad -= dto.cantidad

        val updated = productoRepository.save(producto)

        // Registrar movimiento
        movimientoService.registrarMovimiento(
            tipo = "Consumo",
            producto = updated.nombre,
            productoId = updated.id,
            cantidadAnterior = cantidadAnterior,
            cantidadNueva = updated.cantidad,
            detalles = "Consumo de ${dto.cantidad} unidad(es)"
        )

        return updated.toResponseDTO()
    }

    // Reabastecer producto (sumar stock)
    fun reabastecer(dto: StockOperationDTO): ProductoResponseDTO {
        val producto = productoRepository.findById(dto.productoId)
            .orElseThrow { ResourceNotFoundException("Producto no encontrado con ID: ${dto.productoId}") }

        val cantidadAnterior = producto.cantidad
        producto.cantidad += dto.cantidad

        val updated = productoRepository.save(producto)

        // Registrar movimiento
        movimientoService.registrarMovimiento(
            tipo = "Reabastecimiento",
            producto = updated.nombre,
            productoId = updated.id,
            cantidadAnterior = cantidadAnterior,
            cantidadNueva = updated.cantidad,
            detalles = "Reabastecimiento de ${dto.cantidad} unidad(es)"
        )

        return updated.toResponseDTO()
    }

    // Buscar por nombre
    fun searchByNombre(nombre: String): List<ProductoResponseDTO> {
        return productoRepository.findByNombreContainingIgnoreCase(nombre)
            .map { it.toResponseDTO() }
    }

    // Buscar por categoría
    fun findByCategoria(categoria: String): List<ProductoResponseDTO> {
        return productoRepository.findByCategoria(categoria)
            .map { it.toResponseDTO() }
    }

    // Productos con stock bajo
    fun findStockBajo(limite: Int = 2): List<ProductoResponseDTO> {
        return productoRepository.findByCantidadLessThanEqual(limite)
            .map { it.toResponseDTO() }
    }

    // Productos agotados
    fun findAgotados(): List<ProductoResponseDTO> {
        return productoRepository.findByCantidad(0)
            .map { it.toResponseDTO() }
    }

    // Obtener todas las categorías
    fun findAllCategorias(): List<String> {
        return productoRepository.findAllCategorias()
    }

    // Extension function para convertir Entity a DTO
    private fun ProductoEntity.toResponseDTO() = ProductoResponseDTO(
        id = this.id,
        nombre = this.nombre,
        categoria = this.categoria,
        cantidad = this.cantidad,
        descripcion = this.descripcion,
        ubicacion = this.ubicacion,
        imagenUrl = this.imagenUrl,
        fechaCreacion = this.fechaCreacion,
        fechaActualizacion = this.fechaActualizacion
    )
}