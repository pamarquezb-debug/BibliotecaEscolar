# Biblioteca Escolar

**Evaluación Final Transversal (EFT) — Programación Orientada a Objetos II**  
**Autor:** Pablo Márquez  
**Tecnologías:** Java, Swing, JDBC y MySQL  
**IDE de desarrollo:** IntelliJ IDEA

## Descripción

Biblioteca Escolar es una aplicación de escritorio para administrar libros, estudiantes, categorías y préstamos de una biblioteca escolar. Utiliza una interfaz gráfica desarrollada con Java Swing, persistencia en MySQL mediante JDBC y una organización por capas basada en MVC y DAO.

El sistema distingue dos roles: **bibliotecario**, con acceso a funciones administrativas, y **estudiante**, con acceso a consulta de libros y operaciones de préstamo y devolución asociadas a su registro.

## Funcionalidades

- **Autenticación:** ingreso con correo y contraseña registrados en MySQL; acceso según rol.
- **Libros:** crear, consultar, actualizar y eliminar libros, con categoría y stock.
- **Estudiantes:** crear, consultar, actualizar y eliminar registros; validación de RUT chileno mediante dígito verificador (módulo 11).
- **Categorías:** crear, consultar, actualizar y eliminar categorías.
- **Préstamos y devoluciones:** verificar disponibilidad, registrar préstamo, calcular vencimiento a siete días, identificar atrasos, registrar devolución y actualizar stock.
- **Reportes:** libros más prestados, historial de préstamos y préstamos activos.

### Permisos

| Función | Bibliotecario | Estudiante |
|---|---|---|
| Consultar libros | Sí | Sí |
| Crear, modificar y eliminar libros | Sí | No |
| Administrar estudiantes | Sí | No |
| Administrar categorías | Sí | No |
| Registrar préstamos y devoluciones | Sí | Sí, asociados a su registro |
| Reportes administrativos | Sí | No |

## Arquitectura y conceptos de POO

El proyecto utiliza:

- **MVC:** separación de modelos (`modelo`), vistas (`vista`) y controladores (`controlador`).
- **DAO:** clases `*DAO` responsables de ejecutar consultas SQL y operaciones CRUD.
- **Singleton:** `DatabaseConnection.getInstance()` centraliza la configuración de acceso a MySQL. Cada operación obtiene una conexión JDBC según sea necesario.
- **Herencia y polimorfismo:** `Usuario` es una clase abstracta, extendida por `Bibliotecario` y `EstudianteUsuario`.
- **Interfaces:** `Prestable`, implementada por `Libro`.
- **Concurrencia:** `ServicioPrestamos` ejecuta préstamos y devoluciones mediante `ExecutorService`, sin bloquear el hilo de interfaz de Swing. Los resultados se comunican a la interfaz mediante `SwingUtilities.invokeLater`.
- **Consistencia:** operaciones sincronizadas sobre el modelo `Libro`; las modificaciones de stock en la base de datos usan transacciones y bloqueo de filas (`SELECT ... FOR UPDATE`).

## Estructura del proyecto

```text
BibliotecaEscolar/
├── src/
│   ├── controlador/
│   │   ├── AuthController.java
│   │   ├── LibroController.java
│   │   ├── EstudianteController.java
│   │   ├── CategoriaController.java
│   │   ├── PrestamoController.java
│   │   └── ReporteController.java
│   ├── dao/
│   │   ├── DatabaseConnection.java
│   │   ├── UsuarioDAO.java
│   │   ├── LibroDAO.java
│   │   ├── EstudianteDAO.java
│   │   ├── CategoriaDAO.java
│   │   ├── PrestamoDAO.java
│   │   └── ReporteDAO.java
│   ├── modelo/
│   │   ├── Usuario.java
│   │   ├── Bibliotecario.java
│   │   ├── EstudianteUsuario.java
│   │   ├── Estudiante.java
│   │   ├── Categoria.java
│   │   ├── Libro.java
│   │   ├── Prestamo.java
│   │   └── Prestable.java
│   ├── servicio/
│   │   └── ServicioPrestamos.java
│   ├── util/
│   │   └── RutUtil.java
│   ├── vista/
│   │   ├── VentanaLogin.java
│   │   ├── VentanaPrincipal.java
│   │   ├── VentanaLibros.java
│   │   ├── VentanaEstudiantes.java
│   │   ├── VentanaCategorias.java
│   │   ├── VentanaPrestamos.java
│   │   └── VentanaReportes.java
│   └── Main.java
├── Script_crea_tablas_biblioteca.sql
├── Script_poblado_tablas_biblioteca.sql
└── README.md
```

> La estructura corresponde a la organización prevista para la entrega. Verifica los nombres y ubicaciones de los archivos SQL antes de publicar.

## Requisitos

- JDK compatible con el proyecto configurado en IntelliJ IDEA.
- MySQL Server en ejecución.
- MySQL Connector/J incorporado como dependencia del proyecto.
- IntelliJ IDEA para compilar y ejecutar desde el código fuente.

## Clonar el repositorio

Una vez publicado en GitHub:

```bash
git clone URL_DEL_REPOSITORIO
cd BibliotecaEscolar
```

Reemplaza `URL_DEL_REPOSITORIO` por la dirección real del repositorio público.

## Configuración de MySQL

1. Inicia el servidor MySQL.
2. Crea la base de datos si todavía no existe:

   ```sql
   CREATE DATABASE IF NOT EXISTS biblioteca;
   USE biblioteca;
   ```

3. Ejecuta **primero** `Script_crea_tablas_biblioteca.sql` y **después** `Script_poblado_tablas_biblioteca.sql`, siguiendo las instrucciones de los scripts oficiales.
4. Comprueba que existan las tablas `usuarios`, `estudiantes`, `libros`, `categorias` y `prestamos`.
5. Revisa `src/dao/DatabaseConnection.java` y adapta `URL`, `USER` y `PASSWORD` a tu instalación local. La configuración de desarrollo utiliza `jdbc:mysql://localhost:3306/biblioteca`.

**Seguridad:** no publiques contraseñas reales ni credenciales privadas en GitHub. La autenticación del proyecto utiliza el esquema entregado para la actividad académica; un sistema de producción debería utilizar contraseñas cifradas mediante hash seguro y configuración externa para los secretos.

## Ejecución desde IntelliJ IDEA

1. Abre la carpeta del proyecto en IntelliJ IDEA.
2. Configura el **Project SDK** con el JDK instalado.
3. Agrega `mysql-connector-j` a las dependencias del módulo si no se encuentra configurado.
4. Confirma que MySQL esté iniciado y que `DatabaseConnection.java` contenga los parámetros correctos.
5. Ejecuta la clase `Main.java`.
6. Inicia sesión con una cuenta de prueba incluida en el script de poblamiento.

## Ejecutable

La entrega debe incorporar un ejecutable Java funcional. Al generar el archivo JAR, verifica que:

- La clase principal sea `Main`.
- El controlador JDBC de MySQL esté incluido o disponible en el classpath.
- El JAR abra la interfaz fuera de IntelliJ IDEA.
- La base de datos MySQL esté disponible y configurada.

**Pendiente de documentar:** nombre y ubicación definitivos del JAR y comando de ejecución, una vez que se genere y pruebe.

## Pruebas funcionales recomendadas

1. **Inicio de sesión:** ingresar como bibliotecario y estudiante; comprobar los permisos de cada rol.
2. **Libros:** crear un libro de prueba, consultarlo, editarlo y eliminarlo.
3. **Estudiantes:** comprobar el CRUD y rechazar RUT con dígito verificador incorrecto.
4. **Categorías:** crear, consultar, modificar y eliminar una categoría temporal sin libros asociados.
5. **Préstamos:** prestar un libro con stock disponible y comprobar que disminuye en una unidad.
6. **Devoluciones:** devolver ese préstamo y comprobar que el stock aumenta en una unidad.
7. **Reportes:** verificar libros más prestados, historial y préstamos activos.
8. **Concurrencia:** verificar que no se registre un préstamo adicional cuando el stock es cero y que la interfaz siga respondiendo durante la operación.

## Modelo de datos

La aplicación utiliza las tablas proporcionadas para la EFT:

| Tabla | Propósito |
|---|---|
| `usuarios` | Credenciales y roles de acceso |
| `estudiantes` | Información académica y personal |
| `libros` | Catálogo y existencias |
| `categorias` | Clasificación de libros |
| `prestamos` | Registro de préstamos, vencimiento y estado de devolución |

La columna `fecha_devolucion` se utiliza como **fecha prevista de vencimiento** y `devuelto` indica si el préstamo ya fue devuelto.

## Entrega académica

La Evaluación Final Transversal contempla:

- Archivo ZIP o RAR con el proyecto, código fuente, scripts SQL y ejecutable funcional.
- Repositorio público en GitHub con código y estructura organizada.
- Video explicativo de **3 a 5 minutos** grabado mediante Kaltura, mostrando funcionamiento, IDE, decisiones de diseño y rostro del estudiante en algún momento.

## Autor

**Pablo Márquez**  
Evaluación Final Transversal — Programación Orientada a Objetos II  
Duoc UC — 2026
