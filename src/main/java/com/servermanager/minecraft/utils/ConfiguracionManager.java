package com.servermanager.minecraft.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.servermanager.minecraft.models.Configuracion;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Clase utilitaria para cargar y guardar la configuración
 * de un servidor en formato JSON usando Gson.
 */
public class ConfiguracionManager {

    /** Nombre del archivo de configuración JSON */
    private static final String CONFIG_PATH = "config.json";

    /** Instancia de Gson para serialización y deserialización */
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Carga la configuración desde un archivo JSON ubicado en el path dado.
     * Si el archivo no existe o hay error, devuelve una configuración por defecto.
     *
     * @param path Ruta del directorio donde se encuentra config.json
     * @return Objeto Configuracion cargado o por defecto
     */
    public static Configuracion cargar(String path) {
        try (FileReader reader = new FileReader(path + "/" + CONFIG_PATH)) {
            return gson.fromJson(reader, Configuracion.class);
        } catch (IOException e) {
            return new Configuracion(); // valor por defecto
        }
    }

    /**
     * Guarda la configuración en formato JSON en el path especificado.
     *
     * @param path Directorio donde se guardará el archivo config.json
     * @param config Objeto Configuracion a guardar
     */
    public static void guardar(String path, Configuracion config) {
        try (FileWriter writer = new FileWriter(path + "/" + CONFIG_PATH)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
