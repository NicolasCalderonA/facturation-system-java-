package com.sistemafact.datos;

import com.sistemafact.modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    /**
     * Inserta un nuevo cliente en la base de datos.
     * MANEJA HERENCIA: Inserta primero en 'persona' y luego en 'cliente'.
     * Esto se hace dentro de una "Transacción" para asegurar que
     * se completen ambas operaciones o ninguna.
     */
    public Cliente crearCliente(Cliente cliente) {
        String sqlPersona = "INSERT INTO persona (nombre, dni, direccion) VALUES (?, ?, ?) RETURNING id";
        String sqlCliente = "INSERT INTO cliente (id_persona, email) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement psPersona = null;
        PreparedStatement psCliente = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false);

            psPersona = conn.prepareStatement(sqlPersona);
            psPersona.setString(1, cliente.getNombre());
            psPersona.setString(2, cliente.getDni());
            psPersona.setString(3, cliente.getDireccion());

            rs = psPersona.executeQuery(); // Usamos executeQuery por el "RETURNING id"

            int idPersonaGenerado = -1;
            if (rs.next()) {
                idPersonaGenerado = rs.getInt(1);
                cliente.setId(idPersonaGenerado); // Actualizamos el ID en el objeto
            } else {
                throw new SQLException("Fallo al insertar en persona, no se obtuvo ID.");
            }

            psCliente = conn.prepareStatement(sqlCliente);
            psCliente.setInt(1, idPersonaGenerado);
            psCliente.setString(2, cliente.getEmail());
            psCliente.executeUpdate();

            conn.commit();

            return cliente;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (psPersona != null) psPersona.close();
                if (psCliente != null) psCliente.close();
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
     * Busca un cliente por su ID.
     * MANEJA HERENCIA: Usa un JOIN para unir 'persona' y 'cliente'.
     */
    public Cliente buscarClientePorId(int id) {
        String sql = "SELECT p.id, p.nombre, p.dni, p.direccion, c.email " +
                "FROM persona p " +
                "INNER JOIN cliente c ON p.id = c.id_persona " +
                "WHERE p.id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Cliente cliente = null;

        try {
            conn = ConexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("direccion"),
                        rs.getString("email")
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
        return cliente;
    }

    /**
     * Obtiene una lista de todos los clientes.
     */
    public List<Cliente> listarClientes() {
        String sql = "SELECT p.id, p.nombre, p.dni, p.direccion, c.email " +
                "FROM persona p " +
                "INNER JOIN cliente c ON p.id = c.id_persona";

        List<Cliente> clientes = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("direccion"),
                        rs.getString("email")
                );
                clientes.add(cliente);
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
        return clientes;
    }

    /**
     * Actualiza los datos de un cliente.
     * MANEJA HERENCIA: Actualiza ambas tablas 'persona' y 'cliente'.
     */
    public boolean actualizarCliente(Cliente cliente) {
        String sqlPersona = "UPDATE persona SET nombre = ?, dni = ?, direccion = ? WHERE id = ?";
        String sqlCliente = "UPDATE cliente SET email = ? WHERE id_persona = ?";

        Connection conn = null;
        PreparedStatement psPersona = null;
        PreparedStatement psCliente = null;

        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false);

            psPersona = conn.prepareStatement(sqlPersona);
            psPersona.setString(1, cliente.getNombre());
            psPersona.setString(2, cliente.getDni());
            psPersona.setString(3, cliente.getDireccion());
            psPersona.setInt(4, cliente.getId());
            psPersona.executeUpdate();

            psCliente = conn.prepareStatement(sqlCliente);
            psCliente.setString(1, cliente.getEmail());
            psCliente.setInt(2, cliente.getId());
            psCliente.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (psPersona != null) psPersona.close();
                if (psCliente != null) psCliente.close();
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
     * Elimina un cliente por su ID.
     * MANEJA HERENCIA: PostgreSQL se encarga gracias al "REFERENCES"
     * Pero es más seguro borrar primero el hijo y luego el padre.
     */
    public boolean eliminarCliente(int id) {
        String sqlCliente = "DELETE FROM cliente WHERE id_persona = ?";
        String sqlPersona = "DELETE FROM persona WHERE id = ?";

        Connection conn = null;
        PreparedStatement psCliente = null;
        PreparedStatement psPersona = null;

        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false);

            psCliente = conn.prepareStatement(sqlCliente);
            psCliente.setInt(1, id);
            psCliente.executeUpdate();

            psPersona = conn.prepareStatement(sqlPersona);
            psPersona.setInt(1, id);
            psPersona.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {

            try {
                if (psCliente != null) psCliente.close();
                if (psPersona != null) psPersona.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}