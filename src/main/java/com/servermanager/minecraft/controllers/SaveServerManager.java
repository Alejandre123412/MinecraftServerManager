package com.servermanager.minecraft.controllers;

import com.servermanager.minecraft.models.Server;

import java.util.ArrayList;
import java.util.List;
/**
 * Clase estática que gestiona una lista global de servidores {@link Server}.
 * Permite agregar, eliminar y guardar todos los servidores registrados.
 *
 * <p>Se usa para mantener el estado de los servidores activos o configurados en memoria,
 * y persistirlos cuando sea necesario mediante {@link #saveAllServers()}.</p>
 */
public class SaveServerManager {
    /**
     * Lista interna que almacena todos los servidores gestionados.
     */
    private static List<Server> servers = new ArrayList<>();
    /**
     * Agrega un servidor a la lista de servidores gestionados.
     *
     * @param s servidor a agregar.
     */
    public static void addServer(Server s){
        servers.add(s);
    }
    /**
     * Elimina un servidor de la lista de servidores gestionados.
     *
     * @param s servidor a eliminar.
     */
    public static void removeServer(Server s){
        servers.remove(s);
    }
    /**
     * Agrega múltiples servidores a la lista.
     *
     * @param serverList lista de servidores a agregar.
     */
    public static void addServers(List<Server> serverList){
        servers.addAll(serverList);
    }
    /**
     * Elimina múltiples servidores de la lista.
     *
     * @param serverList lista de servidores a eliminar.
     */
    public static void removeServers(List<Server> serverList){
        servers.removeAll(serverList);
    }
    /**
     * Llama al método {@code guardar()} de todos los servidores registrados.
     * Este método persiste en almacenamiento todos los cambios realizados a los servidores.
     */
    public static void saveAllServers(){
        for(Server s:servers) s.guardar();
    }
}
