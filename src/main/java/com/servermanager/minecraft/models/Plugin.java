package com.servermanager.minecraft.models;

import com.servermanager.minecraft.error.NonExsitenceOfJar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
/**
 * Representa un plugin de Minecraft, generalmente un archivo JAR.
 * Proporciona funcionalidad para almacenar y copiar el archivo JAR del plugin a un directorio destino.
 */
public class Plugin {
    /**
     * Nombre del plugin, usualmente el nombre del archivo JAR.
     */
    String name;

    /**
     * Archivo JAR asociado al plugin.
     */
    File jar;

    /**
     * Constructor que crea un plugin a partir de la ruta del archivo JAR.
     * El nombre del plugin se asigna automáticamente usando el nombre del archivo.
     *
     * @param archivo Ruta al archivo JAR del plugin.
     */
    public Plugin(String archivo){
        this.jar=new File(archivo);
        this.name = this.jar.getName();
    }
    /**
     * Constructor que crea un plugin con un nombre dado.
     * Nota: Este constructor no asigna el archivo JAR.
     *
     * @param archivo Ruta al archivo JAR del plugin (no asignado aquí).
     * @param nombre Nombre del plugin.
     */
    public Plugin(String archivo, String nombre){
        this.name = nombre;
    }
    /**
     * Copia el archivo JAR del plugin a un directorio destino.
     * Si el archivo ya existe en el destino, no realiza ninguna acción.
     * Crea el directorio destino si no existe.
     *
     * @param carpetaDestino Ruta del directorio donde se guardará el plugin.
     * @throws NonExsitenceOfJar Si el archivo JAR no existe.
     * @throws IOException En caso de error al copiar el archivo.
     */
    public void guardar(String carpetaDestino) throws NonExsitenceOfJar, IOException {
        if (jar == null || !jar.exists()) {
            throw new NonExsitenceOfJar("No se ha encontrado el archivo Jar");
        }

        File carpeta = new File(carpetaDestino);
        if (!carpeta.exists()) {
            carpeta.mkdirs(); // crea directorio si no existe
        }

        File destino = new File(carpeta, jar.getName());

        // Si el archivo ya está en la carpeta destino, no hacer nada
        if (jar.getCanonicalPath().equals(destino.getCanonicalPath())) {
            return;
        }

        // Copiar y reemplazar si ya existe
        Files.copy(jar.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Compara dos objetos Plugin para igualdad basándose en el nombre.
     *
     * @param o Objeto a comparar.
     * @return true si los nombres son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Plugin plugin = (Plugin) o;
        return Objects.equals(name, plugin.name);
    }
    /**
     * Genera un código hash basado en el nombre del plugin.
     *
     * @return Código hash entero.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
