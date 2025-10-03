package br.edu.ifpr.gep.panel.dados;
import br.edu.ifpr.gep.model.Portaria;
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
import java.util.Optional;

public class DadosController {
    private PortariaRepository repo = PortariaRepository.INSTANCE;

    @FXML
    private Button simularButton;
    @FXML
    private Button incluirButton;
    @FXML
    private Button alterarButton;
    @FXML
    private Button excluirButton;
    @FXML
    private Button excluirTodosButton;
    @FXML
    private Button voltarButton;

    @FXML
    private void simularDados() {
        repo.insert(new Portaria("MEC", 234, LocalDate.of(2000, 5, 30), "Alana Beatriz Pereira"));
        repo.insert(new Portaria("IFPR", 74, LocalDate.of(2015, 7, 11), "Pietra Maya Souza"));
        repo.insert(new Portaria("MinC", 112, LocalDate.of(2001, 3, 27), "Sueli Lúcia Gabriela dos Santos"));
        repo.insert(new Portaria("MJ", 234, LocalDate.of(2020, 11, 10), "Eduardo Cauã Martins"));
        repo.insert(new Portaria("MPF", 3, LocalDate.of(2005, 9, 12), "Hugo Fernando Melo"));
        repo.insert(new Portaria("UFPR", 1001, LocalDate.of(2008, 2, 20), "Hadassa Isabella Esther Campos"));
        repo.insert(new Portaria("UTFPR", 79, LocalDate.of(2011, 12, 1), "Juan Raul Danilo de Paula"));
        repo.insert(new Portaria("Unicamp", 33, LocalDate.of(2000, 6, 30), "Débora Joana Farias"));
        repo.insert(new Portaria("UEL", 79, LocalDate.of(2019, 5, 3), "Marcos Pedro Bryan Vieira"));
        repo.insert(new Portaria("UFRGS", 98, LocalDate.of(2002, 4, 19), "Murilo Enzo Pedro Araújo"));
        repo.insert(new Portaria("IFPR", 101, LocalDate.of(2010, 10, 21), "Davi Thales Teixeira"));
        repo.insert(new Portaria("UFPR", 234, LocalDate.of(2018, 12, 2), "Anderson Thomas Miguel Lima"));
        repo.insert(new Portaria("MJ", 7, LocalDate.of(2010, 1, 10), "Juliana Adriana Mariah Jesus"));
        repo.insert(new Portaria("MJ", 234, LocalDate.of(2022, 6, 15), "Juliana Adriana Mariah Jesus"));
        repo.insert(new Portaria("UFPR", 11, LocalDate.of(2000, 5, 30), "Louise Aurora Sophia da Conceição"));
        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Dados simulados com sucesso!");
    }

    @FXML
    private void incluir() {
        try {
            String emissor = null;
            while (emissor == null) {
                TextInputDialog emissorDialog = new TextInputDialog();
                emissorDialog.setTitle("Incluir Portaria");
                emissorDialog.setHeaderText("Digite o emissor:");
                Optional<String> emissorResult = emissorDialog.showAndWait();
                if (!emissorResult.isPresent()) {
                    showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de inclusão cancelada.");
                    return;
                }
                String input = emissorResult.get().trim();
                if (input.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo emissor não pode estar em branco.");
                    continue;
                }
                emissor = input;
            }

            Integer num = null;
            while (num == null) {
                TextInputDialog numDialog = new TextInputDialog();
                numDialog.setTitle("Incluir Portaria");
                numDialog.setHeaderText("Digite o número:");
                Optional<String> numResult = numDialog.showAndWait();
                if (!numResult.isPresent()) {
                    showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de inclusão cancelada.");
                    return;
                }
                String input = numResult.get().trim();
                if (input.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo número não pode estar em branco.");
                    continue;
                }
                try {
                    num = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O número deve ser um valor numérico válido.");
                }
            }

            LocalDate data = null;
            while (data == null) {
                TextInputDialog dataDialog = new TextInputDialog();
                dataDialog.setTitle("Incluir Portaria");
                dataDialog.setHeaderText("Digite a data de publicação (yyyy-mm-dd):");
                Optional<String> dataResult = dataDialog.showAndWait();
                if (!dataResult.isPresent()) {
                    showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de inclusão cancelada.");
                    return;
                }
                String input = dataResult.get().trim();
                if (input.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo data não pode estar em branco.");
                    continue;
                }
                try {
                    data = LocalDate.parse(input);
                } catch (DateTimeParseException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "A data deve estar no formato yyyy-mm-dd.");
                }
            }

            String membro = null;
            while (membro == null) {
                TextInputDialog membroDialog = new TextInputDialog();
                membroDialog.setTitle("Incluir Portaria");
                membroDialog.setHeaderText("Digite o membro:");
                Optional<String> membroResult = membroDialog.showAndWait();
                if (!membroResult.isPresent()) {
                    showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de inclusão cancelada.");
                    return;
                }
                String input = membroResult.get().trim();
                if (input.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo membro não pode estar em branco.");
                    continue;
                }
                membro = input;
            }

            Portaria portaria = new Portaria(emissor, num, data, membro);
            if (repo.insert(portaria)) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Portaria incluída com sucesso!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao incluir portaria (já existe ou dados inválidos).");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void alterar() {
        try {
            String emissor = null;
            while (emissor == null) {
                TextInputDialog emissorDialog = new TextInputDialog();
                emissorDialog.setTitle("Alterar Portaria");
                emissorDialog.setHeaderText("Digite o emissor:");
                Optional<String> emissorResult = emissorDialog.showAndWait();
                if (!emissorResult.isPresent()) {
                    showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de alteração cancelada.");
                    return;
                }
                String input = emissorResult.get().trim();
                if (input.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo emissor não pode estar em branco.");
                    continue;
                }
                emissor = input;
            }

            Integer num = null;
            while (num == null) {
                TextInputDialog numDialog = new TextInputDialog();
                numDialog.setTitle("Alterar Portaria");
                numDialog.setHeaderText("Digite o número:");
                Optional<String> numResult = numDialog.showAndWait();
                if (!numResult.isPresent()) {
                    showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de alteração cancelada.");
                    return;
                }
                String input = numResult.get().trim();
                if (input.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo número não pode estar em branco.");
                    continue;
                }
                try {
                    num = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O número deve ser um valor numérico válido.");
                }
            }

            Integer ano = null;
            while (ano == null) {
                TextInputDialog anoDialog = new TextInputDialog();
                anoDialog.setTitle("Alterar Portaria");
                anoDialog.setHeaderText("Digite o ano:");
                Optional<String> anoResult = anoDialog.showAndWait();
                if (!anoResult.isPresent()) {
                    showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de alteração cancelada.");
                    return;
                }
                String input = anoResult.get().trim();
                if (input.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo ano não pode estar em branco.");
                    continue;
                }
                try {
                    ano = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O ano deve ser um valor numérico válido.");
                }
            }

            Optional<Portaria> opt = repo.findPortaria(emissor, num, ano);
            if (opt.isPresent()) {
                Portaria portaria = opt.get();
                String novoMembro = null;
                while (novoMembro == null) {
                    TextInputDialog membroDialog = new TextInputDialog(portaria.getMembro());
                    membroDialog.setTitle("Alterar Portaria");
                    membroDialog.setHeaderText("Digite o novo membro:");
                    Optional<String> novoMembroResult = membroDialog.showAndWait();
                    if (!novoMembroResult.isPresent()) {
                        showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de alteração cancelada.");
                        return;
                    }
                    String input = novoMembroResult.get().trim();
                    if (input.isEmpty()) {
                        showAlert(Alert.AlertType.ERROR, "Erro", "O campo novo membro não pode estar em branco.");
                        continue;
                    }
                    novoMembro = input;
                }
                portaria.setMembro(novoMembro);
                if (repo.update(portaria)) {
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Portaria alterada com sucesso!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao alterar portaria.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Portaria não encontrada.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void excluir() {
        try {
            String emissor = null;
            while (emissor == null) {
                TextInputDialog emissorDialog = new TextInputDialog();
                emissorDialog.setTitle("Excluir Portaria");
                emissorDialog.setHeaderText("Digite o emissor:");
                Optional<String> emissorResult = emissorDialog.showAndWait();
                if (!emissorResult.isPresent()) {
                    showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de exclusão cancelada.");
                    return;
                }
                String input = emissorResult.get().trim();
                if (input.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo emissor não pode estar em branco.");
                    continue;
                }
                emissor = input;
            }

            Integer num = null;
            while (num == null) {
                TextInputDialog numDialog = new TextInputDialog();
                numDialog.setTitle("Excluir Portaria");
                numDialog.setHeaderText("Digite o número:");
                Optional<String> numResult = numDialog.showAndWait();
                if (!numResult.isPresent()) {
                    showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de exclusão cancelada.");
                    return;
                }
                String input = numResult.get().trim();
                if (input.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo número não pode estar em branco.");
                    continue;
                }
                try {
                    num = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O número deve ser um valor numérico válido.");
                }
            }

            Integer ano = null;
            while (ano == null) {
                TextInputDialog anoDialog = new TextInputDialog();
                anoDialog.setTitle("Excluir Portaria");
                anoDialog.setHeaderText("Digite o ano:");
                Optional<String> anoResult = anoDialog.showAndWait();
                if (!anoResult.isPresent()) {
                    showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de exclusão cancelada.");
                    return;
                }
                String input = anoResult.get().trim();
                if (input.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O campo ano não pode estar em branco.");
                    continue;
                }
                try {
                    ano = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "O ano deve ser um valor numérico válido.");
                }
            }

            if (repo.delete(emissor, num, ano)) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Portaria excluída com sucesso!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Portaria não encontrada.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void excluirTodos() {
        int regs = repo.delete();
        showAlert(Alert.AlertType.INFORMATION, "Sucesso", regs + " portarias excluídas!");
    }

    @FXML
    private void voltar() throws Exception {
        Stage stage = (Stage) voltarButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifpr/gep/panel/MainPanel.fxml"));
        Scene scene = new Scene(loader.load(), 400, 300);
        scene.getStylesheets().add(getClass().getResource("/br/edu/ifpr/gep/panel/DesignPanel.css").toExternalForm());
        stage.setScene(scene);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}