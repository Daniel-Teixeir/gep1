package br.edu.ifpr.gep.aplicacao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.net.URL;
import java.io.IOException;

/**
 * Classe principal do sistema GREB.
 * Responsável por inicializar a aplicação JavaFX e carregar a tela principal.
 */
public class List004Principal01 extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Debug: Verifica se o FXML existe no classpath
            URL fxmlUrl = getClass().getResource("/br/edu/ifpr/gep/view/MainPanel.fxml");
            System.out.println("URL do FXML: " + fxmlUrl);  // Imprime no console: se null, problema de localização
            if (fxmlUrl == null) {
                showAlert(Alert.AlertType.ERROR, "Erro Crítico", "Arquivo FXML não encontrado no caminho: /br/edu/ifpr/gep/view/MainPanel.fxml");
                return;  // Sai sem crashar
            }

            // Carrega o painel principal
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            TabPane root = loader.load();

            // Cena com dimensões ajustadas
            Scene scene = new Scene(root, 800, 600);

            // Aplica o CSS, se existir
            try {
                URL cssUrl = getClass().getResource("/br/edu/ifpr/gep/view/DesignPanel.css");
                if (cssUrl != null) {
                    scene.getStylesheets().add(cssUrl.toExternalForm());
                } else {
                    System.err.println("Aviso: CSS não encontrado: /br/edu/ifpr/gep/view/DesignPanel.css");
                }
            } catch (NullPointerException e) {
                System.err.println("Aviso: CSS não encontrado: /br/edu/ifpr/gep/view/DesignPanel.css");
            }

            // Configura janela
            primaryStage.setTitle("Sistema Greb");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {  // Em vez de só IOException
            e.printStackTrace();  // Para debug no console
            showAlert(Alert.AlertType.ERROR, "Erro crítico",
                    "Não foi possível carregar o arquivo FXML: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    /** Método utilitário para exibir alertas */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}