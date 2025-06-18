package com.servermanager.minecraft.error;

/**
 * Excepción lanzada cuando se intenta iniciar un servidor que ya está en ejecución.
 */
public class ServerAlreadyRunningException extends RuntimeException {

    /**
     * Crea una nueva excepción con un mensaje personalizado.
     *
     * @param message Mensaje de error.
     */
    public ServerAlreadyRunningException(String message) {
        super(message);
    }

    /**
     * Crea una nueva excepción con el mensaje por defecto.
     */
    public ServerAlreadyRunningException() {
        super("El servidor ya está en ejecución.");
    }
}
