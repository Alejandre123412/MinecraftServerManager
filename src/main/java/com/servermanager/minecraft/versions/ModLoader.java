package com.servermanager.minecraft.versions;

import java.io.File;
/**
 * @deprecated Esta clase está en desuso y puede ser eliminada en futuras versiones.
 * Representa un Mod Loader para Minecraft con un nombre y una URL o comando de descarga/instalación.
 */
@Deprecated
public class ModLoader {
    private final String name;
    private final String urlDescarga; // o comando de instalación
    /**
     * Constructor para crear un ModLoader con nombre y URL o comando de descarga.
     *
     * @param name Nombre del Mod Loader.
     * @param urlDescarga URL para descargar el loader o comando de instalación.
     */
    public ModLoader(String name, String urlDescarga) {
        this.name = name;
        this.urlDescarga = urlDescarga;
    }
    /**
     * Obtiene el nombre del Mod Loader.
     *
     * @return Nombre del Mod Loader.
     */
    public String getName() { return name; }
    /**
     * Obtiene la URL o comando de descarga del Mod Loader.
     *
     * @return URL o comando de descarga.
     */
    public String getUrlDescarga() { return urlDescarga; }
    /**
     * Método para instalar el Mod Loader en la carpeta del servidor.
     *
     * @param carpetaServidor Carpeta donde se instalará el Mod Loader.
     *
     * Implementar la lógica de descarga y copia del archivo necesario.
     */
    public void instalar(File carpetaServidor) {
        // Implementar lógica de descarga + instalación del loader
        // Por ejemplo, descargar el JAR y moverlo a la carpeta
    }
}

