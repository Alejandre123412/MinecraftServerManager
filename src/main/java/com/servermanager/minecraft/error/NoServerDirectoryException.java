package com.servermanager.minecraft.error;

/**
 * Excepción lanzada cuando el directorio del servidor no existe.
 */
public class NoServerDirectoryException extends RuntimeException {

    /**
     * Crea una nueva excepción con un mensaje personalizado.
     *
     * @param message Mensaje de error.
     */
    public NoServerDirectoryException(String message) {
        super(message);
    }

    /**
     * Crea una nueva excepción con un mensaje por defecto.
     */
    public NoServerDirectoryException() {
        super("El directorio de este servidor no existe");
    }
}
