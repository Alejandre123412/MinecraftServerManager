package com.servermanager.java;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.zip.*;
/**
 * Clase encargada de descargar e instalar automáticamente versiones de JDK desde Adoptium API.
 * Detecta el sistema operativo y arquitectura, descarga el archivo correspondiente, lo descomprime
 * y organiza la estructura de carpetas de forma estandarizada.
 *
 * Requiere Apache Commons Compress para extraer archivos tar.gz en sistemas Unix.
 *
 * <p>Soporta Windows (ZIP) y Unix (tar.gz).</p>
 */
public class JavaDownloader {
    /**
     * Descarga e instala un JDK en el directorio objetivo.
     *
     * @param version   versión del JDK (por ejemplo, 17).
     * @param targetDir directorio destino donde se instalará el JDK.
     * @throws IOException si hay errores durante la descarga o extracción.
     */
    public static void downloadJdk(int version, Path targetDir) throws IOException {
        String os = getOs();
        String arch = getArch();
        String extension = os.equals("windows") ? "zip" : "tar.gz";

        String apiUrl = String.format(
                "https://api.adoptium.net/v3/binary/latest/%d/ga/%s/%s/jdk/hotspot/normal/eclipse",
                version, os, arch
        );
        // Descarga
        System.out.println("[JavaDownloader] Descargando JDK desde: " + apiUrl);
        Path downloadPath = Files.createTempFile("jdk", "." + extension);
        try (InputStream in = new URL(apiUrl).openStream()) {
            Files.copy(in, downloadPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Extrae
        if (os.equals("windows")) {
            unzip(downloadPath, targetDir);
        } else {
            untar(downloadPath, targetDir);
        }
        renameExtractedJdkDir(targetDir, version);

        // Corrige la estructura: mueve JDK interno a jres/jdk<version>
        moveToRootJdkDir(targetDir);

        // Limpia
        Files.deleteIfExists(downloadPath);
        System.out.println("[JavaDownloader] JDK " + version + " descargado y listo en: " + targetDir.toAbsolutePath());
    }
    // Detecta el sistema operativo actual
    private static String getOs() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) return "windows";
        if (os.contains("mac")) return "mac";
        return "linux";
    }
    // Detecta la arquitectura del sistema
    private static String getArch() {
        String arch = System.getProperty("os.arch");
        return (arch.contains("64") || arch.equals("amd64")) ? "x64" : "x32";
    }
    // Descomprime un archivo ZIP en el directorio destino
    private static void unzip(Path zipFile, Path targetDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile.toFile()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path filePath = targetDir.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    Files.copy(zis, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
    /**
     * Extrae un archivo .tar.gz al directorio destino. Protege contra ataques ZipSlip.
     *
     * @param tarGzFile archivo a descomprimir.
     * @param targetDir carpeta destino.
     * @throws IOException si ocurre un error durante la extracción.
     */
    public static void untar(Path tarGzFile, Path targetDir) throws IOException {
        try (InputStream fi = Files.newInputStream(tarGzFile);
             InputStream gzi = new GzipCompressorInputStream(fi);
             TarArchiveInputStream tarIn = new TarArchiveInputStream(gzi)) {

            TarArchiveEntry entry;
            while ((entry = tarIn.getNextEntry()) != null) {
                Path outputPath = targetDir.resolve(entry.getName()).normalize();

                if (!outputPath.startsWith(targetDir)) {
                    // Evita paths fuera del destino (ataque ZipSlip)
                    throw new IOException("Entrada fuera del directorio objetivo: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(outputPath);
                } else {
                    if (outputPath.getParent() != null) {
                        Files.createDirectories(outputPath.getParent());
                    }
                    Files.copy(tarIn, outputPath);
                }
            }
        }
    }
    // Mueve el contenido del JDK extraído directamente a la raíz del destino
    private static void moveToRootJdkDir(Path targetDir) throws IOException {
        // A veces el ZIP crea un subdirectorio jdk-17.x.x dentro de jres/jdk17
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(targetDir)) {
            for (Path sub : stream) {
                if (Files.isDirectory(sub) && sub.getFileName().toString().startsWith("jdk")) {
                    Path temp = sub.resolve("bin").getParent();
                    if (Files.exists(temp)) {
                        moveDirectoryContents(sub, targetDir);
                        Files.walk(sub)
                                .sorted(Comparator.reverseOrder())
                                .map(Path::toFile)
                                .forEach(File::delete);
                        break;
                    }
                }
            }
        }
    }
    // Mueve archivos de un directorio a otro
    private static void moveDirectoryContents(Path src, Path dest) throws IOException {
        Files.walk(src).forEach(sourcePath -> {
            try {
                Path targetPath = dest.resolve(src.relativize(sourcePath));
                if (Files.isDirectory(sourcePath)) {
                    Files.createDirectories(targetPath);
                } else {
                    Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    /**
     * Renombra el directorio JDK extraído a un nombre estándar como "jdk17".
     *
     * @param jresDir     directorio base donde se extrajo el JDK.
     * @param javaVersion versión de Java.
     * @throws IOException si el JDK no se encuentra o hay errores al moverlo.
     */
    public static void renameExtractedJdkDir(Path jresDir, int javaVersion) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(jresDir)) {
            for (Path subDir : stream) {
                if (Files.isDirectory(subDir) && subDir.getFileName().toString().startsWith("jdk")) {
                    Path target = jresDir.resolve("jdk" + javaVersion);
                    if (Files.exists(target)) {
                        // Borrar el destino si ya existía (puedes adaptar esto si quieres conservarlo)
                        Files.walk(target)
                                .sorted(Comparator.reverseOrder())
                                .map(Path::toFile)
                                .forEach(File::delete);
                    }
                    Files.move(subDir, target);
                    return;
                }
            }
        }
        throw new IOException("No se encontró carpeta JDK tras descomprimir.");
    }

}
