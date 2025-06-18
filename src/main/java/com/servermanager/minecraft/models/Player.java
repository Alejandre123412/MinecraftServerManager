package com.servermanager.minecraft.models;

import java.util.Objects;
/**
 * Representa un jugador de Minecraft con un UUID único y un nombre.
 * La clase implementa métodos para comparar jugadores basándose en su UUID.
 */
public class Player {
    /**
     * UUID único del jugador.
     */
    String uuid;

    /**
     * Nombre visible del jugador.
     */
    String name;

    /**
     * Constructor principal.
     *
     * @param uuid UUID único del jugador.
     * @param name Nombre visible del jugador.
     */
    public Player(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    /**
     * Obtiene el UUID del jugador.
     *
     * @return UUID como cadena de texto.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Asigna un UUID al jugador.
     *
     * @param uuid UUID a asignar.
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Obtiene el nombre visible del jugador.
     *
     * @return Nombre del jugador.
     */
    public String getName() {
        return name;
    }

    /**
     * Asigna un nombre visible al jugador.
     *
     * @param name Nombre a asignar.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Representación en forma de texto del jugador.
     *
     * @return Cadena con el formato "nombre (uuid)".
     */
    @Override
    public String toString() {
        return name + " (" + uuid + ")";
    }
    /**
     * Compara si dos objetos Player son iguales basándose en el UUID.
     *
     * @param o Objeto a comparar.
     * @return true si los UUID son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(uuid, player.uuid);
    }
    /**
     * Genera un código hash basado en el UUID del jugador.
     *
     * @return Código hash entero.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}

