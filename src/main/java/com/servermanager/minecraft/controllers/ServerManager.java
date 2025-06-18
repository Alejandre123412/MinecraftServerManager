package com.servermanager.minecraft.controllers;

import com.servermanager.minecraft.models.Server;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/**
 * Clase utilitaria para gestionar servidores Minecraft activos en ejecución.
 *
 * <p>Se encarga de agregar, quitar, detener y consultar el estado de servidores
 * en tiempo de ejecución. Cada servidor es identificado por su nombre único.</p>
 *
 * <p>Esta clase mantiene un {@link Map} interno de servidores activos,
 * y permite acceder a ellos de forma centralizada durante toda la aplicación.</p>
 */
public class ServerManager {
    /**
     * Mapa de servidores actualmente activos, indexados por nombre.
     */
    private static final Map<String, Server> activeServers = new HashMap<>();

    /**
     * Devuelve el servidor asociado a un nombre dado.
     *
     * @param name nombre del servidor.
     * @return instancia del servidor si existe; {@code null} si no está registrado.
     */
    public static Server getServer(String name) {
        return activeServers.get(name);
    }
    /**
     * Agrega un servidor al mapa de servidores activos.
     *
     * @param server el servidor a agregar.
     */
    public static void addServer(Server server) {
        activeServers.put(server.getName(), server);
    }
    /**
     * Elimina un servidor por su nombre. Si está corriendo, se detiene automáticamente.
     *
     * @param name nombre del servidor a eliminar.
     */
    public static void removeServer(String name) {
        Server server = activeServers.remove(name);
        if (server != null && server.isRunning()) {
            server.stop();
        }
    }
    /**
     * Detiene todos los servidores que estén en ejecución y limpia el mapa.
     */
    public static void stopAllServers() {
        for (Server server : activeServers.values()) {
            if (server.isRunning()) {
                server.stop();
            }
        }
        activeServers.clear();
    }
    /**
     * Verifica si un servidor específico está en ejecución.
     *
     * @param name nombre del servidor.
     * @return {@code true} si está corriendo; {@code false} si no existe o está detenido.
     */
    public static boolean isServerRunning(String name) {
        Server server = activeServers.get(name);
        return server != null && server.isRunning();
    }
    /**
     * Devuelve una colección inmutable con todos los servidores activos.
     *
     * @return colección de servidores activos.
     */
    public static Collection<Server> getAllServers() {
        return Collections.unmodifiableCollection(activeServers.values());
    }
    /**
     * Devuelve un mapa inmutable de todos los servidores activos.
     *
     * @return mapa de servidores activos por nombre.
     */
    public static Map<String, Server> getActiveServers() {
        return Collections.unmodifiableMap(activeServers);
    }
    /**
     * Indica si hay al menos un servidor actualmente en ejecución.
     *
     * @return {@code true} si hay servidores corriendo; de lo contrario, {@code false}.
     */
    public static boolean hasRunningServers() {
        return activeServers.values().stream().anyMatch(Server::isRunning);
    }
}
