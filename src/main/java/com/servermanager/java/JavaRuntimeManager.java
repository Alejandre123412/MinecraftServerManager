package com.servermanager.java;

import java.io.*;
import java.nio.file.*;
/**
 * Clase de utilidad para gestionar ejecuciones con versiones específicas del JDK.
 * Asegura que la versión requerida esté instalada, y si no lo está, la descarga usando {@link JavaDownloader}.
 */
public class JavaRuntimeManager {

    private static final String BASE_DIR = "jres";
    /**
     * Retorna la ruta al ejecutable de Java para una versión específica.
     * Si no está instalado, lo descarga automáticamente.
     *
     * @param version versión del JDK requerida (por ejemplo, 17).
     * @return ruta absoluta al ejecutable de Java.
     * @throws IOException si ocurre un error durante la descarga o verificación.
     */
    public static Path getJavaExecutable(int version) throws IOException {
        Path jdkDir = Paths.get(BASE_DIR, "jdk" + version);
        String executableName = isWindows() ? "java.exe" : "java";
        Path javaExecutable = jdkDir.resolve("bin").resolve(executableName);

        if (!Files.exists(javaExecutable)) {
            JavaDownloader.downloadJdk(version, jdkDir);// tu lógica de descarga
        }

        if (!Files.exists(javaExecutable)) {
            throw new IOException("java no encontrado tras intentar descargar JDK " + version);
        }

        return javaExecutable;
    }
    // Detecta si el sistema operativo es Windows
    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
