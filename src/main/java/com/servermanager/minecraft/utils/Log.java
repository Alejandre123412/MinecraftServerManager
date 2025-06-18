package com.servermanager.minecraft.utils;

import java.util.List;

/**
 * Representa un registro de texto compuesto por varias líneas.
 * Permite acceder a las líneas y imprimirlas por consola.
 */
public class Log {
    /** Lista de líneas que contiene el log */
    private List<String> lines;

    /**
     * Constructor que inicializa el log con una lista de líneas.
     *
     * @param lines Lista de líneas que forman el log
     */
    public Log(List<String> lines) {
        this.lines = lines;
    }

    /**
     * Obtiene la lista de líneas del log.
     *
     * @return Lista de líneas
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * Imprime todas las líneas del log en la salida estándar (consola).
     */
    public void print() {
        lines.forEach(System.out::println);
    }
}
