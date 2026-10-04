# 📚 Biblioteca Escolar

Sistema de gestión de biblioteca escolar desarrollado en **Java**, utilizando **MySQL** como sistema gestor de base de datos y **JDBC** para la conexión.

El sistema permite administrar libros, estudiantes, préstamos y devoluciones, incorporando control de stock, autenticación de usuarios, reportes y ejecución de operaciones en segundo plano.

---

## 🎯 Descripción del proyecto

El proyecto busca entregar una solución para la administración de una biblioteca escolar, permitiendo centralizar la información de estudiantes, libros y préstamos.

La aplicación cuenta con diferentes funcionalidades según el tipo de usuario y mantiene la información almacenada de forma persistente en una base de datos MySQL.

Entre sus principales características se encuentran:

- Gestión de libros.
- Gestión de estudiantes.
- Registro de préstamos.
- Registro de devoluciones.
- Control automático del stock.
- Sistema de inicio de sesión.
- Roles de usuario.
- Reportes de información.
- Persistencia mediante MySQL.
- Concurrencia y ejecución de operaciones en segundo plano.
- Arquitectura organizada mediante capas.

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java 17 | Lenguaje principal |
| Java Swing | Interfaz gráfica |
| JDBC | Conexión con MySQL |
| MySQL | Base de datos |
| Maven | Gestión del proyecto y dependencias |
| IntelliJ IDEA | Entorno de desarrollo |

---

## 🏗️ Arquitectura del proyecto

El proyecto utiliza una estructura basada en **MVC**, complementada con las capas **DAO** y **Service**.

```text
src/main/java/cl/duoc/biblioteca/

├── app/
│   └── Main.java
│
├── config/
│   ├── DatabaseConfig.java
│   └── DatabaseConnection.java
│
├── controller/
│   ├── LoginController.java
│   └── BibliotecaController.java
│
├── dao/
│   ├── UsuarioDAO.java
│   ├── EstudianteDAO.java
│   ├── LibroDAO.java
│   ├── CategoriaDAO.java
│   └── PrestamoDAO.java
│
├── model/
│   ├── Usuario.java
│   ├── Persona.java
│   ├── Estudiante.java
│   ├── Libro.java
│   ├── Categoria.java
│   └── Prestamo.java
│
├── service/
│   ├── AutenticacionService.java
│   ├── EstudianteService.java
│   ├── LibroService.java
│   ├── PrestamoService.java
│   └── ReporteService.java
│
└── view/
    ├── LoginFrame.java
    ├── MainFrame.java
    ├── LibrosPanel.java
    ├── EstudiantesPanel.java
    ├── PrestamosPanel.java
    ├── ReportesPanel.java
    └── UI.java
