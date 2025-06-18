package com.servermanager.minecraft.models;

import java.io.*;
import java.util.Properties;
/**
 * Clase que representa y gestiona las propiedades de configuración de un servidor Minecraft.
 * Permite cargar estas propiedades desde un archivo y guardarlas en un archivo.
 * <p>
 * Los valores cargados corresponden a los parámetros habituales del archivo server.properties de Minecraft.
 * </p>
 */
public class Propiedades {
    // Campos booleanos que representan diversas configuraciones del servidor
    boolean accepts_transfers;
    boolean allow_flight;
    boolean allow_nether;
    boolean broadcast_console_to_ops;
    boolean broadcast_rcon_to_ops;
    boolean enable_command_block;
    boolean enable_jmx_monitoring;
    boolean enable_query;
    boolean enable_rcon;
    boolean enable_status;
    boolean enforce_secure_profile;
    boolean enforce_whitelist;
    boolean force_gamemode;
    boolean generate_structures;
    boolean hardcore;
    boolean hide_online_players;
    boolean log_ips;
    boolean online_mode;
    boolean prevent_proxy_connections;
    boolean pvp;
    boolean require_resource_pack;
    boolean spawn_monsters;
    boolean sync_chunk_writes;
    boolean use_native_transport;
    boolean white_list;

    // Campos enteros que representan configuraciones numéricas
    int entity_broadcast_range_percentage;
    int function_permission_level;
    int max_chained_neighbor_updates;
    int max_players;
    int max_tick_time;
    int max_world_size;
    int network_compression_threshold;
    int op_permission_level;
    int pause_when_empty_seconds;
    int player_idle_timeout;
    int query_port;
    int rate_limit;
    int rcon_port;
    int simulation_distance;
    int spawn_protection;
    int text_filtering_version;
    int view_distance;
    int server_port;

    // Campos String para otras configuraciones
    String bug_report_link;
    String difficulty;
    String gamemode;
    String[] generator_settings;
    String initial_disabled_packs;
    String initial_enabled_packs;
    String level_name;
    String level_seed;
    String level_type;
    String motd;
    String rcon_password;
    String region_file_compression;
    String resource_pack;
    String resource_pack_id;
    String resource_pack_prompt;
    String resource_pack_sha1;
    String server_ip;
    String text_filtering_config;
    /**
     * Constructor que carga las propiedades desde un archivo especificado.
     *
     * @param archivo Ruta al archivo de propiedades.
     */
    public Propiedades(String archivo) {
        cargarPropiedades(archivo);
    }
    /**
     * Método privado que carga las propiedades desde un archivo.
     * Si no se encuentra el archivo o hay un error, se imprime la traza de error.
     *
     * @param archivo Ruta al archivo desde donde cargar las propiedades.
     */
    private void cargarPropiedades(String archivo) {
        Properties propiedades = new Properties();

        try (FileInputStream fis = new FileInputStream(archivo)) {
            // Cargar el fis
            propiedades.load(fis);

            // Obtener valores
            accepts_transfers= Boolean.parseBoolean(propiedades.getProperty("accepts-transfers"));
            allow_flight= Boolean.parseBoolean(propiedades.getProperty("allow-flight"));
            allow_nether= Boolean.parseBoolean(propiedades.getProperty("allow-nether"));
             broadcast_console_to_ops= Boolean.parseBoolean(propiedades.getProperty("broadcast-console-to-ops"));
             broadcast_rcon_to_ops= Boolean.parseBoolean(propiedades.getProperty("broadcast-rcon-to-ops"));
             enable_command_block= Boolean.parseBoolean(propiedades.getProperty("enable-command_block"));
             enable_jmx_monitoring= Boolean.parseBoolean(propiedades.getProperty("enable-jmx-monitoring"));
             enable_query= Boolean.parseBoolean(propiedades.getProperty("enable-query"));
             enable_rcon= Boolean.parseBoolean(propiedades.getProperty("enable-rcon"));
             enable_status= Boolean.parseBoolean(propiedades.getProperty("enable-status"));
             enforce_secure_profile= Boolean.parseBoolean(propiedades.getProperty("enforce-secure-profile"));
             enforce_whitelist= Boolean.parseBoolean(propiedades.getProperty("enforce-whitelist"));
             force_gamemode= Boolean.parseBoolean(propiedades.getProperty("force-gamemode"));
             generate_structures= Boolean.parseBoolean(propiedades.getProperty("generate-structures"));
             hardcore= Boolean.parseBoolean(propiedades.getProperty("hardcore"));
             hide_online_players= Boolean.parseBoolean(propiedades.getProperty("hide-online-players"));
             log_ips= Boolean.parseBoolean(propiedades.getProperty("log-ips"));
             online_mode= Boolean.parseBoolean(propiedades.getProperty("online-mode"));
             prevent_proxy_connections= Boolean.parseBoolean(propiedades.getProperty("prevent-proxy-connections"));
             pvp= Boolean.parseBoolean(propiedades.getProperty("pvp"));
             require_resource_pack= Boolean.parseBoolean(propiedades.getProperty("require-resource-pack"));
             spawn_monsters= Boolean.parseBoolean(propiedades.getProperty("spawn-monsters"));
             sync_chunk_writes= Boolean.parseBoolean(propiedades.getProperty("sync-chunk-writes"));
             use_native_transport= Boolean.parseBoolean(propiedades.getProperty("use-native-transport"));
             white_list= Boolean.parseBoolean(propiedades.getProperty("white-list"));

             String value=propiedades.getProperty("entity-broadcast-range-percentage");
            entity_broadcast_range_percentage= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("function-permission-level");
             function_permission_level= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("max-chained-neighbor-updates");
             max_chained_neighbor_updates= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("max-players");
             max_players= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("max-tick-time");
             max_tick_time= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("max-world-size");
             max_world_size= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("network-compression-threshold");
             network_compression_threshold= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("op-permission-level");
             op_permission_level= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("pause-when-empty-seconds");
             pause_when_empty_seconds= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("player-idle-timeout");
             player_idle_timeout= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("query.port");
             query_port= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("rate-limit");
             rate_limit= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("rcon.port");
             rcon_port= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("simulation-distance");
             simulation_distance= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("spawn-protection");
             spawn_protection= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("text-filtering-version");
             text_filtering_version= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("view-distance");
             view_distance= Integer.parseInt(value!=null?value:"-1");
            value=propiedades.getProperty("server-port");
             server_port= Integer.parseInt(value!=null?value:"-1");

             bug_report_link= propiedades.getProperty("bug-report-link");
             difficulty= propiedades.getProperty("difficulty");
             gamemode= propiedades.getProperty("gamemode");
            String valor = propiedades.getProperty("generator-settings");
             generator_settings= (valor!=null?valor:"").split("\\\\s*,\\\\s*");
             initial_disabled_packs= propiedades.getProperty("initial_disabled_packs");
             initial_enabled_packs= propiedades.getProperty("initial_enabled_packs");
             level_name= propiedades.getProperty("level-name");
             level_seed= propiedades.getProperty("level-seed");
             level_type= propiedades.getProperty("level-type");
             motd= propiedades.getProperty("motd");
             rcon_password= propiedades.getProperty("rcon.password");
             region_file_compression= propiedades.getProperty("region-file-compression");
             resource_pack= propiedades.getProperty("resource-pack");
             resource_pack_id= propiedades.getProperty("resource-pack-id");
             resource_pack_prompt= propiedades.getProperty("resource-pack-prompt");
             resource_pack_sha1= propiedades.getProperty("resource-pack-sha1");
             server_ip= propiedades.getProperty("server-ip");
             text_filtering_config= propiedades.getProperty("text-filtering-config");
             

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Guarda las propiedades actuales en el archivo especificado.
     *
     * @param archivo Ruta del archivo donde se guardarán las propiedades.
     */
    public void guardar(String archivo) {
        Properties properties = new Properties();

        try (FileOutputStream out = new FileOutputStream(archivo)) {
            safeSetProperty(properties,"accepts-transfers", String.valueOf(accepts_transfers));
            safeSetProperty(properties,"allow-flight", String.valueOf(allow_flight));
            safeSetProperty(properties,"allow-nether", String.valueOf(allow_nether));
            safeSetProperty(properties,"broadcast-console-to-ops", String.valueOf(broadcast_console_to_ops));
            safeSetProperty(properties,"broadcast-rcon-to-ops", String.valueOf(broadcast_rcon_to_ops));
            safeSetProperty(properties,"enable-command_block", String.valueOf(enable_command_block));
            safeSetProperty(properties,"enable-jmx-monitoring", String.valueOf(enable_jmx_monitoring));
            safeSetProperty(properties,"enable-query", String.valueOf(enable_query));
            safeSetProperty(properties,"enable-rcon", String.valueOf(enable_rcon));
            safeSetProperty(properties,"enable-status", String.valueOf(enable_status));
            safeSetProperty(properties,"enforce-secure-profile", String.valueOf(enforce_secure_profile));
            safeSetProperty(properties,"enforce-whitelist", String.valueOf(enforce_whitelist));
            safeSetProperty(properties,"force-gamemode", String.valueOf(force_gamemode));
            safeSetProperty(properties,"generate-structures", String.valueOf(generate_structures));
            safeSetProperty(properties,"hardcore", String.valueOf(hardcore));
            safeSetProperty(properties,"hide-online-players", String.valueOf(hide_online_players));
            safeSetProperty(properties,"log-ips", String.valueOf(log_ips));
            safeSetProperty(properties,"online-mode", String.valueOf(online_mode));
            safeSetProperty(properties,"prevent-proxy-connections", String.valueOf(prevent_proxy_connections));
            safeSetProperty(properties,"pvp", String.valueOf(pvp));
            safeSetProperty(properties,"require-resource-pack", String.valueOf(require_resource_pack));
            safeSetProperty(properties,"spawn-monsters", String.valueOf(spawn_monsters));
            safeSetProperty(properties,"sync-chunk-writes", String.valueOf(sync_chunk_writes));
            safeSetProperty(properties,"use-native-transport", String.valueOf(use_native_transport));
            safeSetProperty(properties,"white-list", String.valueOf(white_list));

            safeSetProperty(properties,"entity-broadcast-range-percentage", String.valueOf(entity_broadcast_range_percentage));
            safeSetProperty(properties,"function-permission-level", String.valueOf(function_permission_level));
            safeSetProperty(properties,"max-chained-neighbor-updates", String.valueOf(max_chained_neighbor_updates));
            safeSetProperty(properties,"max-players", String.valueOf(max_players));
            safeSetProperty(properties,"max-tick-time", String.valueOf(max_tick_time));
            safeSetProperty(properties,"max-world-size", String.valueOf(max_world_size));
            safeSetProperty(properties,"network-compression-threshold", String.valueOf(network_compression_threshold));
            safeSetProperty(properties,"op-permission-level", String.valueOf(op_permission_level));
            safeSetProperty(properties,"pause-when-empty-seconds", String.valueOf(pause_when_empty_seconds));
            safeSetProperty(properties,"player-idle-timeout", String.valueOf(player_idle_timeout));
            safeSetProperty(properties,"query.port", String.valueOf(query_port));
            safeSetProperty(properties,"rate-limit", String.valueOf(rate_limit));
            safeSetProperty(properties,"rcon.port", String.valueOf(rcon_port));
            safeSetProperty(properties,"simulation-distance", String.valueOf(simulation_distance));
            safeSetProperty(properties,"spawn-protection", String.valueOf(spawn_protection));
            safeSetProperty(properties,"text-filtering-version", String.valueOf(text_filtering_version));
            safeSetProperty(properties,"view-distance", String.valueOf(view_distance));
            safeSetProperty(properties,"server-port", String.valueOf(server_port));

            safeSetProperty(properties,"bug-report-link", bug_report_link);
            safeSetProperty(properties,"difficulty", difficulty);
            safeSetProperty(properties,"gamemode", gamemode);
            safeSetProperty(properties,"generator-settings", String.join(",", generator_settings));
            safeSetProperty(properties,"initial-disabled-packs", initial_disabled_packs);
            safeSetProperty(properties,"initial-enabled-packs", initial_enabled_packs);
            safeSetProperty(properties,"level-name", level_name);
            safeSetProperty(properties,"level-seed", level_seed);
            safeSetProperty(properties,"level-type", level_type);
            safeSetProperty(properties,"motd", motd);
            safeSetProperty(properties,"rcon.password", rcon_password);
            safeSetProperty(properties,"region-file-compression", region_file_compression);
            safeSetProperty(properties,"resource-pack", resource_pack);
            safeSetProperty(properties,"resource-pack-id", resource_pack_id);
            safeSetProperty(properties,"resource-pack-prompt", resource_pack_prompt);
            safeSetProperty(properties,"resource-pack-sha1", resource_pack_sha1);
            safeSetProperty(properties,"server-ip", server_ip);
            safeSetProperty(properties,"text-filtering-config", text_filtering_config);

            properties.store(out, "Propiedades del servidor guardadas desde la interfaz");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Método auxiliar para establecer propiedades de forma segura en un objeto Properties.
     * Si la clave o valor es null, se maneja apropiadamente para evitar excepciones.
     *
     * @param properties Objeto Properties donde se agregará la propiedad.
     * @param key        Clave de la propiedad.
     * @param value      Valor de la propiedad.
     */
    private void safeSetProperty(Properties properties, String key, Object value) {
        if (key == null || value == null) {
            if(key==null) return;
            properties.setProperty(key,"");
            return;
        }
        properties.setProperty(key,String.valueOf(value));
    }

}
