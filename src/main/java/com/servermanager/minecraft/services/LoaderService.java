package com.servermanager.minecraft.services;

import com.servermanager.minecraft.models.ModLoaderInfo;
import com.servermanager.minecraft.models.PluginLoaderInfo;

import java.util.*;
/**
 * Servicio encargado de gestionar la información de loaders (mod y plugin loaders)
 * para diferentes versiones de Minecraft.
 *
 * @deprecated Esta clase está en desuso y puede ser removida en futuras versiones.
 */
@Deprecated
public class LoaderService {

    /**
     * Mapa que contiene loaders de mods organizados por nombre de loader y versión de Minecraft.
     */
    private final Map<String, Map<String, ModLoaderInfo>> modLoaders = new HashMap<>();
    /**
     * Mapa que contiene loaders de plugins organizados por nombre de loader y versión de Minecraft.
     */
    private final Map<String, Map<String, PluginLoaderInfo>> pluginLoaders = new HashMap<>();
    /**
     * Constructor que inicializa los datos simulados de loaders.
     */
    public LoaderService() {
        cargarModLoaders();
        cargarPluginLoaders();
    }
    /**
     * Carga datos simulados de mod loaders.
     * Normalmente estos datos vendrían de un archivo JSON o una API externa.
     */
    private void cargarModLoaders() {
        // Simulación, normalmente esto vendría de un JSON o API
        modLoaders.put("Forge", new HashMap<>());
        modLoaders.get("Forge").put("1.21.1", new ModLoaderInfo(
                "Forge", "1.21.1",
                List.of("46.0.5", "46.0.6", "46.0.7"),
                "46.0.6", // recomendado
                "46.0.7"  // latest
        ));
    }
    /**
     * Carga datos simulados de plugin loaders.
     */
    private void cargarPluginLoaders() {
        pluginLoaders.put("Paper", new HashMap<>());
        pluginLoaders.get("Paper").put("1.21.1", new PluginLoaderInfo(
                "Paper", "1.21.1",
                List.of("1.21.1-42", "1.21.1-43", "1.21.1-44"),
                "1.21.1-43", // recomendado
                "1.21.1-44"  // latest
        ));
    }

    /**
     * Obtiene la información del mod loader solicitado.
     *
     * @param request Objeto ModLoaderInfo con el nombre del loader y la versión de Minecraft.
     * @return La información del mod loader correspondiente, o null si no existe.
     */
    public ModLoaderInfo getLoaderInfo(ModLoaderInfo request) {
        return modLoaders
                .getOrDefault(request.getLoaderName(), Collections.emptyMap())
                .get(request.getMcVersion());
    }
    /**
     * Obtiene la información del plugin loader solicitado.
     *
     * @param request Objeto PluginLoaderInfo con el nombre del loader y la versión de Minecraft.
     * @return La información del plugin loader correspondiente, o null si no existe.
     */
    public PluginLoaderInfo getLoaderInfo(PluginLoaderInfo request) {
        return pluginLoaders
                .getOrDefault(request.getLoaderName(), Collections.emptyMap())
                .get(request.getMcVersion());
    }
}
