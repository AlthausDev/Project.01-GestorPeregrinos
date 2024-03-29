package com.althaus.dev.GestorPeregrinos.view;

import com.althaus.dev.GestorPeregrinos.model.Parada;
import com.althaus.dev.GestorPeregrinos.model.Peregrino;
import com.althaus.dev.GestorPeregrinos.service.CredencialesService;
import com.althaus.dev.GestorPeregrinos.service.ValidationService;
import com.althaus.dev.GestorPeregrinos.util.io.XMLReader;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Clase que proporciona métodos para manejar la interfaz de usuario relacionada con los peregrinos.
 *
 * <p>
 * Esta clase contiene métodos para recopilar información necesaria para agregar un nuevo peregrino,
 * mostrar detalles de un peregrino y obtener información como nombre, contraseña y nacionalidad del usuario.
 * </p>
 *
 * <p>
 * Se utiliza la anotación {@code Component} para que Spring la reconozca como un componente de la aplicación.
 * </p>
 *
 * @author Althaus_Dev
 * @since 2024-01-12
 */
@Component
public class PeregrinoView {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ValidationService validationService = new ValidationService();
    private final HashMap<String, String> nacionalidades = XMLReader.readPaises();

    /**
     * Método para recopilar la información necesaria para agregar un nuevo peregrino.
     * Solicita el nombre, la contraseña, la parada asociada y la nacionalidad del peregrino.
     *
     * @param parada La parada asociada al peregrino.
     * @return Un HashMap que contiene la información recopilada para el nuevo peregrino.
     * Devuelve null si el usuario cancela el proceso de registro.
     * @throws RuntimeException Si ocurre un error durante la ejecución.
     */
    public HashMap<String, Object> agregarPeregrino(Parada parada, CredencialesService credencialService) {
        HashMap<String, Object> peregrinoData = new HashMap<>();

        try {
            System.out.println("Registrar nuevo usuario - Peregrino");
            peregrinoData.put("nombre", obtenerNombre(credencialService));
            peregrinoData.put("password", obtenerPassword());
            peregrinoData.put("parada", parada);
            peregrinoData.put("nacionalidad", obtenerNacionalidad());

            System.out.println("Datos Introducidos:"
                    + "\nNombre del nuevo peregrino: " + peregrinoData.get("nombre")
                    + "\nNacionalidad: " + peregrinoData.get("nacionalidad")
                    + "\n" + peregrinoData.get("parada").toString());

            if (!isCorrecto()) {
                System.out.println("Saliendo del formulario de registro.");
                return null;
            }
            return peregrinoData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Método para mostrar los detalles de un peregrino.
     *
     * @param peregrino El peregrino cuyos detalles se mostrarán.
     */
    public void mostrarDetallesPeregrino(Peregrino peregrino) {
        System.out.println("Datos del peregrino:");
        System.out.println("ID: " + peregrino.getId());
        System.out.println("Nombre: " + peregrino.getName());
        System.out.println("Nacionalidad: " + peregrino.getNacionalidad());
    }

    /**
     * Método para obtener la nacionalidad del usuario, mostrando previamente una lista de códigos de países.
     *
     * @return El código de la nacionalidad seleccionada por el usuario.
     */
    private String obtenerNacionalidad() {
        System.out.println("CODIGO - PAIS");
        nacionalidades.forEach((k, v) ->
                System.out.println(k + " - " + v));

        System.out.println("Inserte el código de su país (2 caracteres): ");

        return validationService.validarCodigoNacionalidad(scanner, nacionalidades);
    }

    /**
     * Método para obtener el nombre del usuario.
     *
     * @return El nombre ingresado por el usuario.
     */
    private String obtenerNombre(CredencialesService credencialService) {
        boolean nombreCorrecto = false;
        String nombre = null;

        while (!nombreCorrecto) {
            System.out.println("Indique un nombre (mínimo 3 caracteres): ");
            nombre = scanner.nextLine().toLowerCase();
            nombreCorrecto = validationService.validarFormatoNombreUsuario(nombre) && validationService.existeUsuario(nombre, credencialService);
        }
        return nombre;
    }

    /**
     * Método para obtener la contraseña del usuario.
     *
     * @return La contraseña ingresada por el usuario.
     */
    private String obtenerPassword() {
        while (true) {
            System.out.println("Indique una contraseña (mínimo 3 caracteres): ");
            String pass = scanner.nextLine();

            if (pass.length() >= 3) {
                return pass;
            } else {
                System.err.println("La contraseña debe tener al menos 3 caracteres. Vuelva a introducirla.");
            }
        }
    }

    /**
     * Método para confirmar si los datos ingresados son correctos.
     *
     * @return true si los datos son correctos, false si el usuario elige cancelar el proceso de registro.
     */

    private boolean isCorrecto() {
        char valido;
        do {
            System.out.println("¿Son los datos introducidos son correctos? S/N");
            valido = scanner.nextLine().toUpperCase().charAt(0);
        } while (valido != 'S' && valido != 'N');
        return valido == 'S';
    }
}
