package com.sistemafact.vista;

import com.sistemafact.datos.*;
import com.sistemafact.logica.FacturacionService;
import com.sistemafact.modelo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static ClienteDAO clienteDAO = new ClienteDAO();
    private static EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private static ArticuloDAO articuloDAO = new ArticuloDAO();
    private static FacturacionService facturacionService = new FacturacionService();

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("--- BIENVENIDO AL SISTEMA DE FACTURACIÓN ---");

        // Bucle principal del menú
        while (true) {
            imprimirMenu();
            int opcion = -1;
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número.");
                continue;
            }

            // switch para manejar la opción del usuario
            switch (opcion) {
                case 1:
                    crearCliente();
                    break;
                case 2:
                    crearEmpleado();
                    break;
                case 3:
                    crearArticulo();
                    break;
                case 4:
                    listarArticulos();
                    break;
                case 5:
                    crearFactura();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema. ¡Adiós!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    private static void imprimirMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Crear nuevo Cliente");
        System.out.println("2. Crear nuevo Empleado");
        System.out.println("3. Crear nuevo Artículo");
        System.out.println("4. Listar todos los Artículos");
        System.out.println("5. Generar nueva Factura");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void crearCliente() {
        System.out.println("--- Nuevo Cliente ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        Cliente cliente = new Cliente(0, nombre, dni, direccion, email);
        if (clienteDAO.crearCliente(cliente) != null) {
            System.out.println("Cliente creado con ID: " + cliente.getId());
        } else {
            System.err.println("Error al crear el cliente.");
        }
    }

    private static void crearEmpleado() {
        System.out.println("--- Nuevo Empleado ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();
        System.out.print("Legajo: ");
        String legajo = scanner.nextLine();

        Empleado empleado = new Empleado(0, nombre, dni, direccion, legajo);
        if (empleadoDAO.crearEmpleado(empleado) != null) {
            System.out.println("Empleado creado con ID: " + empleado.getId());
        } else {
            System.err.println("Error al crear el empleado.");
        }
    }

    private static void crearArticulo() {
        System.out.println("--- Nuevo Artículo ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        System.out.print("Precio de Venta: ");
        double precio = Double.parseDouble(scanner.nextLine());
        System.out.print("Stock Inicial: ");
        int stock = Integer.parseInt(scanner.nextLine());

        Articulo articulo = new Articulo(0, nombre, descripcion, precio, stock);
        if (articuloDAO.crearArticulo(articulo) != null) {
            System.out.println("Artículo creado con ID: " + articulo.getId());
        } else {
            System.err.println("Error al crear el artículo.");
        }
    }

    private static void listarArticulos() {
        System.out.println("--- Listado de Artículos ---");
        List<Articulo> articulos = articuloDAO.listarArticulos();
        if (articulos.isEmpty()) {
            System.out.println("No hay artículos para mostrar.");
            return;
        }
        for (Articulo art : articulos) {
            System.out.printf("ID: %d | Nombre: %s | Precio: $%.2f | Stock: %d\n",
                    art.getId(), art.getNombre(), art.getPrecioVenta(), art.getStock());
        }
    }

    private static void crearFactura() {
        System.out.println("--- Generar Nueva Factura ---");

        try {
            // 1. Obtener Cliente
            System.out.print("Ingrese ID del Cliente: ");
            int idCliente = Integer.parseInt(scanner.nextLine());
            Cliente cliente = clienteDAO.buscarClientePorId(idCliente);
            if (cliente == null) {
                System.err.println("Error: Cliente no encontrado.");
                return;
            }

            // 2. Obtener Empleado
            System.out.print("Ingrese ID del Empleado: ");
            int idEmpleado = Integer.parseInt(scanner.nextLine());
            Empleado empleado = empleadoDAO.buscarEmpleadoPorId(idEmpleado);
            if (empleado == null) {
                System.err.println("Error: Empleado no encontrado.");
                return;
            }

            // 3. Crear lista de detalles
            List<DetalleFactura> detalles = new ArrayList<>();
            while (true) {
                System.out.println("\n-- Agregando Artículo --");
                System.out.print("Ingrese ID del Artículo (o '0' para terminar): ");
                int idArticulo = Integer.parseInt(scanner.nextLine());

                if (idArticulo == 0) {
                    if (detalles.isEmpty()) {
                        System.err.println("Una factura no puede estar vacía.");
                        continue; // Vuelve a pedir un artículo
                    }
                    break; // Termina de agregar artículos
                }

                Articulo articulo = articuloDAO.buscarArticuloPorId(idArticulo);
                if (articulo == null) {
                    System.err.println("Error: Artículo no encontrado.");
                    continue; // Vuelve a pedir un ID
                }

                System.out.print("Ingrese Cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine());

                // Creamos un objeto DetalleFactura temporal
                // El precio unitario real se validará en el Servicio
                DetalleFactura detalle = new DetalleFactura();
                detalle.setArticulo(articulo);
                                detalle.setCantidad(cantidad);

                detalles.add(detalle);
                System.out.println("Artículo '" + articulo.getNombre() + "' agregado.");
            }

            // 4. Llamar la logica
            System.out.println("\nProcesando factura...");
            Factura nuevaFactura = facturacionService.generarFactura(cliente, empleado, detalles);

            System.out.println("------------------------------------");
            System.out.println("¡FACTURA GENERADA EXITOSAMENTE!");
            System.out.println("Factura N°: " + nuevaFactura.getId());
            System.out.println("Cliente: " + nuevaFactura.getCliente().getNombre());
            System.out.println("Total: $" + nuevaFactura.getTotal());
            System.out.println("------------------------------------");

        } catch (NumberFormatException e) {
            System.err.println("Error: Entrada numérica no válida.");
        } catch (Exception e) {
            // Captura los errores de lógica de negocio
            System.err.println("\n--- ERROR AL GENERAR LA FACTURA ---");
            System.err.println("Error: " + e.getMessage());
        }
    }
}