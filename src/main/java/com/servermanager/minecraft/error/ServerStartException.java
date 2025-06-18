package com.servermanager.minecraft.error;

/**
 * Excepción lanzada cuando ocurre un error grave al intentar iniciar el servidor de Minecraft.
 * <p>
 * Esta excepción puede envolver otras excepciones relacionadas con fallos
 * durante el arranque del servidor, como errores de configuración, fallos de I/O,
 * o errores al ejecutar el proceso del servidor.
 */
public class ServerStartException extends RuntimeException {

    /**
     * Crea una nueva excepción indicando un mensaje de error.
     *
     * @param message Mensaje descriptivo del error ocurrido.
     */
    public ServerStartException(String message) {
        super(message);
    }

    /**
     * Crea una nueva excepción con un mensaje y una causa específica.
     *
     * @param message Mensaje descriptivo del error ocurrido.
     * @param cause   La causa subyacente que provocó esta excepción.
     */
    public ServerStartException(String message, Throwable cause) {
        super(message, cause);
    }
}
