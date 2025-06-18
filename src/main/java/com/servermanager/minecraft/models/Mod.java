package com.servermanager.minecraft.models;

import com.servermanager.minecraft.error.NonExsitenceOfJar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * Representa un mod para Minecraft.
 * <p>
 * Contiene información sobre el archivo .jar del mod,
 * su versión y la versión requerida para funcionar.
 */
public class Mod {
    /** Nombre del mod (normalmente el nombre del archivo .jar). */
    String name;

    /** Versión del mod. */
    String version;

    /** Versión requerida del juego para este mod. */
    String versionRequerida;

    /** Archivo .jar del mod. */
    File jar;
    /**
     * Crea un Mod a partir de la ruta del archivo, la versión y la versión requerida.
     *
     * @param archivo ruta del archivo .jar del mod.
     * @param version versión del mod.
     * @param versionRequerida versión requerida para el mod.
     */
    public Mod(String archivo,String version, String versionRequerida){
        this.jar=new File(archivo);
        this.name = this.jar.getName();
        this.version=version;
        this.versionRequerida=versionRequerida;
    }
    /**
     * Crea un Mod a partir del archivo .jar.
     * <p>
     * Intenta inferir la versión y la versión requerida del nombre del archivo,
     * que se espera tenga formato: nombre-version-versionRequerida.jar
     *
     * @param archivo archivo .jar del mod.
     */
    public Mod(File archivo){
        this.jar=archivo;
        this.name = this.jar.getName();
        String baseName = name.substring(0, name.length() - 4); // sin ".jar"
        String[] partes = baseName.split("-");

        String version = "Desconocida",versionRequerida = "Desconocida";

        if (partes.length >= 3) {
            version = partes[1];
            versionRequerida = partes[2];
        }
        this.version=version;
        this.versionRequerida=versionRequerida;
    }
    /**
     * Crea un Mod solo con el nombre (sin archivo asociado).
     *
     * @param name nombre del mod.
     */
    public Mod(String name){
        this.name = name;
    }
    /**
     * Obtiene el nombre del mod (equivalente al nombre del archivo .jar).
     *
     * @return nombre del mod.
     */
    public String getNameModLoader() {
        return name;
    }
    /**
     * Obtiene la versión del mod.
     *
     * @return versión del mod.
     */
    public String getVersion() {
        return version;
    }
    /**
     * Obtiene la versión requerida para el mod.
     *
     * @return versión requerida.
     */
    public String getVersionRequerida() {
        return versionRequerida;
    }
    /**
     * Obtiene el archivo .jar del mod.
     *
     * @return archivo .jar.
     */
    public File getJar() {
        return jar;
    }
    /**
     * Guarda el archivo .jar del mod en la carpeta destino especificada.
     * <p>
     * Si el archivo no existe lanza una excepción {@link NonExsitenceOfJar}.
     * Si la carpeta destino no existe, se crea.
     * Si el archivo ya está en la carpeta destino, no se copia.
     *
     * @param carpetaDestino ruta del directorio donde se guardará el .jar.
     * @throws NonExsitenceOfJar si el archivo .jar no existe.
     * @throws IOException si ocurre un error durante la copia.
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
     * Compara dos mods por su nombre.
     *
     * @param o objeto a comparar.
     * @return true si los nombres son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Mod mod = (Mod) o;
        return Objects.equals(name, mod.name);
    }
    /**
     * Genera el código hash basado en el nombre.
     *
     * @return código hash.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
