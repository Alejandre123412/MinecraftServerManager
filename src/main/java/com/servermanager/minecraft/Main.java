package com.servermanager.minecraft;

import com.servermanager.minecraft.controllers.SaveServerManager;
import com.servermanager.minecraft.controllers.ServerManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
/**
 * Clase principal del proyecto: punto de entrada de la aplicación JavaFX.
 * Inicia la interfaz gráfica, carga el tema visual, y configura el cierre seguro de la aplicación.
 *
 * <p>Extiende {@link javafx.application.Application}, lo que permite inicializar
 * el ciclo de vida de una app JavaFX.</p>
 *
 * @author TuNombre
 */
public class Main extends Application {
    /**
     * Método sobrescrito de {@link Application}.
     * Se ejecuta al iniciar la interfaz gráfica. Carga el archivo FXML, aplica estilos,
     * y configura acciones al cerrar la ventana.
     *
     * @param stage el escenario principal de la aplicación.
     * @throws Exception si ocurre un error al cargar la vista o los recursos.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        Parent root = loader.load(); // <-- Si esto falla, root será null o mal inicializado
        Scene scene = new Scene(root); // <-- Esto es SIEMPRE null porque todavía no se ha agregado a un Stage
        // Carga el tema por defecto (oscuro)
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/themes/dark-theme.css")).toExternalForm());

        stage.setTitle("Minecraft Server Manager");
        stage.setScene(scene);
        stage.show();

        // ✅ Detectar cierre de la ventana (la “X”)
        stage.setOnCloseRequest(event -> {
            System.out.println("Cerrando ventana... finalizando servidores.");
            ServerManager.stopAllServers();  // Usa el método centralizado
            SaveServerManager.saveAllServers();
        });

    }
    /**
     * Método principal que lanza la aplicación JavaFX.
     *
     * @param args argumentos de línea de comandos, si los hubiera.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
