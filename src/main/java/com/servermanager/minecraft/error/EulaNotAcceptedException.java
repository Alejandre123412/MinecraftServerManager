package com.servermanager.minecraft.error;

/**
 * Excepción lanzada cuando el usuario intenta iniciar el servidor sin aceptar el EULA.
 */
public class EulaNotAcceptedException extends RuntimeException {

    /**
     * Crea una nueva excepción con un mensaje personalizado.
     *
     * @param message Mensaje de error.
     */
    public EulaNotAcceptedException(String message) {
        super(message);
    }

    /**
     * Crea una nueva excepción con el mensaje por defecto.
     */
    public EulaNotAcceptedException() {
        super("Debes aceptar el EULA para iniciar el servidor.");
    }
}
