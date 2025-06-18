package com.servermanager.minecraft.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase encargada de cargar mods desde el directorio "mods" dentro de un directorio base.
 */
public class ModLoader {
    /**
     * Carga los mods que estén en la carpeta "mods" dentro del directorio especificado.
     * Si no existe la carpeta, la crea y retorna una lista vacía.
     *
     * @param directorio directorio base donde buscar la carpeta "mods"
     * @return lista de mods encontrados
     */
    public static List<Mod> cargarMods(File directorio){
        List<Mod> mods = new ArrayList<>();
        File modsFolder = new File(directorio, "mods");

        if (!modsFolder.exists()) {
            modsFolder.mkdir();
            return mods;
        }

        File[] modFiles = modsFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));
        if (modFiles == null) return mods;

        for (File file : modFiles) {
            // Ejemplo: "mimod-1.2-1.19.4.jar" => nombre: mimod, version: 1.2, version requerida: 1.19.4
            String fileName = file.getName();
            String baseName = fileName.substring(0, fileName.length() - 4); // sin ".jar"
            String[] partes = baseName.split("-");

            if (partes.length >= 3) {
                String nombre = partes[0];
                String version = partes[1];
                String versionRequerida = partes[2];
                mods.add(new Mod(file.getAbsolutePath(), version, versionRequerida));
            } else {
                // En caso de formato incorrecto, puedes omitirlo o agregar un mod genérico
                mods.add(new Mod(file.getAbsolutePath(), "desconocida", "desconocida"));
            }
        }

        return mods;
    }
}
