package com.mybodega.productos_service.exception

/**
 * Excepción lanzada cuando no se encuentra un recurso solicitado
 * HTTP Status: 404 NOT FOUND
 */
class ResourceNotFoundException(message: String) : RuntimeException(message)

/**
 * Excepción lanzada cuando se intenta crear un recurso que ya existe
 * HTTP Status: 409 CONFLICT
 */
class DuplicateResourceException(message: String) : RuntimeException(message)

/**
 * Excepción lanzada para operaciones de negocio inválidas
 * HTTP Status: 400 BAD REQUEST
 */
class InvalidOperationException(message: String) : RuntimeException(message)