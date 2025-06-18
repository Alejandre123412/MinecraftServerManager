package com.servermanager.minecraft.models;

import java.util.List;

/**
 * Información detallada sobre un Plugin Loader para una versión específica de Minecraft.
 *
 * @deprecated Esta clase está en desuso y puede ser removida en futuras versiones.
 */
@Deprecated
public class PluginLoaderInfo {
    /**
     * Nombre del loader, por ejemplo "Paper".
     */
    private final String loaderName;

    /**
     * Versión de Minecraft a la que corresponde este loader.
     */
    private final String mcVersion;

    /**
     * Lista de versiones disponibles del loader.
     */
    private final List<String> versions;

    /**
     * Versión recomendada del loader.
     */
    private final String recommended;

    /**
     * Última versión disponible del loader.
     */
    private final String latest;

    /**
     * Constructor para crear una instancia de PluginLoaderInfo.
     *
     * @param loaderName  Nombre del loader.
     * @param mcVersion   Versión de Minecraft.
     * @param versions    Lista de versiones disponibles del loader.
     * @param recommended Versión recomendada.
     * @param latest      Última versión disponible.
     */
    public PluginLoaderInfo(String loaderName, String mcVersion, List<String> versions, String recommended, String latest) {
        this.loaderName = loaderName;
        this.mcVersion = mcVersion;
        this.versions = versions;
        this.recommended = recommended;
        this.latest = latest;
    }

    /**
     * @return El nombre del loader.
     */
    public String getLoaderName() {
        return loaderName;
    }

    /**
     * @return La versión de Minecraft.
     */
    public String getMcVersion() {
        return mcVersion;
    }

    /**
     * @return Lista de versiones disponibles del loader.
     */
    public List<String> getVersions() {
        return versions;
    }

    /**
     * @return La versión recomendada del loader.
     */
    public String getRecommended() {
        return recommended;
    }

    /**
     * @return La última versión disponible del loader.
     */
    public String getLatest() {
        return latest;
    }
}
