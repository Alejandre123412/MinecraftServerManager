package com.servermanager.minecraft.models;

import java.io.*;
/**
 * Clase para manejar el archivo EULA de un servidor Minecraft.
 * <p>
 * Permite cargar el estado del EULA desde un archivo,
 * modificarlo y guardarlo de nuevo.
 */
public class Eula {
    /** Indica si el EULA fue aceptado. */
    private boolean aceptar;

    /** Ruta del archivo EULA (no utilizada actualmente). */
    static String url;
    /**
     * Crea una instancia de Eula y carga el estado desde el archivo dado.
     *
     * @param archivo ruta del archivo eula.txt a cargar.
     * @throws RuntimeException si el archivo no existe o no puede leerse.
     */
    public Eula(String archivo) {
        cargarEULA(archivo);
    }
    /**
     * Carga el estado del EULA desde un archivo.
     * <p>
     * Busca la línea que empieza con "eula=" y la interpreta como booleano.
     *
     * @param archivo ruta del archivo eula.txt.
     * @throws RuntimeException si ocurre un error de lectura.
     */
    private void cargarEULA(String archivo) {
        try(BufferedReader br=new BufferedReader(new FileReader(archivo))){
            String linea;
            while((linea=br.readLine())!=null){
                if(linea.startsWith("#")) continue;
                if(linea.startsWith("eula=")){
                    String[] split=linea.split("=");
                    this.aceptar=Boolean.parseBoolean(split[1]);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Devuelve si el EULA ha sido aceptado.
     *
     * @return true si se aceptó el EULA, false en caso contrario.
     */
    public boolean isAceptar() {
        return aceptar;
    }
    /**
     * Guarda el estado actual del EULA en un archivo.
     * <p>
     * El archivo se sobrescribe con el valor actual de aceptación.
     *
     * @param archivo ruta del archivo donde se guardará el estado.
     */
    public void guardar(String archivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            pw.println("# EULA aceptada automáticamente");
            pw.println("eula=" + aceptar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Cambia el estado de aceptación del EULA.
     *
     * @param b nuevo valor para el estado de aceptación.
     */
    public void setAceptar(boolean b) {
        this.aceptar=b;
    }
}
