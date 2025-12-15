Información del ProyectoNombre: MyBodega API
Integrantes:

Maximiliano Martinez
Yetro Valenzuela
Descripción: API REST desarrollada en Kotlin con Spring Boot para la gestión de inventario de productos domésticos.Funcionalidades PrincipalesGestión de Productos

Crear, leer, actualizar y eliminar productos
Consumir stock (restar unidades)
Reabastecer stock (agregar unidades)
Buscar productos por nombre o categoría
Filtrar productos con stock bajo o agotados
Listar categorías disponibles
Gestión de Movimientos

Registro automático de todas las operaciones sobre productos
Consultar historial de movimientos
Filtrar movimientos por tipo, producto o fecha
Obtener estadísticas de movimientos
Limpiar historial completo
Gestión de Usuarios

Registro de nuevos usuarios
Login con validación de credenciales
Contraseñas encriptadas con BCrypt
Roles de usuario (ADMIN, USUARIO)
Endpoints DisponiblesProductos (/api/productos)GET    /api/productos                    - Listar todos los productos
GET    /api/productos/{id}               - Obtener producto por ID
POST   /api/productos                    - Crear nuevo producto
PUT    /api/productos/{id}               - Actualizar producto
DELETE /api/productos/{id}               - Eliminar producto
POST   /api/productos/consumir           - Consumir stock
POST   /api/productos/reabastecer        - Reabastecer stock
GET    /api/productos/search?nombre=     - Buscar por nombre
GET    /api/productos/categoria/{cat}    - Filtrar por categoría
GET    /api/productos/stock-bajo         - Productos con stock bajo
GET    /api/productos/agotados           - Productos agotados
GET    /api/productos/categorias         - Listar categoríasMovimientos (/api/movimientos)GET    /api/movimientos                  - Listar todos los movimientos
GET    /api/movimientos/{id}             - Obtener movimiento por ID
POST   /api/movimientos                  - Crear movimiento manual
GET    /api/movimientos/tipo/{tipo}      - Filtrar por tipo
GET    /api/movimientos/producto/{id}    - Filtrar por producto
GET    /api/movimientos/recientes        - Últimos 50 movimientos
GET    /api/movimientos/hoy              - Movimientos de hoy
GET    /api/movimientos/semana           - Movimientos de la semana
GET    /api/movimientos/estadisticas     - Obtener estadísticas
DELETE /api/movimientos/limpiar          - Limpiar historialUsuarios (/api/usuarios)POST   /api/usuarios/register            - Registrar usuario
POST   /api/usuarios/login               - Iniciar sesión
GET    /api/usuarios                     - Listar usuarios
GET    /api/usuarios/{id}                - Obtener usuario por IDRequisitos del Sistema
Java: JDK 17 o superior
Base de datos: MySQL 8.0 o superior
Gradle: 8.10.2 (incluido en wrapper)
Configuración de Base de Datos
Crear base de datos MySQL:
sqlCREATE DATABASE mybodega_db;
Configurar credenciales en src/main/resources/application.properties:
propertiesspring.datasource.url=jdbc:mysql://localhost:3306/mybodega_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseñaInstrucciones de EjecuciónOpción 1: Usando Gradle Wrapper (Recomendado)bash# Linux/Mac
./gradlew bootRun

# Windows
gradlew.bat bootRunOpción 2: Usando IDE
Abrir el proyecto en IntelliJ IDEA o Eclipse
Ejecutar la clase ProductosServiceApplication.kt
Opción 3: Generar JAR y ejecutarbash# Generar JAR
./gradlew build

# Ejecutar JAR
java -jar build/libs/productosservice-1.0.0.jarVerificar que la API está funcionandoLa API estará disponible en: http://localhost:8080Probar endpoint de salud:
bashcurl http://localhost:8080/actuator/healthEstructura del Proyectosrc/main/kotlin/com/mybodega/productos_service/
├── controller/          # Controladores REST
├── service/            # Lógica de negocio
├── repository/         # Acceso a datos (JPA)
├── model/              # Entidades de base de datos
├── dto/                # Data Transfer Objects
└── exception/          # Manejo de excepcionesTecnologías Utilizadas
Kotlin 2.0.21
Spring Boot 3.3.5
Spring Data JPA - Persistencia de datos
MySQL Connector 8.2.0
Spring Validation - Validación de datos
BCrypt - Encriptación de contraseñas
Jackson - Serialización JSON
Ejemplos de UsoCrear Productobashcurl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Arroz Integral",
    "categoria": "Alimentos",
    "cantidad": 10,
    "descripcion": "Arroz integral 1kg",
    "ubicacion": "Cocina - Alacena"
  }'Loginbashcurl -X POST http://localhost:8080/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@example.com",
    "password": "123456"
  }'Consumir Stockbashcurl -X POST http://localhost:8080/api/productos/consumir \
  -H "Content-Type: application/json" \
  -d '{
    "productoId": 1,
    "cantidad": 2
  }'Ejecución de Testsbash# Ejecutar todos los tests
./gradlew test

# Ver reporte de tests
./gradlew test --infoNotas Importantes
La API utiliza CORS abierto (origins = ["*"]) solo para desarrollo
En producción, configurar CORS específico y agregar autenticación JWT
Las contraseñas se almacenan hasheadas con BCrypt
Los movimientos se registran automáticamente en cada operación
Solución de ProblemasError de conexión a MySQL

Verificar que MySQL esté ejecutándose
Confirmar credenciales en application.properties
Asegurar que la base de datos existe
Puerto 8080 en uso

Cambiar puerto en application.properties:

propertiesserver.port=8081Error de compilación Kotlin

Verificar versión de JDK (debe ser 17+)
Limpiar y reconstruir: ./gradlew clean build
Contacto y ColaboraciónEste proyecto fue desarrollado como parte del curso de desarrollo móvil.Evidencia de trabajo colaborativo: Los commits del proyecto demuestran la participación de ambos integrantes en el desarrollo de la API, con contribuciones en controllers, services, y configuración de base de datos.
