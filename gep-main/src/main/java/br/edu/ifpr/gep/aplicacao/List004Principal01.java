package br.edu.ifpr.gep.aplicacao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import java.io.IOException;

public class List004Principal01 extends Application {

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifpr/gep/panel/MainPanel.fxml"));
        if (loader.getLocation() == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Arquivo FXML não encontrado: /br/edu/ifpr/gep/panel/MainPanel.fxml");
            return;
        }
        TabPane root = loader.load();
        Scene scene = new Scene(root, 600, 400); // Ajustado para as dimensões do FXML
        try {
            scene.getStylesheets().add(getClass().getResource("/br/edu/ifpr/gep/panel/DesignPanel.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Aviso: Arquivo CSS não encontrado: /br/edu/ifpr/gep/panel/DesignPanel.css");
        }
        primaryStage.setTitle("Sistema Greb");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
