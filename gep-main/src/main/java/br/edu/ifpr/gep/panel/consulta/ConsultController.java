package br.edu.ifpr.gep.panel.consulta;

import br.edu.ifpr.gep.model.Portaria;
import br.edu.ifpr.gep.model.StringSearch;
import br.edu.ifpr.gep.model.repository.PortariaRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class ConsultController {

    private PortariaRepository repo = PortariaRepository.INSTANCE;

    @FXML
    private Button todosButton;

    @FXML
    private Button portariaButton;

    @FXML
    private Button emissorButton;

    @FXML
    private Button numeroButton;

    @FXML
    private Button publicacaoButton;

    @FXML
    private Button periodoButton;

    @FXML
    private Button nomeButton;

    @FXML
    private Button voltarButton;

    @FXML
    private void consultarTodos() {
        List<Portaria> todos = repo.findAll();
        showAlert(Alert.AlertType.INFORMATION, "Resultados", formatList(todos));
    }

    @FXML
    private void consultarPortaria() {
        try {
            String emissor = null;
            while (emissor == null) {
                TextInputDialog emissorDialog = new TextInputDialog();
                emissorDialog.setTitle("Consultar Portaria");
                emissorDialog.setHeaderText("Digite o emissor:");
                Optional<String> emissorResult = emissorDialog.showAndWait();
                if (!emissorResult.isPresent()) {
                    showAlert(Alert.AlertType.INFORMATION, "Cancelado", "Consulta de portaria cancelada.");
                    return;
                }
                if (emissorResult.get().trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo emissor não pode estar vazio.");
                    continue;
                }
                emissor = emissorResult.get().trim();
            }

            Integer num = null;
            while (num == null) {
                TextInputDialog numDialog = new TextInputDialog();
                numDialog.setTitle("Consultar Portaria");
                numDialog.setHeaderText("Digite o número:");
                Optional<String> numResult = numDialog.showAndWait();
                if (!numResult.isPresent()) {
                    showAlert(Alert.AlertType.INFORMATION, "Cancelado", "Consulta de portaria cancelada.");
                    return;
                }
                if (numResult.get().trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo número não pode estar vazio.");
                    continue;
                }
                try {
                    num = Integer.parseInt(numResult.get().trim());
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Número inválido. Digite apenas valores numéricos.");
                    continue;
                }
            }

            Integer ano = null;
            while (ano == null) {
                TextInputDialog anoDialog = new TextInputDialog();
                anoDialog.setTitle("Consultar Portaria");
                anoDialog.setHeaderText("Digite o ano:");
                Optional<String> anoResult = anoDialog.showAndWait();
                if (!anoResult.isPresent()) {
                    showAlert(Alert.AlertType.INFORMATION, "Cancelado", "Consulta de portaria cancelada.");
                    return;
                }
                if (anoResult.get().trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo ano não pode estar vazio.");
                    continue;
                }
                try {
                    ano = Integer.parseInt(anoResult.get().trim());
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Ano inválido. Digite apenas valores numéricos.");
                    continue;
                }
            }

            Optional<Portaria> opt = repo.findPortaria(emissor, num, ano);
            if (opt.isPresent()) {
                showAlert(Alert.AlertType.INFORMATION, "Resultado", opt.get().toString());
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Portaria não encontrada.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void consultarEmissor() {
        String emissor = null;
        while (emissor == null) {
            TextInputDialog emissorDialog = new TextInputDialog();
            emissorDialog.setTitle("Consultar por Emissor");
            emissorDialog.setHeaderText("Digite o emissor:");
            Optional<String> emissorResult = emissorDialog.showAndWait();
            if (!emissorResult.isPresent()) {
                showAlert(Alert.AlertType.INFORMATION, "Cancelado", "Consulta por emissor cancelada.");
                return;
            }
            if (emissorResult.get().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "O campo emissor não pode estar vazio.");
                continue;
            }
            emissor = emissorResult.get().trim();
        }
        List<Portaria> list = repo.findByEmissor(emissor, StringSearch.PARTIAL_CASE_INSENSITIVE);
        showAlert(Alert.AlertType.INFORMATION, "Resultados", formatList(list));
    }

    @FXML
    private void consultarNumero() {
        try {
            Integer num = null;
            while (num == null) {
                TextInputDialog numDialog = new TextInputDialog();
                numDialog.setTitle("Consultar por Número");
                numDialog.setHeaderText("Digite o número:");
                Optional<String> numResult = numDialog.showAndWait();
                if (!numResult.isPresent()) {
                    showAlert(Alert.AlertType.INFORMATION, "Cancelado", "Consulta por número cancelada.");
                    return;
                }
                if (numResult.get().trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo número não pode estar vazio.");
                    continue;
                }
                try {
                    num = Integer.parseInt(numResult.get().trim());
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Número inválido. Digite apenas valores numéricos.");
                    continue;
                }
            }
            List<Portaria> list = repo.findByNumero(num);
            showAlert(Alert.AlertType.INFORMATION, "Resultados", formatList(list));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void consultarPublicacao() {
        try {
            LocalDate data = null;
            while (data == null) {
                TextInputDialog dataDialog = new TextInputDialog();
                dataDialog.setTitle("Consultar por Publicação");
                dataDialog.setHeaderText("Digite a data de publicação (yyyy-mm-dd):");
                Optional<String> dataResult = dataDialog.showAndWait();
                if (!dataResult.isPresent()) {
                    showAlert(Alert.AlertType.INFORMATION, "Cancelado", "Consulta por publicação cancelada.");
                    return;
                }
                if (dataResult.get().trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo data não pode estar vazio.");
                    continue;
                }
                try {
                    data = LocalDate.parse(dataResult.get().trim());
                } catch (DateTimeParseException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Formato de data inválido. Use o formato yyyy-mm-dd.");
                    continue;
                }
            }
            List<Portaria> list = repo.findByPublicacao(data);
            showAlert(Alert.AlertType.INFORMATION, "Resultados", formatList(list));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void consultarPeriodo() {
        try {
            LocalDate start = null;
            while (start == null) {
                TextInputDialog startDialog = new TextInputDialog();
                startDialog.setTitle("Consultar por Período");
                startDialog.setHeaderText("Digite a data de início (yyyy-mm-dd):");
                Optional<String> startResult = startDialog.showAndWait();
                if (!startResult.isPresent()) {
                    showAlert(Alert.AlertType.INFORMATION, "Cancelado", "Consulta por período cancelada.");
                    return;
                }
                if (startResult.get().trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo data de início não pode estar vazio.");
                    continue;
                }
                try {
                    start = LocalDate.parse(startResult.get().trim());
                } catch (DateTimeParseException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Formato de data inválido. Use o formato yyyy-mm-dd.");
                    continue;
                }
            }

            LocalDate end = null;
            while (end == null) {
                TextInputDialog endDialog = new TextInputDialog();
                endDialog.setTitle("Consultar por Período");
                endDialog.setHeaderText("Digite a data de fim (yyyy-mm-dd):");
                Optional<String> endResult = endDialog.showAndWait();
                if (!endResult.isPresent()) {
                    showAlert(Alert.AlertType.INFORMATION, "Cancelado", "Consulta por período cancelada.");
                    return;
                }
                if (endResult.get().trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo data de fim não pode estar vazio.");
                    continue;
                }
                try {
                    end = LocalDate.parse(endResult.get().trim());
                } catch (DateTimeParseException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Formato de data inválido. Use o formato yyyy-mm-dd.");
                    continue;
                }
            }

            List<Portaria> list = repo.findByPeriodo(start, end);
            showAlert(Alert.AlertType.INFORMATION, "Resultados", formatList(list));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void consultarNome() {
        String nome = null;
        while (nome == null) {
            TextInputDialog nomeDialog = new TextInputDialog();
            nomeDialog.setTitle("Consultar por Nome");
            nomeDialog.setHeaderText("Digite o nome do membro:");
            Optional<String> nomeResult = nomeDialog.showAndWait();
            if (!nomeResult.isPresent()) {
                showAlert(Alert.AlertType.INFORMATION, "Cancelado", "Consulta por nome cancelada.");
                return;
            }
            if (nomeResult.get().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "O campo nome não pode estar vazio.");
                continue;
            }
            nome = nomeResult.get().trim();
        }
        List<Portaria> list = repo.findByMembro(nome, StringSearch.PARTIAL_CASE_INSENSITIVE);
        showAlert(Alert.AlertType.INFORMATION, "Resultados", formatList(list));
    }

    @FXML
    private void voltar() throws Exception {
        Stage stage = (Stage) voltarButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifpr/gep/panel/MainPanel.fxml"));
        Scene scene = new Scene(loader.load(), 400, 300);
        scene.getStylesheets().add(getClass().getResource("/br/edu/ifpr/gep/panel/DesignPanel.css").toExternalForm());
        stage.setScene(scene);
    }

    private String formatList(List<Portaria> list) {
        if (list.isEmpty()) {
            return "Nenhuma portaria encontrada.";
        }
        StringBuilder sb = new StringBuilder();
        for (Portaria p : list) {
            sb.append(p.toString()).append("\n");
        }
        return sb.toString();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}