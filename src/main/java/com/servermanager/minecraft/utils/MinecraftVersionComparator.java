package com.servermanager.minecraft.utils;

import com.servermanager.minecraft.versions.MinecraftVersion;
import java.util.Comparator;

/**
 * Comparador para ordenar versiones de Minecraft en orden descendente
 * basado en su versión numérica, ignorando etiquetas como "Snapshot".
 */
public class MinecraftVersionComparator implements Comparator<MinecraftVersion> {

    /**
     * Compara dos objetos MinecraftVersion para orden descendente.
     *
     * @param v1 Primera versión
     * @param v2 Segunda versión
     * @return Resultado de la comparación
     */
    @Override
    public int compare(MinecraftVersion v1, MinecraftVersion v2) {
        return compareVersions(v2.getVersion(), v1.getVersion()); // orden descendente
    }

    /**
     * Compara dos cadenas de versión numérica (ej: "1.20.1" o "1.20w13a").
     * Ignora prefijos "Snapshot" y partes no numéricas.
     *
     * @param a Versión a comparar
     * @param b Versión a comparar
     * @return Resultado de la comparación numérica
     */
    private int compareVersions(String a, String b) {
        String[] partsA = a.replace("Snapshot", "").trim().split("\\.");
        String[] partsB = b.replace("Snapshot", "").trim().split("\\.");

        int length = Math.max(partsA.length, partsB.length);
        for (int i = 0; i < length; i++) {
            int numA = i < partsA.length ? parseVersionPart(partsA[i]) : 0;
            int numB = i < partsB.length ? parseVersionPart(partsB[i]) : 0;

            if (numA != numB) {
                return Integer.compare(numA, numB);
            }
        }
        return 0;
    }

    /**
     * Intenta parsear una parte de la versión a entero.
     * Si no es numérica, devuelve 0.
     *
     * @param part Parte de la versión
     * @return Número parseado o 0 si falla
     */
    private int parseVersionPart(String part) {
        try {
            return Integer.parseInt(part);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
