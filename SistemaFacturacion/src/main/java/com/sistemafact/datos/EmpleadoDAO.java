package com.sistemafact.datos;

import com.sistemafact.modelo.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    /**
     * Inserta un nuevo empleado.
     * Maneja la transacción para 'persona' y 'empleado'.
     */
    public Empleado crearEmpleado(Empleado empleado) {
        String sqlPersona = "INSERT INTO persona (nombre, dni, direccion) VALUES (?, ?, ?) RETURNING id";
        String sqlEmpleado = "INSERT INTO empleado (id_persona, legajo) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement psPersona = null;
        PreparedStatement psEmpleado = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciar Transacción

            // 1. Insertar en 'persona'
            psPersona = conn.prepareStatement(sqlPersona);
            psPersona.setString(1, empleado.getNombre());
            psPersona.setString(2, empleado.getDni());
            psPersona.setString(3, empleado.getDireccion());

            rs = psPersona.executeQuery();

            int idPersonaGenerado = -1;
            if (rs.next()) {
                idPersonaGenerado = rs.getInt(1);
                empleado.setId(idPersonaGenerado);
            } else {
                throw new SQLException("Fallo al insertar en persona, no se obtuvo ID.");
            }

            // 2. Insertar en 'empleado'
            psEmpleado = conn.prepareStatement(sqlEmpleado);
            psEmpleado.setInt(1, idPersonaGenerado);
            psEmpleado.setString(2, empleado.getLegajo());
            psEmpleado.executeUpdate();

            conn.commit(); // Confirmar transacción
            return empleado;

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (psPersona != null) psPersona.close();
                if (psEmpleado != null) psEmpleado.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Busca un empleado por su ID.
     * Usa JOIN para unir 'persona' y 'empleado'.
     */
    public Empleado buscarEmpleadoPorId(int id) {
        String sql = "SELECT p.id, p.nombre, p.dni, p.direccion, e.legajo " +
                "FROM persona p " +
                "INNER JOIN empleado e ON p.id = e.id_persona " +
                "WHERE p.id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Empleado empleado = null;

        try {
            conn = ConexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                empleado = new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("direccion"),
                        rs.getString("legajo")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return empleado;
    }

    /**
     * Obtiene una lista de todos los empleados.
     */
    public List<Empleado> listarEmpleados() {
        String sql = "SELECT p.id, p.nombre, p.dni, p.direccion, e.legajo " +
                "FROM persona p " +
                "INNER JOIN empleado e ON p.id = e.id_persona";

        List<Empleado> empleados = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Empleado empleado = new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("direccion"),
                        rs.getString("legajo")
                );
                empleados.add(empleado);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return empleados;
    }


}