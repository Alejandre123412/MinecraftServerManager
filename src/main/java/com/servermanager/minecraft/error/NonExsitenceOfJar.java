package com.servermanager.minecraft.error;

/**
 * Excepción lanzada cuando no se encuentra el archivo JAR necesario para iniciar el servidor.
 */
public class NonExsitenceOfJar extends Throwable {

    /**
     * Crea una nueva excepción con un mensaje específico.
     *
     * @param message Mensaje de error.
     */
    public NonExsitenceOfJar(String message) {
        super(message);
    }
}
