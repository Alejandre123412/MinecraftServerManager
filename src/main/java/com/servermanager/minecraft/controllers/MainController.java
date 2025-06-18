package com.servermanager.minecraft.controllers;

import com.servermanager.minecraft.error.*;
import com.servermanager.minecraft.models.*;
import com.servermanager.minecraft.utils.IPBan;
import com.servermanager.minecraft.utils.MinecraftVersionLoader;
import com.servermanager.minecraft.versions.MinecraftVersion;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Controlador principal para la interfaz gráfica de Server Manager.
 * <p>
 * Esta clase maneja las interacciones entre el usuario y la vista principal,
 * representada en el archivo FXML asociado. Administra la configuración,
 * control, visualización y administración de múltiples servidores Minecraft.
 * </p>
 */
public class MainController {

        // ───────────────────────────────────────────────────────
        // Sección: Configuración de Loaders
        // ───────────────────────────────────────────────────────

        /** ComboBox para seleccionar el tipo de Mod Loader (Forge, Fabric, etc.). */
        @FXML private ComboBox modLoaderComboBox;

        /** ComboBox para seleccionar la versión del Mod Loader. */
        @FXML private ComboBox modLoaderVersionComboBox;

        /** ComboBox para seleccionar el tipo de Plugin Loader (Spigot, Paper, etc.). */
        @FXML private ComboBox pluginLoaderComboBox;

        /** ComboBox para seleccionar la versión del Plugin Loader. */
        @FXML private ComboBox pluginLoaderVersionComboBox;

        // ───────────────────────────────────────────────────────
        // Sección: Tema e Interfaz Principal
        // ───────────────────────────────────────────────────────

        /** Selector de tema (oscuro, claro, etc.). */
        @FXML private ComboBox<String> themeSelector;

        /** Pestañas principales de la aplicación. */
        @FXML private TabPane mainTabPane;

        // ───────────────────────────────────────────────────────
        // Sección: Información del Servidor
        // ───────────────────────────────────────────────────────

        /** Botón para cambiar el icono del servidor. */
        @FXML private Button serverIconButton;

        /** Vista previa del icono del servidor. */
        @FXML private ImageView serverIconView;

        /** Etiqueta que muestra el nombre actual del servidor. */
        @FXML private Label serverNameLabel;

        /** ComboBox para seleccionar la versión de Minecraft. */
        @FXML private ComboBox<MinecraftVersion> versionComboBox;

        /** Área de texto para la descripción del servidor. */
        @FXML private TextArea serverDescriptionArea;

        // ───────────────────────────────────────────────────────
        // Sección: Configuración Avanzada
        // ───────────────────────────────────────────────────────

        /** ScrollPane que contiene las propiedades del servidor. */
        @FXML private ScrollPane scrollPane;

        /** Contenedor de propiedades configurables. */
        @FXML private VBox scrollContent;

        /** Grid para visualizar propiedades de configuración del servidor. */
        @FXML private GridPane propertiesGrid;

        /** Checkbox para aceptar el EULA de Mojang. */
        @FXML private CheckBox eulaCheckBox;

        /** WebView para visualizar el contenido del EULA. */
        @FXML private WebView eulaWebView;

        // ───────────────────────────────────────────────────────
        // Sección: Listas de Jugadores
        // ───────────────────────────────────────────────────────

        /** Lista de jugadores baneados. */
        @FXML private ListView<Player> bannedPlayersView;

        /** Campo para ingresar un nombre de jugador a banear. */
        @FXML private TextField bannedPlayerInput;

        /** Lista de IPs baneadas. */
        @FXML private ListView<IPBan> bannedIpsView;

        /** Campo para ingresar una IP a banear. */
        @FXML private TextField bannedIPInput;

        /** Lista de operadores del servidor. */
        @FXML private ListView<Player> opsView;

        /** Campo para ingresar un nombre de operador. */
        @FXML private TextField opInput;

        /** Lista de jugadores recientes. */
        @FXML private ListView<Player> recentPlayersView;

        /** Lista de jugadores en la whitelist. */
        @FXML private ListView<Player> whitelistView;

        /** Campo para ingresar un jugador a la whitelist. */
        @FXML private TextField whitelistInput;

        // ───────────────────────────────────────────────────────
        // Sección: Soporte para Mods y Plugins
        // ───────────────────────────────────────────────────────

        /** Checkbox para habilitar el uso de Mods. */
        @FXML private CheckBox enableModsCheckbox;

        /** Checkbox para habilitar el uso de Plugins. */
        @FXML private CheckBox enablePluginsCheckbox;

        /** Lista de mods cargados. */
        @FXML private ListView<Mod> modsListView;

        /** Lista de plugins cargados. */
        @FXML private ListView<Plugin> pluginsListView;

        // ───────────────────────────────────────────────────────
        // Sección: Consola
        // ───────────────────────────────────────────────────────

        /** Panel principal que contiene la consola. */
        @FXML private AnchorPane consolePane;

        /** ScrollPane que contiene la consola. */
        @FXML private ScrollPane scrollPaneConsole;

        /** Área de texto de salida de la consola del servidor. */
        @FXML private TextArea consoleArea;

        /** Campo para enviar comandos a la consola del servidor. */
        @FXML private TextField commandInput;

        // ───────────────────────────────────────────────────────
        // Sección: Controles de Servidor
        // ───────────────────────────────────────────────────────

        /** Botón para iniciar el servidor. */
        @FXML private Button startServerButton;

        /** Botón para detener el servidor. */
        @FXML private Button stopServerButton;

        /** Botón para reiniciar el servidor. */
        @FXML private Button restartServerButton;

        // ───────────────────────────────────────────────────────
        // Sección: Estado Interno del Controlador
        // ───────────────────────────────────────────────────────

        /** Lista de servidores disponibles. */
        List<Server> servers;

        /** Lista visual de servidores en la interfaz. */
        @FXML private ListView<Server> serverList;

        /** Referencia al servidor actualmente seleccionado. */
        private Server selectedServer;



    /**
     * Inicializa los elementos de la interfaz gráfica (FXML).
     *
     * - Carga temas disponibles en el selector.
     * - Configura listeners de eventos para la selección de versión de Minecraft.
     * - Carga las versiones de Minecraft en segundo plano.
     * - Una vez cargadas las versiones, carga los servidores y permite seleccionar uno.
     */
    @FXML
    public void initialize() {
        // 1. Cargar temas rápido (UI thread)
        ObservableList<String> listaTemas = FXCollections.observableArrayList();
        for (File archivo : cargarTemas()) {
            listaTemas.add(archivo.getName().replace(".css", ""));
        }
        themeSelector.setItems(listaTemas);

        // 2. Listener para guardar versión
        versionComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (selectedServer != null && newVal != null) {
                selectedServer.getConfiguracion().setVersion(newVal);
                selectedServer.guardar();
            }
        });

        // 3. Cargar versiones en background
        Task<List<MinecraftVersion>> loadVersionsTask = new Task<>() {
            @Override
            protected List<MinecraftVersion> call() throws Exception {
                List<MinecraftVersion> allVersions = MinecraftVersionLoader.loadAllVersions();
                return allVersions.stream()
                        .filter(v -> v.getVersion() != null && v.getVersion().matches("1\\.\\d+\\.\\d+"))
                        .collect(Collectors.toList());
            }
        };

        loadVersionsTask.setOnSucceeded(event -> {
            List<MinecraftVersion> soloReleases = loadVersionsTask.getValue();
            versionComboBox.setItems(FXCollections.observableArrayList(soloReleases));

            // 4. Ahora que las versiones están cargadas, cargamos los servidores
            serverList.getItems().addAll(servers);
            SaveServerManager.addServers(servers);
            // 5. Listener para selección de servidor
            serverList.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
                selectedServer = nuevo;
                if (nuevo != null) {
                    MinecraftVersion versionActual = nuevo.getVersion();
                    if (versionActual != null) {
                        for (MinecraftVersion v : versionComboBox.getItems()) {
                            if (v.getVersion().equals(versionActual.getVersion())) {
                                versionComboBox.setValue(v);
                                break;
                            }
                        }
                    } else {
                        versionComboBox.setValue(null);
                    }

                    serverNameLabel.setText(nuevo.getName());
                    serverDescriptionArea.setText(nuevo.getDescription());
                    eulaCheckBox.setSelected(nuevo.isEulaAccepted());
                    loadConfigToGrid(nuevo.getConfig(), propertiesGrid);

                    modsListView.getItems().setAll(nuevo.getMods() != null ? nuevo.getMods() : List.of());
                    pluginsListView.getItems().setAll(nuevo.getPlugins() != null ? nuevo.getPlugins() : List.of());
                    opsView.getItems().setAll(nuevo.getOps() != null ? nuevo.getOps() : List.of());
                    bannedPlayersView.getItems().setAll(nuevo.getBannedPlayers() != null ? nuevo.getBannedPlayers() : List.of());
                    bannedIpsView.getItems().setAll(nuevo.getBannedIps() != null ? nuevo.getBannedIps() : List.of());
                    whitelistView.getItems().setAll(nuevo.getWhitelist() != null ? nuevo.getWhitelist() : List.of());
                    cargarImagen();
                    consoleArea.setEditable(false);consoleArea.setWrapText(true);
                    consoleArea.clear();
                    for (String line : nuevo.getConsole().getLog()) {
                        consoleArea.appendText(line + "\n");
                    }
                    scrollPaneConsole.setVvalue(1.0);
                }
                updateServerControls(nuevo);
            });
        });

        loadVersionsTask.setOnFailed(event -> {
            loadVersionsTask.getException().printStackTrace();
            // Aquí podrías mostrar alerta
        });

        new Thread(loadVersionsTask).start();
    }
    /**
     * Constructor del controlador principal. Inicializa la lista de servidores
     * cargándolos desde el directorio local "servers".
     */
    public MainController() {
        this.servers=new ArrayList<>();
        cargarServidores();
    }
    /**
     * Crea un nodo de entrada (editor) para una propiedad específica según su tipo.
     *
     * @param field Campo de la clase Propiedades.
     * @param value Valor actual del campo.
     * @return Nodo JavaFX correspondiente para editar el valor.
     */
    private Node createEditor(Field field, Object value) {
        String name = field.getName();

        if (field.getType() == boolean.class) {
            CheckBox cb = new CheckBox();
            cb.setSelected((boolean) value);
            cb.setUserData(name);
            return cb;
        }

        if (field.getType() == int.class) {
            TextField tf = new TextField(String.valueOf(value));
            tf.setTextFormatter(new TextFormatter<>(c ->
                    c.getControlNewText().matches("-?\\d*") ? c : null));
            tf.setUserData(name);
            return tf;
        }

        if (field.getType() == String.class) {
            List<String> opciones = switch (name) {
                case "gamemode" -> List.of("survival", "creative", "adventure", "spectator");
                case "difficulty" -> List.of("peaceful", "easy", "normal", "hard");
                default -> null;
            };

            if (opciones != null) {
                ComboBox<String> combo = new ComboBox<>();
                combo.getItems().addAll(opciones);
                combo.setValue((String) value);
                combo.setUserData(name);
                return combo;
            } else {
                TextField tf = new TextField((String) value);
                tf.setUserData(name);
                return tf;
            }
        }

        if (field.getType().isArray() && field.getType().getComponentType() == String.class) {
            TextField tf = new TextField(String.join(",", (String[]) value));
            tf.setUserData(name);
            return tf;
        }

        return new Label("Tipo no soportado");
    }
    /**
     * Carga los valores de configuración del objeto Propiedades en el GridPane,
     * generando dinámicamente editores para cada campo.
     *
     * @param config Objeto de configuración del servidor.
     * @param gridPane Contenedor donde se mostrarán los campos editables.
     */
    public void loadConfigToGrid(Propiedades config, GridPane gridPane) {
        gridPane.getChildren().clear();
        int row = 0;

        for (Field field : config.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(config);
                Label label = new Label(field.getName());
                Node input = createEditor(field, value);
                gridPane.addRow(row++, label, input);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Guarda los valores de los campos del GridPane en el objeto Propiedades.
     *
     * @param config Objeto donde se almacenarán los nuevos valores.
     * @param gridPane Contenedor que contiene los editores de configuración.
     */
    public void saveGridToConfig(Propiedades config, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == 1) {
                String name = (String) node.getUserData();
                try {
                    Field field = config.getClass().getDeclaredField(name);
                    field.setAccessible(true);

                    if (node instanceof CheckBox cb) {
                        field.setBoolean(config, cb.isSelected());
                    } else if (node instanceof TextField tf) {
                        if (field.getType() == int.class) {
                            field.setInt(config, Integer.parseInt(tf.getText()));
                        } else if (field.getType() == String.class) {
                            field.set(config, tf.getText());
                        } else if (field.getType().isArray()) {
                            field.set(config, tf.getText().split("\\s*,\\s*"));
                        }
                    } else if (node instanceof ComboBox<?> combo) {
                        field.set(config, combo.getValue().toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Carga todos los servidores existentes en el directorio "servers".
     * Si la carpeta no existe, intenta crearla. Lanza excepción si falla.
     */
    private void cargarServidores() {
        File serversDir= new File("servers");
        boolean creada=false;
        boolean noExiste=!serversDir.exists() || !serversDir.isDirectory();
        if(noExiste) creada=serversDir.mkdir();
        if(noExiste&&!creada) throw new NoCreatedDirectoryException("Problemas al crear la capeta \"servers\"");

        File[] subdirs = serversDir.listFiles(File::isDirectory);
        if (subdirs != null) {
            for (File subdir : subdirs) {
                System.out.println("Servidor encontrado: " + subdir.getName());
                this.servers.add(new Server(subdir,this));
            }
        }
    }
    /**
     * Acción invocada al presionar el botón "Nuevo Servidor".
     *
     * - Muestra un diálogo para ingresar el nombre del nuevo servidor.
     * - Verifica si ya existe un servidor con ese nombre.
     * - Crea la carpeta del nuevo servidor y copia una plantilla base.
     * - Agrega el nuevo servidor a la lista y selecciona automáticamente.
     *
     * @param actionEvent Evento de acción asociado al botón.
     */
    public void onNewServerClicked(ActionEvent actionEvent) {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Nuevo Servidor");
        nameDialog.setHeaderText("Crear nuevo servidor");
        nameDialog.setContentText("Nombre del servidor:");
        nameDialog.showAndWait().ifPresent(nombre -> {
            if (nombre.trim().isEmpty()) return;

            File folder = new File("servers/" + nombre);
            if (folder.exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ya existe un servidor con ese nombre.");
                alert.show();
                return;
            }

            if (folder.mkdir()) {
                try {
                    copyTemplate(folder);
                } catch (URISyntaxException | IOException e) {
                    throw new RuntimeException(e);
                }
                Server nuevo = new Server(folder,this);
                servers.add(nuevo);
                serverList.getItems().add(nuevo);
                serverList.getSelectionModel().select(nuevo);

                Alert success = new Alert(Alert.AlertType.INFORMATION, "Servidor creado correctamente.");
                success.show();
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR, "No se pudo crear la carpeta del servidor.");
                error.show();
            }
        });
    }
    /**
     * Copia los archivos base de un servidor desde una plantilla al nuevo directorio.
     *
     * @param directory Carpeta destino del nuevo servidor.
     * @throws URISyntaxException Si hay problemas con la URI del archivo.
     * @throws IOException Si ocurre un error de entrada/salida durante la copia.
     */
    public static void copyTemplate(File directory) throws URISyntaxException, IOException {
        File template=new File("template/server_base");
        if(!template.exists()){
            crearTemplate(template);
        }
        Server.cargarFromTemplate(template,directory);
    }
    /**
     * Crea la estructura de archivos básica para la plantilla de un servidor,
     * incluyendo archivos como server.properties, eula.txt, y listas vacías de jugadores.
     *
     * @param template Directorio donde se generará la plantilla.
     */
    public static void crearTemplate(File template) {
        // 1. Crear carpetas
        if (!template.mkdirs()) return;

        // 2. Crear archivos base con contenido mínimo
        try {
            // server.properties
            File serverProps = new File(template, "server.properties");
            try (FileWriter writer = new FileWriter(serverProps)) {
                writer.write("""
                        #Minecraft server properties
                        #Thu May 15 13:04:19 CEST 2025
                        accepts-transfers=false
                        allow-flight=false
                        allow-nether=true
                        broadcast-console-to-ops=true
                        broadcast-rcon-to-ops=true
                        bug-report-link=
                        difficulty=easy
                        enable-command-block=false
                        enable-jmx-monitoring=false
                        enable-query=false
                        enable-rcon=false
                        enable-status=true
                        enforce-secure-profile=true
                        enforce-whitelist=false
                        entity-broadcast-range-percentage=100
                        force-gamemode=false
                        function-permission-level=2
                        gamemode=survival
                        generate-structures=true
                        generator-settings={}
                        hardcore=false
                        hide-online-players=false
                        initial-disabled-packs=
                        initial-enabled-packs=vanilla
                        level-name=world
                        level-seed=
                        level-type=minecraft\\:normal
                        log-ips=true
                        max-chained-neighbor-updates=1000000
                        max-players=20
                        max-tick-time=60000
                        max-world-size=29999984
                        motd=A Minecraft Server
                        network-compression-threshold=256
                        online-mode=true
                        op-permission-level=4
                        pause-when-empty-seconds=60
                        player-idle-timeout=0
                        prevent-proxy-connections=false
                        pvp=true
                        query.port=25565
                        rate-limit=0
                        rcon.password=
                        rcon.port=25575
                        region-file-compression=deflate
                        require-resource-pack=false
                        resource-pack=
                        resource-pack-id=
                        resource-pack-prompt=
                        resource-pack-sha1=
                        server-ip=
                        server-port=25565
                        simulation-distance=10
                        spawn-monsters=true
                        spawn-protection=16
                        sync-chunk-writes=true
                        text-filtering-config=
                        text-filtering-version=0
                        use-native-transport=true
                        view-distance=10
                        white-list=false
                """);
            }

            // eula.txt
            File eula = new File(template, "eula.txt");
            try (FileWriter writer = new FileWriter(eula)) {
                writer.write("""
                        #By changing the setting below to TRUE you are indicating your agreement to our EULA (https://aka.ms/MinecraftEULA).
                        #Thu May 15 13:04:20 CEST 2025
                        eula=false
                        """);
            }

            File band=new File(template,"banned-ips.json");
            try (FileWriter writer = new FileWriter(band)) {
                writer.write("[]");
            }
            band=new File(template,"banned-players.json");
            try (FileWriter writer = new FileWriter(band)) {
                writer.write("[]");
            }
            band=new File(template,"ops.json");
            try (FileWriter writer = new FileWriter(band)) {
                writer.write("[]");
            }
            band=new File(template,"usercache.json");
            try (FileWriter writer = new FileWriter(band)) {
                writer.write("[]");
            }
            band=new File(template,"whitelist.json");
            try (FileWriter writer = new FileWriter(band)) {
                writer.write("[]");
            }

            // Otros archivos si quieres agregar más...

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Evento invocado al hacer clic en el botón de cambio de icono del servidor.
     *
     * - Abre un diálogo para seleccionar una imagen (PNG o JPG).
     * - Guarda la imagen seleccionada como "icon.png" en la carpeta del servidor.
     * - Recarga el icono mostrado en la interfaz.
     *
     * @param actionEvent Evento de acción del botón.
     * @throws IOException Si ocurre un error al copiar la imagen.
     */
    @FXML
    public void onChangeIconClicked(ActionEvent actionEvent) throws IOException {
        if (selectedServer == null) return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona una imagen para el servidor");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg"));

        Stage stage=(Stage) serverIconButton.getScene().getWindow();
        File imagenSeleccionada = fileChooser.showOpenDialog(stage);
        if (imagenSeleccionada != null) {
            guardarIconoServidor(imagenSeleccionada, selectedServer.getFolder());
        }
        cargarImagen();
    }
    /**
     * Carga y muestra el icono del servidor actualmente seleccionado en el ImageView.
     */
    private void cargarImagen() {
        Image imagen = selectedServer.getImage();
        serverIconView.setImage(imagen);
    }
    /**
     * Guarda la imagen seleccionada como el nuevo icono del servidor.
     *
     * - Copia la imagen al archivo "icon.png" en la carpeta del servidor.
     * - Actualiza la imagen en el objeto Server.
     *
     * @param imagenSeleccionada Archivo de imagen elegido por el usuario.
     * @param carpetaServidor Carpeta raíz del servidor.
     * @throws IOException Si ocurre un error al copiar la imagen.
     */
    public void guardarIconoServidor(File imagenSeleccionada, File carpetaServidor) throws IOException {
        if (!imagenSeleccionada.exists()) return;

        File destino = new File(carpetaServidor, "icon.png");
        Files.copy(imagenSeleccionada.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
        selectedServer.setImage(new Image(new FileInputStream(destino)));
    }
    /**
     * Evento invocado al hacer clic en el botón para cambiar el nombre del servidor.
     *
     * - Abre un diálogo para ingresar un nuevo nombre.
     * - Verifica si el nuevo nombre no está vacío ni repetido.
     * - Renombra la carpeta del servidor y actualiza la lista.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void onChangeNameClicked(ActionEvent actionEvent) {
        if (selectedServer == null) return;

        TextInputDialog dialog = new TextInputDialog(selectedServer.getName());
        dialog.setTitle("Cambiar Nombre");
        dialog.setHeaderText("Nuevo nombre para el servidor:");
        dialog.setContentText("Nombre:");
        dialog.showAndWait().ifPresent(newName -> {
            if (newName.trim().isEmpty()) return;

            File newFolder = new File("servers/" + newName);
            if (newFolder.exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ya existe un servidor con ese nombre.");
                alert.show();
                return;
            }

            if (selectedServer.getFolder().renameTo(newFolder)) {
                selectedServer.setFolder(newFolder);
                serverList.refresh(); // Actualizar la lista
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR, "No se pudo renombrar la carpeta del servidor.");
                error.show();
            }
        });
    }
    /**
     * Guarda las propiedades del servidor actualmente seleccionado.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void onSaveProperties(ActionEvent actionEvent) {
        if (selectedServer == null) return;
        selectedServer.saveProperties(); // Supuesto método
    }
    /**
     * Marca la EULA como aceptada para el servidor seleccionado.
     *
     * @param actionEvent Evento de acción del checkbox.
     */
    public void onEulaCheckboxChanged(ActionEvent actionEvent) {
        if (selectedServer == null) return;
        selectedServer.setEulaAccepted(true); // o leer el estado desde eulaCheckBox
    }
    /**
     * Añade un jugador a la whitelist del servidor.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void onAddWhitelist(ActionEvent actionEvent) {
        String player = whitelistInput.getText().trim();
        if (!player.isEmpty() && selectedServer != null) {
            selectedServer.addToWhitelist(player);
            whitelistView.getItems().add(selectedServer.getBanner().getPlayer(player));
            whitelistInput.clear();
        }
    }
    /**
     * Elimina el jugador seleccionado de la whitelist del servidor.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void onRemoveWhitelist(ActionEvent actionEvent) {
        Player player = whitelistView.getSelectionModel().getSelectedItem();
        if (player != null && selectedServer != null) {
            selectedServer.removeFromWhitelist(player);
            whitelistView.getItems().remove(player);
        }
    }
    /**
     * Añade un jugador a la lista de baneados del servidor.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void onAddBannedPlayer(ActionEvent actionEvent) {
        String player = bannedPlayerInput.getText().trim();
        if (!player.isEmpty() && selectedServer != null) {
            selectedServer.banPlayer(player);
            bannedPlayersView.getItems().add(selectedServer.getBanner().getPlayer(player));
            bannedPlayerInput.clear();
        }
    }
    /**
     * Elimina un jugador de la lista de baneados del servidor.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void onRemoveBannedPlayer(ActionEvent actionEvent) {
        Player player = bannedPlayersView.getSelectionModel().getSelectedItem();
        if (player != null && selectedServer != null) {
            selectedServer.unbanPlayer(player);
            bannedPlayersView.getItems().remove(player);
        }
    }
    /**
     * Añade una IP a la lista de IPs baneadas del servidor.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void onAddBannedIp(ActionEvent actionEvent) {
        String ip = bannedIPInput.getText().trim();
        if (!ip.isEmpty() && selectedServer != null) {
            selectedServer.banIp(ip);
            bannedIpsView.getItems().add(new IPBan().setIP(ip));
            bannedIPInput.clear();
        }
    }
    /**
     * Elimina una IP de la lista de IPs baneadas del servidor.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void onRemoveBannedIp(ActionEvent actionEvent) {
        IPBan ip = bannedIpsView.getSelectionModel().getSelectedItem();
        if (ip != null && selectedServer != null) {
            selectedServer.unbanIp(ip);
            bannedIpsView.getItems().remove(ip);
        }
    }
    /**
     * Añade un operador (op) al servidor.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void onAddOp(ActionEvent actionEvent) {
        String op = opInput.getText().trim();
        if (!op.isEmpty() && selectedServer != null) {
            selectedServer.addOp(op);
            opsView.getItems().add(selectedServer.getBanner().getPlayer(op));
            opInput.clear();
        }
    }
    /**
     * Elimina un operador (op) del servidor.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void onRemoveOp(ActionEvent actionEvent) {
        Player op = opsView.getSelectionModel().getSelectedItem();
        if (op != null && selectedServer != null) {
            selectedServer.removeOp(op);
            opsView.getItems().remove(op);
        }
    }
    /**
     * Añade un mod al servidor seleccionado desde un archivo .jar.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void AddMod(ActionEvent actionEvent) {
        if (selectedServer != null) {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos JAR", "*.jar"));
            File mod = fc.showOpenDialog(startServerButton.getScene().getWindow());
            Mod m=new Mod(mod);
            if (mod != null) {
                selectedServer.addMod(m);
                modsListView.getItems().add(m);
            }
        }
    }
    /**
     * Elimina el mod seleccionado de la lista del servidor.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void RemoveMod(ActionEvent actionEvent) {
        Mod modName = modsListView.getSelectionModel().getSelectedItem();
        if (modName != null && selectedServer != null) {
            selectedServer.removeMod(modName);
            modsListView.getItems().remove(modName);
        }
    }
    /**
     * Añade un plugin al servidor desde un archivo .jar.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void handleAddPlugin(ActionEvent actionEvent) {
        if (selectedServer != null) {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos JAR", "*.jar"));
            File plugin = fc.showOpenDialog(startServerButton.getScene().getWindow());
            Plugin p=new Plugin(plugin.getPath());
            if (plugin != null) {
                selectedServer.addPlugin(p);
                pluginsListView.getItems().add(p);
            }
        }
    }
    /**
     * Elimina el plugin seleccionado de la lista del servidor.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void handleRemovePlugin(ActionEvent actionEvent) {
        Plugin pluginName = pluginsListView.getSelectionModel().getSelectedItem();
        if (pluginName != null && selectedServer != null) {
            selectedServer.removePlugin(pluginName);
            pluginsListView.getItems().remove(pluginName);
        }
    }

    /**
     * Maneja la pulsación de teclas en el campo de comandos.
     *
     * Si se presiona ENTER, envía el comando actual.
     *
     * @param keyEvent Evento de teclado.
     */
    public void onCommandKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case ENTER -> onSendCommand(null);
        }
    }
    /**
     * Envía el comando ingresado en el campo de texto al servidor.
     *
     * @param actionEvent Evento de acción del botón o ENTER.
     */
    public void onSendCommand(ActionEvent actionEvent) {
        String command = commandInput.getText().trim();
        if (!command.isEmpty() && selectedServer != null) {
            selectedServer.sendCommand(command);
            commandInput.clear();
        }
    }
    /**
     * Abre el archivo de log completo del servidor seleccionado.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void abrirLogCompleto(ActionEvent actionEvent) {
        if (selectedServer != null) {
            selectedServer.openFullLog();
        }
    }

    /**
     * Actualiza el estado de los botones (start/stop/restart) según el estado del servidor.
     *
     * @param server Instancia del servidor cuyo estado se evalúa.
     */
    public void updateServerControls(Server server) {
        switch (server.getState()) {
            case STOPPED:
                startServerButton.setDisable(false);
                stopServerButton.setDisable(true);
                restartServerButton.setDisable(true);
                break;
            case STARTING:
                startServerButton.setDisable(true);
                stopServerButton.setDisable(false);
                restartServerButton.setDisable(true);
                break;
            case RUNNING:
                startServerButton.setDisable(true);
                stopServerButton.setDisable(false);
                restartServerButton.setDisable(false);
                break;
        }
    }
    /**
     * Inicia el servidor seleccionado y actualiza la interfaz.
     *
     * Realiza validaciones sobre el estado del servidor y EULA.
     * Inicia un hilo para mostrar la consola en tiempo real.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void onStartServerClicked(ActionEvent actionEvent) {
        selectedServer.setEulaAccepted(eulaCheckBox.isSelected());
        ServerManager.addServer(selectedServer);
        String name=selectedServer.getName();
        Server running=ServerManager.getServer(name);

        if (running == null || running.isRunning()){
            ServerManager.removeServer(name);
            return;
        }

        running.setState(Server.ServerState.STARTING);
        updateServerControls(running);

        try {
            running.start();

            running.setState(Server.ServerState.RUNNING);
            updateServerControls(running);

            startConsoleLogThread(); // mueve la lógica del hilo aquí si quieres modularidad

        } catch (EulaNotAcceptedException | ServerAlreadyRunningException e) {
            mostrarError(e.getMessage());
            running.setState(Server.ServerState.STOPPED);
            updateServerControls(running);

        } catch (ServerStartException e) {
            e.printStackTrace();
            mostrarError("Fallo grave al iniciar el servidor:\n" + e.getMessage());
            running.setState(Server.ServerState.STOPPED);
            updateServerControls(running);
        }
    }
    /**
     * Muestra una alerta de error con el mensaje proporcionado en la interfaz.
     *
     * @param mensaje Texto del error.
     */
    private void mostrarError(String mensaje) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al iniciar el servidor");
            alert.setContentText(mensaje);
            alert.showAndWait();
        });
    }

    /**
     * Inicia un hilo que actualiza periódicamente el área de consola
     * con el log generado por el servidor.
     */
    private void startConsoleLogThread() {
        new Thread(() -> {
            List<String> previousLog = new ArrayList<>();
            while (selectedServer.getProcess() != null && selectedServer.getProcess().isAlive()) {
                List<String> currentLog = selectedServer.getConsole().getLog();
                if (currentLog.size() > previousLog.size()) {
                    List<String> newLines = currentLog.subList(previousLog.size(), currentLog.size());
                    Platform.runLater(() -> {
                        for (String line : newLines) {
                            consoleArea.appendText(line + "\n");
                        }
                        scrollPaneConsole.setVvalue(1.0);
                    });
                    previousLog = new ArrayList<>(currentLog);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    /**
     * Detiene el servidor seleccionado y actualiza el estado visual.
     * También limpia el área de consola.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void onStopServerClicked(ActionEvent actionEvent) {
        String name=selectedServer.getName();
        Server running=ServerManager.getServer(name);
        if (running == null || running.getState() == Server.ServerState.STOPPED) return;

        running.stop();
        running.setState(Server.ServerState.STOPPED);
        updateServerControls(running);

        // Opcional: Limpiar consola visual
        Platform.runLater(() -> consoleArea.clear());
        ServerManager.removeServer(name);
    }
    /**
     * Reinicia el servidor seleccionado con una pausa de 5 segundos entre detener e iniciar.
     *
     * @param actionEvent Evento de acción del botón.
     */
    public void onRestartServerClicked(ActionEvent actionEvent) {
        String name=selectedServer.getName();
        Server running=ServerManager.getServer(name);
        if (running == null) return;

        running.stop();
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                running.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    /**
     * Carga los temas disponibles desde la carpeta "themes".
     * Solo se consideran archivos con extensión ".css".
     *
     * @return Lista de archivos de tema encontrados.
     */
    private List<File> cargarTemas() {
        File carpetaTemas = new File("themes");
        if (!carpetaTemas.exists()) carpetaTemas.mkdirs();

        File[] archivos = carpetaTemas.listFiles((dir, name) -> name.endsWith(".css")); // o el tipo que uses
        return archivos != null ? List.of(archivos) : new ArrayList<>();
    }

}