package com.servermanager.minecraft.models;


import com.servermanager.java.JavaRuntimeManager;
import com.servermanager.minecraft.controllers.MainController;
import com.servermanager.minecraft.error.*;
import com.servermanager.minecraft.services.LoaderService;
import com.servermanager.minecraft.utils.ConfiguracionManager;
import com.servermanager.minecraft.utils.IPBan;
import com.servermanager.minecraft.utils.Log;
import com.servermanager.minecraft.utils.MinecraftVersionLoader;
import com.servermanager.minecraft.versions.MinecraftVersion;
import javafx.scene.image.Image;
import org.apache.commons.compress.archivers.dump.InvalidFormatException;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
/**
 * Representa un servidor de Minecraft gestionado por la aplicación.
 * Esta clase maneja la configuración, el estado, los mods, plugins,
 * control del proceso del servidor y la consola asociada.
 */
public class Server{

    private final MainController controller;

    @Deprecated
    private static final LoaderService servicio = new LoaderService();

    private String name;
    private String description;

    /** Configuración del servidor (server.properties) */
    private Propiedades config;

    /** Listas de baneados, ops y whitelist */
    private Banner playerLists;

    /** Control del EULA */
    private Eula eula;

    /** Mods instalados */
    private List<Mod> mods;

    /** Plugins instalados */
    private List<Plugin> plugins;

    @Deprecated
    private String selectedModLoader;              // Ej: "Forge"
    @Deprecated
    private String selectedModLoaderVersion;       // Ej: "46.0.5"
    @Deprecated
    private String selectedPluginLoader;           // Ej: "Paper"
    @Deprecated
    private String selectedPluginLoaderVersion;    // Ej: "1.21.1-42"

    /** Consola para enviar comandos y recibir salida */
    private Console console;

    /** Directorio raíz del servidor */
    private File directory;

    /** Proceso que ejecuta el servidor */
    private Process process;

    /** Registro de logs de la consola */
    private List<String> consoleLog = new ArrayList<>();

    /** Imagen/icono del servidor */
    private Image image;

    /** Lista de versiones de Minecraft disponibles */
    static List<MinecraftVersion> versiones;

    /** Configuración avanzada del servidor */
    private Configuracion configuracion;

    /**
     * Representa los posibles estados del servidor Minecraft.
     */
    public enum ServerState {
        /**
         * El servidor está detenido.
         */
        STOPPED,

        /**
         * El servidor está en proceso de inicio.
         */
        STARTING,

        /**
         * El servidor está actualmente en ejecución.
         */
        RUNNING
    }

    /**
     * Estado actual del servidor.
     */
    private ServerState serverState;
    /**
     * Carga todas las versiones de Minecraft disponibles al iniciar la clase.
     */
    static {
        try {
            versiones = MinecraftVersionLoader.loadAllVersions();
        } catch (IOException e) {

        }
    }
    /**
     * Constructor principal que inicializa el servidor basado en un directorio dado.
     * Carga configuración, mods, plugins, eula y la imagen/icono si existe.
     * @param directorio Carpeta del servidor.
     * @param controller Controlador principal para interacción UI.
     * @throws RuntimeException si ocurre un error al copiar plantilla o cargar archivos.
     */
    public Server(File directorio,MainController controller) {
        this.controller=controller;
        try {
            copyTemplate(directorio);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        this.name= directorio.getName();
        this.plugins = new ArrayList<>();
        this.mods = new ArrayList<>();
        this.config=new Propiedades(directorio.getPath()+"/server.properties");
        try {
            this.playerLists=new Banner(directorio.getPath());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.eula=new Eula(directorio.getPath()+"/eula.txt");
        this.mods=ModLoader.cargarMods(directorio);
        this.directory=directorio;
        File icon=new File(directorio.getPath()+"/icon.png");
        if(icon.exists()) {
            try {
                this.image = new Image(new FileInputStream(icon));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else{
            this.image=new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/default.png")));
        }
        this.configuracion= ConfiguracionManager.cargar(directorio.getPath());
        this.serverState=ServerState.STOPPED;
        this.console=new Console();
        this.description=this.configuracion.getDescripcion();
    }
    /**
     * Copia la plantilla base para un servidor nuevo si no existe.
     * @param directorio Directorio destino del servidor.
     * @throws InvalidFormatException Si hay error en formato de archivos de plantilla.
     * @throws NoServerDirectoryException Si el directorio destino no existe.
     * @throws NoCreatedDirectoryException Si la plantilla no está disponible.
     */
    public void copyTemplate(File directorio) throws InvalidFormatException, NoServerDirectoryException, NoCreatedDirectoryException {
        File template=new File("template/server_base");
        if(!template.exists()){
            MainController.crearTemplate(template);
        }
        cargarFromTemplate(directorio,template);
    }
    /**
     * Copia recursivamente archivos y carpetas desde origen a destino.
     * @param destino Carpeta destino.
     * @param origen Carpeta origen.
     * @throws InvalidFormatException Si formato no válido.
     * @throws NoServerDirectoryException Si el destino no existe.
     * @throws NoCreatedDirectoryException Si la carpeta origen no existe.
     */
    public static void cargarFromTemplate(File destino, File origen) throws InvalidFormatException, NoServerDirectoryException, NoCreatedDirectoryException {
        if(!destino.exists()) throw new NoServerDirectoryException();
        if(!origen.exists()) throw new NoCreatedDirectoryException("La carpeta de plantillas no se ha creado correctamente.");
        if (!destino.exists()) destino.mkdirs();

        if (origen.isDirectory()) {
            if (!destino.exists()) destino.mkdirs();

            for (String archivo : Objects.requireNonNull(origen.list())) {
                cargarFromTemplate(
                        new File(origen, archivo),
                        new File(destino, archivo)
                );
            }
        } else {
            cp(origen,destino);
        }
    }
    /**
     * Copia un archivo de origen a destino si no existe.
     * @param origen Archivo origen.
     * @param destino Archivo destino.
     */
    public static void cp(File origen,File destino){
        if(destino.exists()) return;
        try (InputStream in = new FileInputStream(origen);
             OutputStream out = new FileOutputStream(destino)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(name, server.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
    /**
     * Arranca el servidor Minecraft si no está corriendo y si el EULA está aceptado.
     * Descarga el jar si es necesario.
     * @throws EulaNotAcceptedException Si el EULA no está aceptado.
     * @throws ServerAlreadyRunningException Si el servidor ya está corriendo.
     * @throws ServerStartException Si ocurre error al iniciar.
     */
    public void start() throws EulaNotAcceptedException, ServerAlreadyRunningException, ServerStartException {
        if (isRunning()) {
            throw new ServerAlreadyRunningException();
        }

        if (!isEulaAccepted()) {
            throw new EulaNotAcceptedException();
        }

        guardar(); // guarda config antes de arrancar

        try {
            asegurarVersionDescargada();

            ProcessBuilder pb = new ProcessBuilder(getJavaExecutable(), "-jar", "server.jar", "nogui");
            pb.directory(directory);
            pb.redirectErrorStream(true);
            process = pb.start();

            this.console = new Console(process.getOutputStream());
            startReadingOutput(process);
            // Hilo para recoger logs
            Thread server=new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        console.write(line);
                        System.out.println("[SERVER " + name + "] " + line); // salida al sistema
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            server.start();

        } catch (IOException e) {
            throw new ServerStartException("Error al iniciar el servidor.", e);
        }
    }
    /**
     * Lee la salida estándar del proceso en un hilo separado y actualiza la consola.
     * @param process Proceso del servidor.
     */
    private void startReadingOutput(Process process) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    console.write("[SERVER " + getName() + "] " + line);
                }

                // Si se salió del bucle porque el stream terminó, y el proceso ya no está vivo:
                if (!process.isAlive()) {
                    onServerStoppedGracefully();
                }

            } catch (IOException e) {
                // Solo mostrar si el proceso no fue detenido manualmente
                if (process.isAlive()) {
                    e.printStackTrace(); // o log
                }else{
                    onServerStoppedGracefully();
                }
            }
        }, "Server-Output-" + getName()).start();
    }
    /**
     * Lógica a ejecutar cuando el servidor se detiene correctamente.
     * Actualiza el estado y la interfaz.
     */
    private void onServerStoppedGracefully() {
        // Aquí puedes liberar recursos, reiniciar botones, actualizar estado de UI...
        System.out.println("Servidor " + getName() + " detenido correctamente con /stop.");
        // Por ejemplo:
        this.serverState=ServerState.STOPPED;
        this.controller.updateServerControls(this);
    }

    /**
     * Guarda la configuración, EULA, mods y plugins en sus respectivos archivos.
     */
    public void guardar() {
        this.eula.guardar(this.directory.getPath()+"/eula.txt");
        guardarMods();
        guardarPlugins();
        this.config.guardar(this.directory.getPath()+"/server.properties");
        ConfiguracionManager.guardar(this.directory.getPath(),this.configuracion);
    }

    private void guardarPlugins() {
        for(Plugin p:this.plugins){
            try {
                p.guardar(this.directory.getPath()+"/plugins");
            } catch (NonExsitenceOfJar | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void guardarMods() {
        for(Mod m:this.mods){
            try {
                m.guardar(this.directory.getPath()+"/mods");
            } catch (NonExsitenceOfJar | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Envía un comando a la consola del servidor.
     * @param command Comando a enviar.
     */
    public void sendCommand(String command) {
        if (process != null && process.isAlive()) {
            console.sendCommand(command);
        }
    }

    /**
     * Detiene el servidor enviando el comando /stop y esperando a que finalice.
     * Fuerza la terminación si el servidor no responde.
     */
    public void stop() {
        if (process != null && process.isAlive()) {
            try {
                console.sendCommand("stop"); // Intenta cerrar bien el servidor
                if (!process.waitFor(10, TimeUnit.SECONDS)) {
                    System.out.println("El servidor no respondió, forzando cierre...");
                    process.destroy(); // Intento suave
                    if (!process.waitFor(5, TimeUnit.SECONDS)) {
                        process.destroyForcibly(); // Forzar si aún sigue vivo
                        System.out.println("Servidor forzado a detenerse: " + directory.getName());
                    }
                } else {
                    System.out.println("Servidor detenido correctamente: " + directory.getName());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Interrupción al detener servidor: " + directory.getName());
            }
        } else {
            System.out.println("Servidor no está en ejecución: " + directory.getName());
        }
    }


    /**
     * Descarga el archivo server.jar si no está disponible o si hay una nueva versión.
     * @throws IOException Si ocurre un error en la descarga.
     */
    private void asegurarVersionDescargada() throws IOException {
        File serverJar = new File(directory, "server.jar");

        MinecraftVersion version = getConfiguracion().getVersion();
        if (version == null) throw new IllegalStateException("Versión no configurada para el servidor.");

        String jarUrl = version.getJarUrl();
        if (jarUrl == null || jarUrl.isEmpty()) throw new IOException("No se pudo obtener la URL del JAR");

        System.out.println("Descargando " + jarUrl);

        // Descarga
        try (InputStream in = new URL(jarUrl).openStream()) {
            Files.copy(in, serverJar.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("Descarga completada para " + serverJar.getAbsolutePath());
    }
    /**
     * Obtiene la configuración actual del servidor.
     * @return La configuración.
     */
    public Configuracion getConfiguracion() {
        return configuracion;
    }
    /**
     * Obtiene el log de la consola del servidor.
     * @deprecated Este método está en desuso.
     * @return El log de la consola.
     */
    @Deprecated
    public Log Consola() {
        return new Log(consoleLog);
    }
    /**
     * Obtiene el nombre del servidor como representación en String.
     * @return El nombre del servidor.
     */
    @Override
    public String toString() {
        return name; // o como se llame el atributo del nombre del servidor
    }
    /**
     * Obtiene la carpeta donde está almacenado el servidor.
     * @return La carpeta del servidor.
     */
    public File getFolder() {
        return this.directory;
    }

    /**
     * Establece si se ha aceptado el EULA.
     * @param b true si se acepta, false si no.
     */
    public void setEulaAccepted(boolean b) {
        eula.setAceptar(b);
    }
    /**
     * Obtiene la consola del servidor.
     * @return La consola.
     */
    public Console getConsole() {
        return console;
    }
    /**
     * Guarda las propiedades del servidor en el archivo correspondiente.
     */
    public void saveProperties() {
        this.config.guardar(this.directory.getPath()+"/server.properties");
    }
    /**
     * Obtiene el proceso del servidor en ejecución.
     * @return El proceso.
     */
    public Process getProcess() {
        return this.process;
    }
    /**
     * Añade un plugin al servidor.
     * @param plugin El plugin a añadir.
     */
    public void addPlugin(Plugin plugin) {
        this.plugins.add(plugin);
    }
    /**
     * Elimina un plugin del servidor.
     * @param pluginName El plugin a eliminar.
     */
    public void removePlugin(Plugin pluginName) {
        this.plugins.remove(pluginName);
    }
    /**
     * Elimina un mod del servidor.
     * @param modName El mod a eliminar.
     */
    public void removeMod(Mod modName) {
        this.mods.remove(modName);
    }
    /**
     * Añade un mod al servidor.
     * @param mod El mod a añadir.
     */
    public void addMod(Mod mod) {
        this.mods.add(mod);
    }
    /**
     * Elimina un operador de la lista de ops.
     * @param op El jugador operador a eliminar.
     */
    public void removeOp(Player op) {
        this.playerLists.ops.remove(op);
    }
    /**
     * Añade un operador a la lista de ops.
     * @param op El nombre del jugador operador a añadir.
     */
    public void addOp(String op) {
        this.playerLists.ops.add(playerLists.getPlayer(op));
    }
    /**
     * Elimina un bloqueo de IP de la lista de baneos.
     * @param ip La IP baneada a eliminar.
     */
    public void unbanIp(IPBan ip) {
        this.playerLists.ipBans.remove(ip);
    }
    /**
     * Añade una IP a la lista de baneos.
     * @param ip La IP a banear.
     */
    public void banIp(String ip) {
        this.playerLists.ipBans.add(new IPBan().setIP(ip));
    }
    /**
     * Elimina un jugador baneado de la lista.
     * @param player El jugador a desbanear.
     */
    public void unbanPlayer(Player player) {
        this.playerLists.bannedPlayers.remove(player);
    }
    /**
     * Añade un jugador a la lista de baneados.
     * @param player El nombre del jugador a banear.
     */
    public void banPlayer(String player) {
        this.playerLists.bannedPlayers.add(playerLists.getPlayer(player));
    }
    /**
     * Añade un jugador a la whitelist.
     * @param player El nombre del jugador a añadir.
     */
    public void addToWhitelist(String player) {
        this.playerLists.whiteList.add(playerLists.getPlayer(player));
    }
    /**
     * Elimina un jugador de la whitelist.
     * @param player El jugador a eliminar.
     */
    public void removeFromWhitelist(Player player) {
        this.playerLists.whiteList.remove(player);
    }
    /**
     * Obtiene el nombre del servidor.
     * @return El nombre.
     */
    public String getName() {
        return name;
    }
    /**
     * Establece el nombre del servidor.
     * @param name El nuevo nombre.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Obtiene la lista de mods instalados.
     * @return La lista de mods.
     */
    public List<Mod> getMods() {
        if(mods == null) return new ArrayList<>();
        return mods;
    }
    /**
     * Obtiene la lista de plugins instalados.
     * @return La lista de plugins.
     */
    public List<Plugin> getPlugins() {
        if(plugins == null) return new ArrayList<>();
        return plugins;
    }
    /**
     * Obtiene la descripción del servidor.
     * @return La descripción.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Indica si se ha aceptado el EULA.
     * @return true si aceptado, false en caso contrario.
     */
    public boolean isEulaAccepted() {
        return eula.isAceptar();
    }
    /**
     * Obtiene la lista de operadores.
     * @return La lista de operadores.
     */
    public List<Player> getOps() {
        if(playerLists.ops == null) return new ArrayList<>();
        return this.playerLists.ops;
    }
    /**
     * Obtiene la lista de jugadores baneados.
     * @return La lista de baneados.
     */
    public List<Player> getBannedPlayers() {
        if(playerLists.bannedPlayers == null) return new ArrayList<>();
        return this.playerLists.bannedPlayers;
    }
    /**
     * Obtiene la lista de IPs baneadas.
     * @return La lista de IPs baneadas.
     */
    public List<IPBan> getBannedIps() {
        if(playerLists.ipBans == null) return new ArrayList<>();
        return this.playerLists.ipBans;
    }
    /**
     * Obtiene la whitelist de jugadores.
     * @return La lista blanca.
     */
    public List<Player> getWhitelist() {
        if(playerLists.whiteList == null) return new ArrayList<>();
        return this.playerLists.whiteList;
    }
    /**
     * Establece la carpeta donde está ubicado el servidor.
     * @param newFolder Nueva carpeta.
     */
    public void setFolder(File newFolder) {
        this.directory=newFolder;
    }
    /**
     * Obtiene el banner del servidor.
     * @return El banner.
     */
    public Banner getBanner() {
        return this.playerLists;
    }
    /**
     * Abre el log completo (implementación vacía).
     */
    public void openFullLog() {
    }
    /**
     * Obtiene la imagen asociada al servidor.
     * @return La imagen.
     */
    public Image getImage() {
        return this.image;
    }
    /**
     * Establece la imagen asociada al servidor.
     * @param image La nueva imagen.
     */
    public void setImage(Image image) {
        this.image=image;
    }
    /**
     * Establece la descripción del servidor y la actualiza en la configuración.
     * @param description Nueva descripción.
     */
    public void setDescription(String description) {
        this.description = description;
        this.configuracion.setDescripcion(description);
    }
    /**
     * Obtiene la versión de Minecraft configurada para el servidor.
     * @return La versión.
     */
    public MinecraftVersion getVersion() {
        return configuracion.getVersion();
    }
    /**
     * Indica si el servidor está en ejecución.
     * @return true si está corriendo, false en caso contrario.
     */
    public boolean isRunning() {
        return process != null && process.isAlive();
    }
    /**
     * Obtiene las propiedades de configuración del servidor.
     * @return Las propiedades.
     */
    public Propiedades getConfig() {
        return config;
    }
    /**
     * Obtiene el estado actual del servidor.
     * @return El estado.
     */
    public ServerState getState() {
        return serverState;
    }
    /**
     * Establece el estado actual del servidor.
     * @param serverState Nuevo estado.
     */
    public void setState(ServerState serverState) {
        this.serverState=serverState;
    }
    /**
     * Obtiene el ejecutable de Java necesario según la versión del servidor.
     * @return Ruta absoluta al ejecutable Java.
     * @throws RuntimeException si no se puede obtener la ruta.
     */
    public String getJavaExecutable() {
        int javaVersion = getVersion().getRequiredJavaVersion(); // p.e. 21

        try {
            return JavaRuntimeManager.getJavaExecutable(javaVersion).toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo obtener el ejecutable de Java para JDK " + javaVersion, e);
        }
    }
}

