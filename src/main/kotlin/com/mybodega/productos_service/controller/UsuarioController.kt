package com.mybodega.productos_service.controller

import com.mybodega.productos_service.dto.*
import com.mybodega.productos_service.service.UsuarioService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = ["*"])
class UsuarioController(
    private val usuarioService: UsuarioService
) {

    // POST /api/usuarios/register - Registrar usuario
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody dto: UsuarioCreateDTO
    ): ResponseEntity<UsuarioResponseDTO> {
        val created = usuarioService.register(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    // POST /api/usuarios/login - Login
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody dto: LoginRequestDTO
    ): ResponseEntity<LoginResponseDTO> {
        val response = usuarioService.login(dto)
        return ResponseEntity.ok(response)
    }

    // GET /api/usuarios - Obtener todos
    @GetMapping
    fun getAllUsuarios(): ResponseEntity<List<UsuarioResponseDTO>> {
        return ResponseEntity.ok(usuarioService.findAll())
    }

    // GET /api/usuarios/{id} - Obtener por ID
    @GetMapping("/{id}")
    fun getUsuarioById(@PathVariable id: Long): ResponseEntity<UsuarioResponseDTO> {
        return ResponseEntity.ok(usuarioService.findById(id))
    }
}