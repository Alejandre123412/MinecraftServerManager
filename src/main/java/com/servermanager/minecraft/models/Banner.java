package com.servermanager.minecraft.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.servermanager.minecraft.utils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Clase que gestiona los jugadores baneados, operadores (ops), la whitelist y bans por IP
 * de un servidor de Minecraft.
 * <p>
 * Esta clase carga los datos desde los archivos JSON generados por el servidor de Minecraft,
 * como `banned-players.json`, `ops.json`, `whitelist.json`, `banned-ips.json` y `usercache.json`.
 */
public class Banner {
    /** Lista de jugadores baneados. */
    public List<Player> bannedPlayers;

    /** Lista de operadores del servidor. */
    public List<Player> ops;

    /** Lista de jugadores permitidos (whitelist). */
    public List<Player> whiteList;

    /** Lista de baneos por dirección IP. */
    public List<IPBan> ipBans;

    /** Mapa de UUID a nombre de usuario cargado desde `usercache.json`. */
    private Map<String, String> uuidToName;

    /**
     * Construye un nuevo objeto Banner cargando los datos de control de acceso
     * desde los archivos del directorio del servidor.
     *
     * @param serverDir Ruta al directorio del servidor.
     * @throws FileNotFoundException si alguno de los archivos requeridos no puede ser leído.
     */
    public Banner(String serverDir) throws FileNotFoundException {

        this.bannedPlayers=new ArrayList<>();
        this.ops=new ArrayList<>();
        this.ipBans=new ArrayList<>();
        this.whiteList=new ArrayList<>();
            Gson gson = new Gson();

            // Definir los tipos de lista para Gson
            Type userCacheType = new TypeToken<List<CachedUser>>() {
            }.getType();
            Type ipBanType = new TypeToken<List<IPBan>>() {
            }.getType();
            Type bannedType = new TypeToken<List<BannedEntry>>() {
            }.getType();
            Type opsType = new TypeToken<List<OpEntry>>() {
            }.getType();
            Type whitelistType = new TypeToken<List<WhitelistEntry>>() {
            }.getType();

            // Cargar usercache.json

            File f=new File(serverDir+"/usercache.json");
            if(f.exists()){
                List<CachedUser> cachedUsers = gson.fromJson(new FileReader(f), userCacheType);
                uuidToName = cachedUsers.stream()
                        .collect(Collectors.toMap(u -> u.uuid, u -> u.name));
            }else{
                uuidToName=new HashMap<>();
            }

            // Leer archivos JSON
            List<IPBan> ipBanList;
            List<BannedEntry> bannedList;
            List<OpEntry> opList;
            List<WhitelistEntry> whitelist;
            f=new File(serverDir+"/banned-ips.json");
            if(f.exists()){
                ipBanList = gson.fromJson(new FileReader(f), ipBanType);
            }else{
                ipBanList=new ArrayList<>();
            }
            f=new File(serverDir+"/banned-players.json");
            if(f.exists()){
                bannedList = gson.fromJson(new FileReader(f), bannedType);
            }else{
                bannedList=new ArrayList<>();
            }
            f=new File(serverDir+"/ops.json");
            if(f.exists()){
                opList = gson.fromJson(new FileReader(f), opsType);
            }else{
                opList=new ArrayList<>();
            }
            f=new File(serverDir+"/whitelist.json");
            if(f.exists()){
                whitelist = gson.fromJson(new FileReader(f), whitelistType);
            }else{
                whitelist=new ArrayList<>();
            }

        List<Player> bannerPlayers=new ArrayList<>();
        List<Player> opPlayers=new ArrayList<>();
        List<Player> whiteListPlayers=new ArrayList<>();


            // Convertir a Player
            if(!bannedList.isEmpty())
                bannerPlayers = bannedList.stream()
                    .map(b -> new Player(b.uuid, b.name))
                    .toList();
        if(!bannedList.isEmpty())
            opPlayers = opList.stream()
                    .map(o -> new Player(o.uuid, o.name))
                    .collect(Collectors.toList());
        if(!bannedList.isEmpty())
            whiteListPlayers = whitelist.stream()
                    .map(w -> new Player(w.uuid, w.name))
                    .collect(Collectors.toList());

            // Crear Banner
        this.bannedPlayers=bannedPlayers;
            this.ops = opPlayers;
            this.whiteList = whiteListPlayers;
            this.ipBans = ipBanList;
    }
    /**
     * Obtiene un objeto Player a partir de su nombre o UUID usando el mapa `uuidToName`.
     * Si no se encuentra directamente, intenta buscar el nombre sin distinguir mayúsculas.
     *
     * @param name Nombre del jugador o UUID.
     * @return El objeto Player si se encuentra; {@code null} si no se encuentra.
     */
    public Player getPlayer(String name){
        Player returned=new Player(name,this.uuidToName.get(name));
        if(returned.name==null){
            returned=null;
            for(Map.Entry<String, String> entryset:this.uuidToName.entrySet()){
                if(entryset.getValue().equalsIgnoreCase(name)){
                    return new Player(entryset.getKey(), name);
                }
            }
        }

        return returned;
    }
}
