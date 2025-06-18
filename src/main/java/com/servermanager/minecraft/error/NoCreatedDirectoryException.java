package com.servermanager.minecraft.error;

/**
 * Excepción lanzada cuando no se puede crear un directorio necesario para el servidor.
 */
public class NoCreatedDirectoryException extends RuntimeException {

    /**
     * Crea una nueva excepción con un mensaje específico.
     *
     * @param message Mensaje de error.
     */
    public NoCreatedDirectoryException(String message) {
        super(message);
    }
}
