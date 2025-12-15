package com.mybodega.productos_service.repository

import com.mybodega.productos_service.model.UsuarioEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UsuarioRepository : JpaRepository<UsuarioEntity, Long> {
    fun findByEmail(email: String): Optional<UsuarioEntity>
    fun existsByEmail(email: String): Boolean
    fun findByRol(rol: String): List<UsuarioEntity>
}