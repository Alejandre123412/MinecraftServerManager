package com.servermanager.minecraft.models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa la consola de un servidor Minecraft.
 * <p>
 * Permite manejar el flujo de entrada/salida de la consola, almacenar
 * el log de salida y enviar comandos al proceso del servidor.
 */
public class Console {
    /** Lista que almacena el log de la consola. */
    private final List<String> log = new ArrayList<>();

    /** Escritor para enviar comandos al proceso. */
    private PrintWriter writer;
    /**
     * Constructor que inicializa la consola con un OutputStream.
     * <p>
     * El OutputStream se usa para enviar comandos al servidor.
     *
     * @param outputStream el flujo de salida hacia el proceso del servidor.
     */
    public Console(OutputStream outputStream) {
        this.writer = new PrintWriter(outputStream, true);
    }
    /**
     * Constructor vacío para crear una consola sin streams inicialmente.
     * Los streams pueden adjuntarse posteriormente usando {@link #attach(OutputStream, InputStream)}.
     */
    public Console(){}
    /**
     * Adjunta los flujos de salida e entrada para la consola.
     * <p>
     * Inicia un hilo que lee continuamente del InputStream para capturar
     * la salida del servidor y almacenarla en el log.
     *
     * @param outputStream flujo de salida hacia el proceso del servidor (para enviar comandos).
     * @param inputStream flujo de entrada desde el proceso del servidor (para leer su salida).
     */
    public void attach(OutputStream outputStream, InputStream inputStream) {
        this.writer = new PrintWriter(outputStream, true);
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    synchronized (log) {
                        log.add(line+"\n");
                    }
                }
            } catch (IOException e) {
                synchronized (log) {
                    log.add("[ERROR] " + e.getMessage());
                }
            }
        }, "Console-Reader").start();
    }
    /**
     * Añade una línea al log de la consola.
     * <p>
     * Si la línea cumple con un patrón específico (comienza con corchetes),
     * se añade una línea vacía para separar visualmente en el log.
     *
     * @param line línea de texto a añadir al log.
     */
    public void write(String line) {
        if (line.matches("^\\[.*?\\].*")) {
            log.add(""); // Añade salto de línea visual
        }
        log.add(line);
    }

    /**
     * Envía un comando al proceso del servidor y lo registra en el log.
     *
     * @param command comando a enviar.
     */
    public void sendCommand(String command) {
        writer.println(command);
        writer.flush();
        log.add("> " + command);
    }
    /**
     * Obtiene una copia del log actual de la consola.
     *
     * @return lista con las líneas del log.
     */
    public List<String> getLog() {
        return new ArrayList<>(log);
    }
    /**
     * Limpia todo el log almacenado.
     */
    public void clearLog() {
        log.clear();
    }
}
