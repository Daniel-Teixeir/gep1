package br.edu.ifpr.gep.panel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class PanelControle {

    @FXML
    private Button dadosButton;

    @FXML
    private Button consultasButton;

    @FXML
    private Button encerrarButton;

    @FXML
    private void openDadosPanel() throws IOException {
        Stage stage = (Stage) dadosButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifpr/gep/panel/dados/DadosPanel.fxml"));
        Scene scene = new Scene(loader.load(), 400, 400);
        scene.getStylesheets().add(getClass().getResource("/br/edu/ifpr/gep/panel/dados/DesignPanel.css").toExternalForm());
        stage.setScene(scene);
    }

    @FXML
    private void openConsultasPanel() throws IOException {
        Stage stage = (Stage) consultasButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifpr/gep/panel/consulta/ConsultPanel.fxml"));
        Scene scene = new Scene(loader.load(), 400, 500);
        scene.getStylesheets().add(getClass().getResource("/br/edu/ifpr/gep/panel/consulta/DesignPanel.css").toExternalForm());
        stage.setScene(scene);
    }

    @FXML
    private void encerrarPrograma() {
        Platform.exit();
    }
}