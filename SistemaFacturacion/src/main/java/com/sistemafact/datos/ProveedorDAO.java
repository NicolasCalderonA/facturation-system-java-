package com.sistemafact.datos;

import com.sistemafact.modelo.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    /**
     * Inserta un nuevo proveedor.
     * Maneja la transacción para 'persona' y 'proveedor'.
     */
    public Proveedor crearProveedor(Proveedor proveedor) {
        String sqlPersona = "INSERT INTO persona (nombre, dni, direccion) VALUES (?, ?, ?) RETURNING id";
        String sqlProveedor = "INSERT INTO proveedor (id_persona, cuit, telefono) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement psPersona = null;
        PreparedStatement psProveedor = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciar Transacción

            // 1. Insertar en 'persona'
            psPersona = conn.prepareStatement(sqlPersona);
            psPersona.setString(1, proveedor.getNombre());
            psPersona.setString(2, proveedor.getDni());
            psPersona.setString(3, proveedor.getDireccion());

            rs = psPersona.executeQuery();

            int idPersonaGenerado = -1;
            if (rs.next()) {
                idPersonaGenerado = rs.getInt(1);
                proveedor.setId(idPersonaGenerado);
            } else {
                throw new SQLException("Fallo al insertar en persona, no se obtuvo ID.");
            }

            // 2. Insertar en 'proveedor'
            psProveedor = conn.prepareStatement(sqlProveedor);
            psProveedor.setInt(1, idPersonaGenerado);
            psProveedor.setString(2, proveedor.getCuit());
            psProveedor.setString(3, proveedor.getTelefono());
            psProveedor.executeUpdate();

            conn.commit(); // Confirmar transacción
            return proveedor;

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (psPersona != null) psPersona.close();
                if (psProveedor != null) psProveedor.close();
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
     * Busca un proveedor por su ID.
     * Usa JOIN para unir 'persona' y 'proveedor'.
     */
    public Proveedor buscarProveedorPorId(int id) {
        String sql = "SELECT p.id, p.nombre, p.dni, p.direccion, pr.cuit, pr.telefono " +
                "FROM persona p " +
                "INNER JOIN proveedor pr ON p.id = pr.id_persona " +
                "WHERE p.id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Proveedor proveedor = null;

        try {
            conn = ConexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                proveedor = new Proveedor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("direccion"),
                        rs.getString("cuit"),
                        rs.getString("telefono")
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
        return proveedor;
    }

    /**
     * Obtiene una lista de todos los proveedores.
     */
    public List<Proveedor> listarProveedores() {
        String sql = "SELECT p.id, p.nombre, p.dni, p.direccion, pr.cuit, pr.telefono " +
                "FROM persona p " +
                "INNER JOIN proveedor pr ON p.id = pr.id_persona";

        List<Proveedor> proveedores = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("direccion"),
                        rs.getString("cuit"),
                        rs.getString("telefono")
                );
                proveedores.add(proveedor);
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
        return proveedores;
    }
}