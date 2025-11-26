package com.mybodega.productos_service.controller

import com.mybodega.productos_service.service.ProductoService
import com.mybodega.productos_service.dto.ProductoCreateDTO
import com.mybodega.productos_service.dto.ProductoResponseDTO
import com.mybodega.productos_service.dto.ProductoUpdateDTO
import com.mybodega.productos_service.dto.StockOperationDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = ["*"]) // Para desarrollo, limita en producción
class ProductoController(
    private val productoService: ProductoService
) {

    // GET /api/productos - Obtener todos los productos
    @GetMapping
    fun getAllProductos(): ResponseEntity<List<ProductoResponseDTO>> {
        return ResponseEntity.ok(productoService.findAll())
    }

    // GET /api/productos/{id} - Obtener producto por ID
    @GetMapping("/{id}")
    fun getProductoById(@PathVariable id: Long): ResponseEntity<ProductoResponseDTO> {
        return ResponseEntity.ok(productoService.findById(id))
    }

    // POST /api/productos - Crear producto
    @PostMapping
    fun createProducto(
        @Valid @RequestBody dto: ProductoCreateDTO
    ): ResponseEntity<ProductoResponseDTO> {
        val created = productoService.create(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    // PUT /api/productos/{id} - Actualizar producto
    @PutMapping("/{id}")
    fun updateProducto(
        @PathVariable id: Long,
        @Valid @RequestBody dto: ProductoUpdateDTO
    ): ResponseEntity<ProductoResponseDTO> {
        val updated = productoService.update(id, dto)
        return ResponseEntity.ok(updated)
    }

    // DELETE /api/productos/{id} - Eliminar producto
    @DeleteMapping("/{id}")
    fun deleteProducto(@PathVariable id: Long): ResponseEntity<Void> {
        productoService.delete(id)
        return ResponseEntity.noContent().build()
    }

    // POST /api/productos/consumir - Consumir producto (restar stock)
    @PostMapping("/consumir")
    fun consumirProducto(
        @Valid @RequestBody dto: StockOperationDTO
    ): ResponseEntity<ProductoResponseDTO> {
        val updated = productoService.consumir(dto)
        return ResponseEntity.ok(updated)
    }

    // POST /api/productos/reabastecer - Reabastecer producto (sumar stock)
    @PostMapping("/reabastecer")
    fun reabastecerProducto(
        @Valid @RequestBody dto: StockOperationDTO
    ): ResponseEntity<ProductoResponseDTO> {
        val updated = productoService.reabastecer(dto)
        return ResponseEntity.ok(updated)
    }

    // GET /api/productos/search?nombre={nombre} - Buscar por nombre
    @GetMapping("/search")
    fun searchProductos(
        @RequestParam nombre: String
    ): ResponseEntity<List<ProductoResponseDTO>> {
        return ResponseEntity.ok(productoService.searchByNombre(nombre))
    }

    // GET /api/productos/categoria/{categoria} - Filtrar por categoría
    @GetMapping("/categoria/{categoria}")
    fun getProductosByCategoria(
        @PathVariable categoria: String
    ): ResponseEntity<List<ProductoResponseDTO>> {
        return ResponseEntity.ok(productoService.findByCategoria(categoria))
    }

    // GET /api/productos/stock-bajo?limite={limite} - Productos con stock bajo
    @GetMapping("/stock-bajo")
    fun getStockBajo(
        @RequestParam(defaultValue = "2") limite: Int
    ): ResponseEntity<List<ProductoResponseDTO>> {
        return ResponseEntity.ok(productoService.findStockBajo(limite))
    }

    // GET /api/productos/agotados - Productos agotados
    @GetMapping("/agotados")
    fun getAgotados(): ResponseEntity<List<ProductoResponseDTO>> {
        return ResponseEntity.ok(productoService.findAgotados())
    }

    // GET /api/productos/categorias - Obtener todas las categorías
    @GetMapping("/categorias")
    fun getCategorias(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(productoService.findAllCategorias())
    }
}