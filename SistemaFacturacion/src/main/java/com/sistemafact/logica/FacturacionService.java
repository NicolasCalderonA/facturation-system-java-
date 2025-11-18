package com.sistemafact.logica;

import com.sistemafact.datos.ArticuloDAO;
import com.sistemafact.datos.FacturaDAO;
import com.sistemafact.datos.ConexionDB;
import com.sistemafact.modelo.Articulo;
import com.sistemafact.modelo.Cliente;
import com.sistemafact.modelo.DetalleFactura;
import com.sistemafact.modelo.Empleado;
import com.sistemafact.modelo.Factura;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class FacturacionService {

    private ArticuloDAO articuloDAO;
    private FacturaDAO facturaDAO;

    public FacturacionService() {
        this.articuloDAO = new ArticuloDAO();
        this.facturaDAO = new FacturaDAO();
    }

    /**
     * Lógica de negocio principal: Generar una nueva factura.
     * Esta función maneja la transacción de la base de datos.
     *
     * @param cliente El cliente que compra.
     * @param empleado El empleado que vende.
     * @param detalles La lista de artículos y cantidades que se compran.
     * @return La Factura creada.
     * @throws Exception Si la lógica de negocio falla (ej. sin stock).
     */
    public Factura generarFactura(Cliente cliente, Empleado empleado, List<DetalleFactura> detalles) throws Exception {

        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); //


            double totalFactura = 0.0;

            for (DetalleFactura detalle : detalles) {
                Articulo articulo = articuloDAO.buscarArticuloPorId(detalle.getArticulo().getId());

                // Regla 1: El artículo debe existir
                if (articulo == null) {
                    throw new Exception("Error: El artículo con ID " + detalle.getArticulo().getId() + " no existe.");
                }

                // Regla 2: Debe haber stock suficiente
                if (articulo.getStock() < detalle.getCantidad()) {
                    throw new Exception("Error: No hay stock suficiente para '" + articulo.getNombre() +
                            "'. Stock actual: " + articulo.getStock() +
                            ", Solicitado: " + detalle.getCantidad());
                }

                detalle.setPrecioUnitario(articulo.getPrecioVenta());
                totalFactura += detalle.getPrecioUnitario() * detalle.getCantidad();
            }

            // --- 3. EJECUTAR LAS OPERACIONES DE BD ---

            // a. Crear el objeto Factura (cabecera)
            Factura factura = new Factura();
            factura.setCliente(cliente);
            factura.setEmpleado(empleado);
            factura.setFecha(LocalDate.now());
            factura.setDetalles(detalles);
            factura.setTotal(totalFactura);

            // b. Guardar la factura y sus detalles
            facturaDAO.guardarFactura(conn, factura);

            // c. Actualizar el stock de cada artículo
            for (DetalleFactura detalle : detalles) {
                Articulo articulo = articuloDAO.buscarArticuloPorId(detalle.getArticulo().getId());
                int nuevoStock = articulo.getStock() - detalle.getCantidad();

                // Este método fue diseñado para usar una conexión existente
                articuloDAO.actualizarStock(conn, articulo.getId(), nuevoStock);
            }

            conn.commit();

            System.out.println("Factura N°" + factura.getId() + " generada exitosamente.");
            return factura;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    System.err.println("Error en la transacción, aplicando ROLLBACK.");
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new Exception("Fallo al generar la factura: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Aca agregar la lógica para 'registrarCompra' (aumento de stock)
}