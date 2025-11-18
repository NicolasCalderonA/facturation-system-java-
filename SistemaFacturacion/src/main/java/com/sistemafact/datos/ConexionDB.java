package com.sistemafact.datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    // --- Configuración de la Conexión ---
    // 1. URL de la base de datos (¡asegúrate que el nombre "facturacion_db" sea correcto!)
    private static final String URL = "jdbc:postgresql://localhost:5432/facturacion_db";

    // 2. Usuario de la base de datos (normalmente "postgres")
    private static final String USER = "postgres";

    // 3. Contraseña que definiste durante la instalación de PostgreSQL
    // !! ¡¡CAMBIA ESTO POR TU CONTRASEÑA REAL!!
    private static final String PASSWORD = "1234";
    // ------------------------------------


    // Método público y estático para obtener una conexión
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // No es necesario Class.forName() con los drivers JDBC modernos,
            // pero si tuvieras problemas, puedes descomentar la siguiente línea:
            // Class.forName("org.postgresql.Driver");

            // Intenta establecer la conexión
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // System.out.println("¡Conexión exitosa a PostgreSQL!"); // Opcional: para debug

        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos:");
            e.printStackTrace();
        }
        return connection;
    }

    // --- Método de prueba (main) ---
    // Este método 'main' es solo para probar que la clase funciona.
    // No es parte de la aplicación final, pero es súper útil ahora.
    public static void main(String[] args) {
        Connection conn = ConexionDB.getConnection();

        if (conn != null) {
            System.out.println("¡¡PRUEBA EXITOSA: Conexión a 'facturacion_db' establecida!!");
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("!! PRUEBA FALLIDA: No se pudo establecer la conexión.");
        }
    }
}