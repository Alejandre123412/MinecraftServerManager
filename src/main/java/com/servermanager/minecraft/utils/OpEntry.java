package com.servermanager.minecraft.utils;

/**
 * Representa una entrada de operador (OP) en el servidor de Minecraft.
 * Contiene la información básica para identificar y manejar permisos especiales.
 */
public class OpEntry {
    /** UUID único del jugador */
    public String uuid;

    /** Nombre del jugador */
    public String name;

    /** Nivel de operador que indica los permisos asignados */
    public int level;

    /** Indica si el operador puede ignorar el límite de jugadores */
    public boolean bypassesPlayerLimit;
}
