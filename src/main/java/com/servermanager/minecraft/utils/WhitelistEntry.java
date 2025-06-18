package com.servermanager.minecraft.utils;

/**
 * Representa una entrada en la lista blanca (whitelist) de un servidor Minecraft.
 * Contiene la información mínima para identificar a un jugador permitido.
 */
public class WhitelistEntry {
    /** UUID único del jugador */
    public String uuid;

    /** Nombre del jugador */
    public String name;
}
