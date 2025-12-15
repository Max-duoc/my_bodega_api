package com.mybodega.productos_service.service

import com.mybodega.productos_service.dto.*
import com.mybodega.productos_service.exception.DuplicateResourceException
import com.mybodega.productos_service.exception.ResourceNotFoundException
import com.mybodega.productos_service.model.UsuarioEntity
import com.mybodega.productos_service.repository.UsuarioRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class UsuarioService(
    private val usuarioRepository: UsuarioRepository
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    // Registrar nuevo usuario
    fun register(dto: UsuarioCreateDTO): UsuarioResponseDTO {
        // Validar que no exista el email
        if (usuarioRepository.existsByEmail(dto.email)) {
            throw DuplicateResourceException("Ya existe un usuario con el email: ${dto.email}")
        }

        // Crear usuario con contraseña hasheada
        val usuario = UsuarioEntity(
            email = dto.email,
            password = passwordEncoder.encode(dto.password),
            nombre = dto.nombre,
            rol = dto.rol
        )

        val saved = usuarioRepository.save(usuario)
        return saved.toResponseDTO()
    }

    // Login (validar credenciales)
    fun login(dto: LoginRequestDTO): LoginResponseDTO {
        val usuario = usuarioRepository.findByEmail(dto.email)
            .orElse(null)

        // Validar credenciales
        if (usuario == null || !passwordEncoder.matches(dto.password, usuario.password)) {
            return LoginResponseDTO(
                success = false,
                message = "Credenciales incorrectas"
            )
        }

        // Actualizar último acceso
        usuario.ultimoAcceso = LocalDateTime.now()
        usuarioRepository.save(usuario)

        return LoginResponseDTO(
            success = true,
            message = "Login exitoso",
            usuario = usuario.toResponseDTO()
        )
    }

    // Obtener todos los usuarios
    fun findAll(): List<UsuarioResponseDTO> {
        return usuarioRepository.findAll()
            .map { it.toResponseDTO() }
    }

    // Obtener usuario por ID
    fun findById(id: Long): UsuarioResponseDTO {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Usuario no encontrado con ID: $id") }
        return usuario.toResponseDTO()
    }

    // Cambiar contraseña
    fun changePassword(id: Long, oldPassword: String, newPassword: String): Boolean {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Usuario no encontrado") }

        if (!passwordEncoder.matches(oldPassword, usuario.password)) {
            return false
        }

        usuario.password = passwordEncoder.encode(newPassword)
        usuarioRepository.save(usuario)
        return true
    }

    private fun UsuarioEntity.toResponseDTO() = UsuarioResponseDTO(
        id = this.id,
        email = this.email,
        nombre = this.nombre,
        rol = this.rol,
        activo = this.activo,
        fechaCreacion = this.fechaCreacion,
        ultimoAcceso = this.ultimoAcceso
    )
}