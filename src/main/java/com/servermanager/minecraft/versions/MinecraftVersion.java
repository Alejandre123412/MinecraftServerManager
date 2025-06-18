package com.servermanager.minecraft.versions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
/**
 * Representa una versión de Minecraft con información asociada,
 * incluyendo si es una versión estable, la URL del manifiesto, y la versión requerida de Java.
 */
public class MinecraftVersion {
    private final String version;
    private final boolean isStable;
    private String manifestURL;
    private int requiredJavaVersion; // e.g. 8, 17, 21

    /**
     * Crea una instancia de MinecraftVersion.
     * @param version La versión como cadena (ej. "1.20.1").
     * @param isStable Indica si es una versión estable (true) o snapshot (false).
     */
    public MinecraftVersion(String version, boolean isStable) {
        this.version = version;
        this.isStable = isStable;
        this.manifestURL=null;
    }
    /**
     * Crea una instancia de MinecraftVersion con la URL del manifiesto.
     * @param version La versión como cadena (ej. "1.20.1").
     * @param isStable Indica si es una versión estable (true) o snapshot (false).
     * @param url URL al manifiesto JSON de esta versión.
     */
    public MinecraftVersion(String version, boolean isStable, String url) {
        this.version = version;
        this.isStable = isStable;
        this.manifestURL=url;
    }
    /**
     * Obtiene la versión de Minecraft.
     * @return La versión como cadena.
     */
    public String getVersion() { return version; }

    /**
     * Indica si la versión es estable.
     * @return true si es estable, false si es snapshot.
     */
    public boolean isStable() { return isStable; }

    /**
     * Obtiene la URL del archivo JAR del servidor para esta versión.
     * Este método descarga y procesa el manifiesto JSON para extraer la URL
     * y también obtiene la versión requerida de Java.
     *
     * @return URL del JAR del servidor o null si ocurre un error.
     */
    public String getJarUrl() {
        try {
            URL url = new URL(this.manifestURL); // `this.url` es la URL de `version.json`
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            try (InputStream is = con.getInputStream();
                 Reader reader = new InputStreamReader(is)) {

                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                JsonObject downloads = root.getAsJsonObject("downloads");
                JsonObject server = downloads.getAsJsonObject("server");
                JsonObject javaVersion=root.getAsJsonObject("javaVersion");
                JsonElement majorVersion=javaVersion.get("majorVersion");
                this.requiredJavaVersion= majorVersion.getAsInt();

                return server.get("url").getAsString();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Obtiene la URL del manifiesto JSON para esta versión.
     * @return La URL del manifiesto.
     */
    private String getManifestUrl() {
        return this.manifestURL;
    }

    /**
     * Representación en cadena de la versión.
     * Añade "(Snapshot)" si no es estable.
     * @return La representación en cadena.
     */
    @Override
    public String toString() {
        return version + (isStable ? "" : " (Snapshot)");
    }

    /**
     * Compara esta versión con otro objeto para igualdad.
     * Se consideran iguales si tienen el mismo número de versión.
     * @param o Otro objeto a comparar.
     * @return true si son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MinecraftVersion that = (MinecraftVersion) o;
        return Objects.equals(version, that.version);
    }
    /**
     * Código hash basado en la versión.
     * @return Código hash.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(version);
    }
    /**
     * Obtiene la versión requerida de Java para esta versión de Minecraft.
     * Se actualiza cuando se llama a {@link #getJarUrl()}.
     * @return Versión requerida de Java (por ejemplo, 8, 17, 21).
     */
    public int getRequiredJavaVersion() {
        return requiredJavaVersion;
    }
}

