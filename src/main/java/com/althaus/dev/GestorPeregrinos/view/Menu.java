package com.althaus.dev.GestorPeregrinos.view;

import com.althaus.dev.GestorPeregrinos.app.UserSession;
import com.althaus.dev.GestorPeregrinos.controller.LoginController;
import com.althaus.dev.GestorPeregrinos.controller.ParadaController;
import com.althaus.dev.GestorPeregrinos.controller.PeregrinoController;
import com.althaus.dev.GestorPeregrinos.model.Parada;
import com.althaus.dev.GestorPeregrinos.model.Peregrino;
import com.althaus.dev.GestorPeregrinos.model.Perfil;
import com.althaus.dev.GestorPeregrinos.service.PeregrinoService;
import com.althaus.dev.GestorPeregrinos.service.ValidationService;
import com.althaus.dev.GestorPeregrinos.util.io.XMLWriter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * La clase `Menu` se encarga de gestionar los menús y las opciones disponibles para diferentes perfiles de usuario.
 * Dependiendo del perfil de usuario, se muestra un menú específico con opciones personalizadas.
 *
 * @author S.Althaus
 */
public class Menu {
    Scanner sc = new Scanner(System.in);
    private UserSession userSession;
    private LoginController loginController;
    private ParadaController paradaController;
    private PeregrinoController peregrinoController;
    private final ValidationService validationService = new ValidationService();


    /**
     * Constructor de la clase `Menu` que muestra el menú correspondiente al perfil de usuario.
     *
     * @param userSession La instancia de UserSession para realizar operaciones relacionadas con la sesión del usuario.
     */
    public Menu(UserSession userSession) {
        this.userSession = userSession;
        Perfil perfil = userSession.getPerfil();

            switch (perfil) {
                case PEREGRINO:
                    menuPeregrino();
                    break;
                case ADMIN_PARADA:
                    menuAdminParada();
                    break;
                case ADMIN_GENERAL:
                    menuAdmin();
                    break;
                case INVITADO:
                default:
                    menuInvitado();
                    break;

        }
    }

    /**
     * Menú para usuarios invitados (perfil INVITADO).
     * Permite iniciar sesión, crear un nuevo usuario o salir del programa.
     */
    private void menuInvitado() {
        int opcion = -1;
        do {
            System.out.println("Bienvenido al sistema de Gestión de peregrinos!");
            System.out.println("Menu:");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Crear nuevo usuario");
            System.out.println("0. Salir");

            opcion = obtenerOpcionUsuario();
            switch (opcion) {
                case 0:
                    System.out.println("Saliendo del programa.");
                    salir();
                    break;
                case 1:
                    login.login();
                    opcion = 0;
                    break;
                case 2:
                    peregrinoController.nuevoPeregrino();
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida." + "\n");
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Menú para peregrinos (perfil PEREGRINO).
     * Permite visualizar y exportar datos del carnet, así como cerrar sesión o salir del programa.
     */
    protected void menuPeregrino() {
        int opcion = -1;
        do {
            System.out.println("Menu Peregrino:");
            System.out.println("1. Visualizar y exportar datos del carnet");
            System.out.println("2. Cerrar Sesion");
            System.out.println("0. Salir");

            opcion = obtenerOpcionUsuario();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo del programa.");
                    salir();
                    break;
                case 1:
                    /////implementar
                    System.out.println((Peregrino)userSession.getUsuario().getCarnet().toString());
                    break;
                case 2:
                    userSession.cerrarSesion();
                    opcion = 0;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida." + "\n");
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Menú para administradores de parada (perfil ADMIN_PARADA).
     * Permite visualizar, exportar datos de parada, cerrar sesión y salir del programa.
     */

    protected void menuAdminParada() {
        int opcion = -1;
        do {
            System.out.println("Menu Administrador de Parada:");
            System.out.println("1. Visualizar datos de parada");
            System.out.println("2. Exportar datos de parada");
            System.out.println("3. Recibir peregrino en parada");
            System.out.println("4. Cerrar Sesion");
            System.out.println("0. Salir");

            opcion = obtenerOpcionUsuario();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo del programa.");
                    salir();
                    break;
                case 1:
                    mostrarDatosParadaActual();
                    break;
                case 2:
                    menuExportarEstanciasFechas();
                    break;
                case 3:
                    menuRecibirPeregrinoEnParada();
                    break;
                case 4:
                    userSession.cerrarSesion();
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida." + "\n");
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Menú para administradores generales (perfil ADMIN_GENERAL).
     * Permite registrar una nueva parada, cerrar sesión o salir del programa.
     */
    protected void menuAdmin() {
        int opcion = -1;
        do {
            System.out.println("Menu Administrador:");
            System.out.println("1. Registrar nueva parada");
            System.out.println("2. Cerrar Sesion");
            System.out.println("0. Salir");

            opcion = obtenerOpcionUsuario();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo del programa.");
                    salir();
                    break;
                case 1:
                    paradaController.nuevaParada();
                    break;
                case 2:
                    userSession.cerrarSesion();
                    opcion = 0;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida." + "\n");
                    break;
            }
        } while (opcion != 0);
    }
    private void salir(){
        userSession.setContinuar(false);
    }

    private int obtenerOpcionUsuario() {
        try {
            return sc.nextInt();
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("Entrada no válida. Por favor, introduzca un número." + "\n");
            return -1;
        }
    }


    private void menuExportarEstanciasFechas() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Exportar Estancias en un rango de fechas:");


        System.out.println("Introduzca la fecha de inicio (YYYY-MM-DD): ");
        LocalDate fechaInicio = validarFormatoFecha(sc);
        LocalDate fechaFin = null;

        do {
            System.out.println("Introduzca la fecha de fin (YYYY-MM-DD): ");
            fechaFin = validarFormatoFecha(sc);
            if (fechaFin.isBefore(fechaInicio)) {
                System.err.println("Error: La fecha de fin debe ser mayor o igual a la fecha de inicio.");
            }
        } while (fechaFin.isBefore(fechaInicio));

        ExportarEstanciasFechas.exportarEstancias(userSession.getUsuario().getId(), fechaInicio, fechaFin);
    }



    private void menuRecibirPeregrinoEnParada() {

        System.out.println("Recibir peregrino en parada:");
        Parada paradaActual = Sesion.getParadaActual();

        if (paradaActual == null) {
            System.out.println("Error: No se ha seleccionado una parada válida.");
            return;
        }

        System.out.println("Información de la parada:");
        System.out.println("ID: " + paradaActual.getId());
        System.out.println("Nombre: " + paradaActual.getNombre());
        System.out.println("Región: " + paradaActual.getRegion());

        Peregrino peregrino;
        do {
            System.out.println("Ingrese el identificador del peregrino:");
            long idPeregrino = sc.nextLong();
            sc.nextLine();

            PeregrinoDAOImpl peregrinoDAO = new PeregrinoDAOImpl();
            peregrino = peregrinoDAO.read(idPeregrino);

            if (peregrino == null) {
                System.out.println("No se encontró un peregrino con ese identificador. Vuelva a introducir su ID");
            }
        } while (peregrino == null);

        System.out.println("Datos del peregrino:");
        System.out.println("ID: " + peregrino.getId());
        System.out.println("Nombre: " + peregrino.getNombre());
        System.out.println("Nacionalidad: " + peregrino.getNacionalidad());

        System.out.println("¿Desea sellar el carnet del peregrino? (S/N):");
        String confirmacion = sc.nextLine().trim();

        if (confirmacion.equalsIgnoreCase("S")) {
            sellarCarnet(peregrino, paradaActual);
        } else {
            System.out.println("Operación cancelada. No se ha sellado el carnet.");
        }
    }

    private void mostrarDatosParadaActual() {
       System.out.println(Sesion.getParadaActual().toString());
    }
}