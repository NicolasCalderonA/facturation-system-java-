package com.sistemafact.datos;

import com.sistemafact.modelo.Articulo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDAO {

    /**
     * Inserta un nuevo artículo en la base de datos.
     */
    public Articulo crearArticulo(Articulo articulo) {
        String sql = "INSERT INTO articulo (nombre, descripcion, precio_venta, stock) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionDB.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, articulo.getNombre());
            ps.setString(2, articulo.getDescripcion());
            ps.setDouble(3, articulo.getPrecioVenta());
            ps.setInt(4, articulo.getStock());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                articulo.setId(rs.getInt(1));
            }
            rs.close();

            return articulo;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Busca un artículo por su ID.
     * Este método será VITAL para la facturación.
     */
    public Articulo buscarArticuloPorId(int id) {
        String sql = "SELECT * FROM articulo WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Articulo articulo = null;

        try {
            conn = ConexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                articulo = new Articulo(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getInt("stock")
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
        return articulo;
    }

    /**
     * Obtiene una lista de todos los artículos.
     */
    public List<Articulo> listarArticulos() {
        String sql = "SELECT * FROM articulo";
        List<Articulo> articulos = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Articulo articulo = new Articulo(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getInt("stock")
                );
                articulos.add(articulo);
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
        return articulos;
    }

    /**
     * Actualiza los datos de un artículo (nombre, descripción, precio).
     * NO actualiza el stock, para eso usamos un método separado.
     */
    public boolean actualizarArticulo(Articulo articulo) {
        String sql = "UPDATE articulo SET nombre = ?, descripcion = ?, precio_venta = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, articulo.getNombre());
            ps.setString(2, articulo.getDescripcion());
            ps.setDouble(3, articulo.getPrecioVenta());
            ps.setInt(4, articulo.getId());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Actualiza *solo* el stock de un artículo.
     * Este método debe usarse dentro de una transacción más grande (la factura o la compra).
     * Nota: La conexión se pasa como parámetro para que pueda ser parte
     * de una transacción externa (manejada por la capa de lógica/servicio).
     */
    public boolean actualizarStock(Connection conn, int idArticulo, int nuevoStock) throws SQLException {
        if (nuevoStock < 0) {
            throw new SQLException("El stock no puede ser negativo.");
        }

        String sql = "UPDATE articulo SET stock = ? WHERE id = ?";
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, nuevoStock);
            ps.setInt(2, idArticulo);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            throw e;
        } finally {

            if (ps != null) ps.close();
        }
    }

    /**
     * Elimina un artículo por su ID.
     */
    public boolean eliminarArticulo(int id) {
        String sql = "DELETE FROM articulo WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}