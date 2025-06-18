package com.servermanager.minecraft.utils;

/**
 * Representa un usuario cacheado con información temporal,
 * incluyendo nombre, UUID y fecha de expiración del cache.
 */
public class CachedUser {
    /** Nombre del usuario */
    public String name;

    /** UUID del usuario */
    public String uuid;

    /** Fecha de expiración del cache en formato String */
    public String expiresOn;
}
