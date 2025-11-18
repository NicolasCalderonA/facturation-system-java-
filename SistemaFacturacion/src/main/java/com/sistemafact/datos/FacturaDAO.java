package com.sistemafact.datos;

import com.sistemafact.modelo.Factura;
import com.sistemafact.modelo.DetalleFactura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

public class FacturaDAO {

    /**
     * Guarda una factura (cabecera y detalles) en la base de datos.
     * Este método está diseñado para ser llamado DESDE DENTRO de una transacción
     * que será manejada por la capa de Lógica/Servicio.
     *
     * @param conn La conexión de base de datos (con la transacción ya iniciada)
     * @param factura El objeto Factura que se va a guardar.
     * @return El objeto Factura actualizado con el ID generado por la BD.
     * @throws SQLException Si ocurre un error de SQL, se lanza para que
     * la capa de servicio pueda hacer un rollback.
     */
    public Factura guardarFactura(Connection conn, Factura factura) throws SQLException {

        String sqlFactura = "INSERT INTO factura (fecha, id_cliente, id_empleado, total) VALUES (?, ?, ?, ?) RETURNING id";
        String sqlDetalle = "INSERT INTO detalle_factura (id_factura, id_articulo, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        PreparedStatement psFactura = null;
        PreparedStatement psDetalle = null;
        ResultSet rs = null;

        try {
            psFactura = conn.prepareStatement(sqlFactura);
            psFactura.setDate(1, Date.valueOf(factura.getFecha()));
            psFactura.setInt(2, factura.getCliente().getId());
            psFactura.setInt(3, factura.getEmpleado().getId());
            psFactura.setDouble(4, factura.getTotal());

            rs = psFactura.executeQuery();

            int idFacturaGenerado;
            if (rs.next()) {
                idFacturaGenerado = rs.getInt(1);
                factura.setId(idFacturaGenerado);
            } else {
                throw new SQLException("Fallo al insertar factura, no se obtuvo ID.");
            }

            psDetalle = conn.prepareStatement(sqlDetalle);

            for (DetalleFactura detalle : factura.getDetalles()) {
                psDetalle.setInt(1, idFacturaGenerado);
                psDetalle.setInt(2, detalle.getArticulo().getId());
                psDetalle.setInt(3, detalle.getCantidad());
                psDetalle.setDouble(4, detalle.getPrecioUnitario());

                psDetalle.addBatch();
            }

            psDetalle.executeBatch();

            return factura;

        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (psFactura != null) psFactura.close();
            if (psDetalle != null) psDetalle.close();
        }
    }
}