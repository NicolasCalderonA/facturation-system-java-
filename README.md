# Sistema de Facturación - Trabajo Especial Paradigmas de Programación UTN

Este repositorio contiene el **Trabajo Especial de Paradigmas de Programación**, integrando los conceptos del Paradigma Orientado a Objetos (OOP) con persistencia de datos en Base de Datos Relacional.

Este proyecto lo hice para la materia Paradigmas de programacion de ing en sistemas de la UTN frsr.

## 📋 Descripción del Proyecto

El sistema es una aplicación de consola desarrollada en Java que permite gestionar el proceso de facturación de una empresa comercial. Fue desarrollado con java y PostgreSQL

El sistema permite administrar:
* **Usuarios:** Clientes y Empleados (heredando de una clase base `Persona`).
* **Inventario:** Artículos con control de stock.
* **Ventas:** Generación de Facturas con múltiples líneas de detalle.

###  Características Principales

* **CRUD Completo:** Operaciones de Crear, Leer, Actualizar y Borrar para Clientes, Empleados y Artículos.
* **Gestión de Stock:** Descuento automático del stock al realizar una venta.
* **Transaccionalidad (ACID):** Uso de transacciones de base de datos para asegurar que no se genere una factura si no hay stock suficiente o si falla la inserción de un detalle.
* **Consistencia de Datos:** Validaciones para evitar stocks negativos o facturación de artículos inexistentes.

## 🛠️ Tecnologías y Arquitectura

El proyecto sigue una arquitectura en capas para separar responsabilidades:

* **Lenguaje:** Java.
* **Base de Datos:** PostgreSQL.
* **Gestión de Dependencias:** Maven.
* **IDE:** IntelliJ IDEA.

## ⚙️ Instalación y Configuración

Sigue estos pasos para poner en marcha el proyecto en tu entorno local.

### Prerrequisitos
* **Java JDK**: Versión 11 o superior.
* **PostgreSQL**: Instalado y con el servicio ejecutándose.
* **IntelliJ IDEA**: O cualquier IDE compatible con Maven.
* **Git**.

### Paso 1: Clonar el repositorio

### Paso 2: Configuración de la Base de Datos
1.  Abre **pgAdmin 4** (o tu cliente SQL de preferencia).
2.  Crea una nueva base de datos llamada `facturacion_db`.
3.  Abre la herramienta de consultas (Query Tool) sobre esa nueva base de datos.
4.  Ejecuta el script `schema.sql` (que se encuentra en la carpeta principal de este proyecto) para crear todas las tablas y relaciones necesarias.

### Paso 3: Configurar Credenciales
Para que la aplicación Java pueda conectarse a tu base de datos local, necesitas actualizar los datos de conexión:

1.  Abre el proyecto en IntelliJ IDEA.
2.  Navega al archivo: `src/main/java/com/sistemafact/datos/ConexionDB.java`.
3.  Busca las constantes de configuración y coloca tu contraseña real de PostgreSQL:

```java
private static final String URL = "jdbc:postgresql://localhost:5432/facturacion_db";
private static final String USER = "postgres";
private static final String PASSWORD = "TU_CONTRASEÑA_AQUI"; // <--- Editar esto
