package com.servermanager.minecraft.utils;

import com.google.gson.*;
import com.servermanager.minecraft.versions.MinecraftVersion;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Carga las versiones oficiales de Minecraft consultando el manifiesto
 * público proporcionado por Mojang a través de internet.
 */
public class MinecraftVersionLoader {

    /** URL del manifiesto de versiones oficial de Minecraft */
    private static final String VERSION_MANIFEST_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json";

    /**
     * Carga todas las versiones oficiales de Minecraft desde el manifiesto remoto.
     * Ordena las versiones de forma descendente según su número de versión.
     *
     * @return Lista de versiones de Minecraft
     * @throws IOException Si ocurre un error de red o lectura
     */
    public static List<MinecraftVersion> loadAllVersions() throws IOException {
        List<MinecraftVersion> versions = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(VERSION_MANIFEST_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            assert response.body() != null;
            String json = response.body().string();
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonArray versionArray = root.getAsJsonArray("versions");

            for (JsonElement element : versionArray) {
                JsonObject obj = element.getAsJsonObject();
                String id = obj.get("id").getAsString();
                String type = obj.get("type").getAsString();
                String versionUrl = obj.get("url").getAsString();
                boolean isStable = type.equals("release");

                versions.add(new MinecraftVersion(id, isStable, versionUrl));
            }
        }
        versions.sort(new MinecraftVersionComparator());
        return versions;
    }
}
