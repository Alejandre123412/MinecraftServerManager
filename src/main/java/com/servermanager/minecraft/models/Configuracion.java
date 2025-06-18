package com.servermanager.minecraft.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.servermanager.minecraft.versions.MinecraftVersion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Representa la configuración de un servidor Minecraft,
 * incluyendo la versión del servidor y una descripción opcional.
 * <p>
 * Permite guardar esta configuración en un archivo JSON mediante Gson.
 */
public class Configuracion {
    /** Versión de Minecraft asociada a esta configuración. */
    private MinecraftVersion version;

    /** Descripción opcional del servidor o configuración. */
    private String descripcion;
    /**
     * Constructor por defecto.
     * Inicializa la versión con "1.0" y activa el flag por defecto (true).
     */
    public Configuracion() {
        this.version = new MinecraftVersion("1.0", true);
    }
    /**
     * Constructor que permite definir la versión inicial.
     *
     * @param version versión de Minecraft para esta configuración.
     */
    public Configuracion(MinecraftVersion version) {
        this.version = version;
    }
    /**
     * Obtiene la versión de Minecraft configurada.
     *
     * @return la versión de Minecraft.
     */
    public MinecraftVersion getVersion() {
        return version;
    }
    /**
     * Establece la versión de Minecraft para esta configuración.
     *
     * @param version nueva versión de Minecraft.
     */
    public void setVersion(MinecraftVersion version) {
        this.version = version;
    }
    /**
     * Obtiene la descripción del servidor o configuración.
     *
     * @return la descripción (puede ser null si no está establecida).
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * Establece una descripción para el servidor o configuración.
     *
     * @param descripcion texto descriptivo.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * Guarda la configuración actual en un archivo JSON.
     * <p>
     * Si ocurre un error de E/S durante el guardado, se imprime el stacktrace.
     *
     * @param archivo archivo donde se guardará la configuración JSON.
     */
    public void guardar(File archivo) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(archivo)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

