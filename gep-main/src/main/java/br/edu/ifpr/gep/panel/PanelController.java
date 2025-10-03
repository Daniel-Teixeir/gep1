package br.edu.ifpr.gep.panel;

import br.edu.ifpr.gep.model.Portaria;
import br.edu.ifpr.gep.model.StringSearch;
import br.edu.ifpr.gep.model.repository.PortariaRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TabPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PanelController implements Initializable {
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

    // Campos para a tabela
    @FXML
    private TableView<Portaria> tableView;
    @FXML
    private TableColumn<Portaria, String> colPortaria;
    @FXML
    private TableColumn<Portaria, String> colEmissor;
    @FXML
    private TableColumn<Portaria, Integer> colNumero;
    @FXML
    private TableColumn<Portaria, LocalDate> colPublicacao;
    @FXML
    private TableColumn<Portaria, String> colNome;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configura as colunas da tabela
    	colEmissor.setCellValueFactory(new PropertyValueFactory<>("emissor"));
    	colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));  // Sem acento
    	colPublicacao.setCellValueFactory(new PropertyValueFactory<>("publicacao"));  // Sem acento
    	colNome.setCellValueFactory(new PropertyValueFactory<>("membro"));

        // Coluna "Portaria" personalizada (formato: "Portaria [Número]/[Ano]")
    	colPortaria.setCellValueFactory(cellData -> {
    	    Portaria portaria = cellData.getValue();
    	    if (portaria != null) {
    	        return javafx.beans.binding.Bindings.createStringBinding(() -> 
    	            "Portaria " + portaria.getNumero() + "/" + portaria.getPublicacao().getYear());  // Ajustado sem acento
    	    }
    	    return javafx.beans.binding.Bindings.createStringBinding(() -> "");
    	});

        // Carrega os dados iniciais do JSON
        updateTable();
    }

    // Método para atualizar a tabela com dados do JSON
    private void updateTable() {
        List<Portaria> portarias = repo.findAll();
        ObservableList<Portaria> observableList = FXCollections.observableArrayList(portarias);
        tableView.setItems(observableList);
    }

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
        updateTable();
    }

    @FXML
    private void incluir() {
        try {
            String emissor = promptInput("Incluir Portaria", "Digite o emissor:", false);
            if (emissor == null) {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de inclusão cancelada.");
                return;
            }

            Integer num = promptInteger("Incluir Portaria", "Digite o número:", false);
            if (num == null) {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de inclusão cancelada.");
                return;
            }

            LocalDate data = promptDate("Incluir Portaria", "Digite a data de publicação (yyyy-mm-dd):", false);
            if (data == null) {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de inclusão cancelada.");
                return;
            }

            String membro = promptInput("Incluir Portaria", "Digite o membro:", false);
            if (membro == null) {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de inclusão cancelada.");
                return;
            }

            Portaria portaria = new Portaria(emissor, num, data, membro);
            if (repo.insert(portaria)) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Portaria incluída com sucesso!");
                updateTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao incluir portaria (já existe ou dados inválidos).");
            }
        } catch (Exception e) {
            System.err.println("Erro na inclusão: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void alterar() {
        try {
            String emissor = promptInput("Alterar Portaria", "Digite o emissor:", false);
            if (emissor == null) {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de alteração cancelada.");
                return;
            }

            Integer num = promptInteger("Alterar Portaria", "Digite o número:", false);
            if (num == null) {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de alteração cancelada.");
                return;
            }

            Integer ano = promptInteger("Alterar Portaria", "Digite o ano:", false);
            if (ano == null) {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de alteração cancelada.");
                return;
            }

            Optional<Portaria> opt = repo.findPortaria(emissor, num, ano);
            if (opt.isPresent()) {
                Portaria portaria = opt.get();
                String novoMembro = promptInput("Alterar Portaria", "Digite o novo membro:", false, portaria.getMembro());
                if (novoMembro != null) {
                    portaria.setMembro(novoMembro);
                    if (repo.update(portaria)) {
                        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Portaria alterada com sucesso!");
                        updateTable();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao alterar portaria.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de alteração cancelada.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Portaria não encontrada.");
            }
        } catch (Exception e) {
            System.err.println("Erro na alteração: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void excluir() {
        try {
            String emissor = promptInput("Excluir Portaria", "Digite o emissor:", false);
            if (emissor == null) {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de exclusão cancelada.");
                return;
            }

            Integer num = promptInteger("Excluir Portaria", "Digite o número:", false);
            if (num == null) {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de exclusão cancelada.");
                return;
            }

            Integer ano = promptInteger("Excluir Portaria", "Digite o ano:", false);
            if (ano == null) {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de exclusão cancelada.");
                return;
            }

            if (repo.delete(emissor, num, ano)) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Portaria excluída com sucesso!");
                updateTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Portaria não encontrada.");
            }
        } catch (Exception e) {
            System.err.println("Erro na exclusão: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void excluirTodos() {
        int regs = repo.delete();
        showAlert(Alert.AlertType.INFORMATION, "Sucesso", regs + " portarias excluídas!");
        updateTable();
    }

    @FXML
    private void consultarTodos() {
        List<Portaria> todos = repo.findAll();
        showAlert(Alert.AlertType.INFORMATION, "Resultados", formatList(todos));
        updateTable();
    }

    @FXML
    private void consultarPortaria() {
        try {
            String emissor = promptInput("Consultar Portaria", "Digite o emissor:", true);
            if (emissor == null) return;

            Integer num = promptInteger("Consultar Portaria", "Digite o número:", true);
            if (num == null) return;

            Integer ano = promptInteger("Consultar Portaria", "Digite o ano:", true);
            if (ano == null) return;

            Optional<Portaria> opt = repo.findPortaria(emissor, num, ano);
            if (opt.isPresent()) {
                showAlert(Alert.AlertType.INFORMATION, "Resultado", opt.get().toString());
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Portaria não encontrada.");
            }
        } catch (Exception e) {
            System.err.println("Erro na consulta de portaria: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void consultarEmissor() {
        String emissor = promptInput("Consultar por Emissor", "Digite o emissor:", true);
        if (emissor == null) return;
        List<Portaria> list = repo.findByEmissor(emissor, StringSearch.PARTIAL_CASE_INSENSITIVE);
        showAlert(Alert.AlertType.INFORMATION, "Resultados", formatList(list));
    }

    @FXML
    private void consultarNumero() {
        try {
            Integer num = promptInteger("Consultar por Número", "Digite o número:", true);
            if (num == null) return;
            List<Portaria> list = repo.findByNumero(num);
            showAlert(Alert.AlertType.INFORMATION, "Resultados", formatList(list));
        } catch (Exception e) {
            System.err.println("Erro na consulta por número: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void consultarPublicacao() {
        try {
            LocalDate data = promptDate("Consultar por Publicação", "Digite a data de publicação (yyyy-mm-dd):", true);
            if (data == null) return;
            List<Portaria> list = repo.findByPublicacao(data);
            showAlert(Alert.AlertType.INFORMATION, "Resultados", formatList(list));
        } catch (Exception e) {
            System.err.println("Erro na consulta por publicação: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void consultarPeriodo() {
        try {
            LocalDate start = promptDate("Consultar por Período", "Digite a data de início (yyyy-mm-dd):", false);
            if (start == null) return;

            LocalDate end = promptDate("Consultar por Período", "Digite a data de fim (yyyy-mm-dd):", false);
            if (end == null) return;

            List<Portaria> list = repo.findByPeriodo(start, end);
            showAlert(Alert.AlertType.INFORMATION, "Resultados", formatList(list));
        } catch (Exception e) {
            System.err.println("Erro na consulta por período: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void consultarNome() {
        String nome = promptInput("Consultar por Nome", "Digite o nome do membro:", true);
        if (nome == null) return;
        List<Portaria> list = repo.findByMembro(nome, StringSearch.PARTIAL_CASE_INSENSITIVE);
        showAlert(Alert.AlertType.INFORMATION, "Resultados", formatList(list));
    }

    @FXML
    private void voltar() throws Exception {
        Stage stage = (Stage) voltarButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifpr/gep/panel/MainPanel.fxml"));

        if (loader.getLocation() == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Arquivo FXML não encontrado: /br/edu/ifpr/gep/panel/MainPanel.fxml");
            return;
        }

        TabPane tabPane = loader.load();
        Scene scene = new Scene(tabPane, 600, 400);

        var cssResource = getClass().getResource("/br/edu/ifpr/gep/DesignPanel.css");
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        } else {
            System.err.println("CSS não encontrado: /br/edu/ifpr/gep/DesignPanel.css");
        }

        stage.setScene(scene);
        stage.show();
    }

    // Métodos auxiliares para prompts
    private String promptInput(String title, String header, boolean allowCancel) {
        while (true) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle(title);
            dialog.setHeaderText(header);
            Optional<String> result = dialog.showAndWait();
            if (!result.isPresent()) {
                if (allowCancel) return null;
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", title + " cancelada.");
                continue;
            }
            String input = result.get().trim();
            if (input.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", header + " não pode estar em branco.");
                continue;
            }
            return input;
        }
    }

    private String promptInput(String title, String header, boolean allowCancel, String defaultValue) {
        while (true) {
            TextInputDialog dialog = new TextInputDialog(defaultValue);
            dialog.setTitle(title);
            dialog.setHeaderText(header);
            Optional<String> result = dialog.showAndWait();
            if (!result.isPresent()) {
                if (allowCancel) return null;
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", title + " cancelada.");
                continue;
            }
            String input = result.get().trim();
            if (input.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", header + " não pode estar em branco.");
                continue;
            }
            return input;
        }
    }

    private Integer promptInteger(String title, String header, boolean allowCancel) {
        while (true) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle(title);
            dialog.setHeaderText(header);
            Optional<String> result = dialog.showAndWait();
            if (!result.isPresent()) {
                if (allowCancel) return null;
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", title + " cancelada.");
                continue;
            }
            String input = result.get().trim();
            if (input.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", header + " não pode estar em branco.");
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", header + " deve ser um valor numérico válido.");
            }
        }
    }

    private LocalDate promptDate(String title, String header, boolean allowCancel) {
        while (true) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle(title);
            dialog.setHeaderText(header);
            Optional<String> result = dialog.showAndWait();
            if (!result.isPresent()) {
                if (allowCancel) return null;
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", title + " cancelada.");
                continue;
            }
            String input = result.get().trim();
            if (input.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", header + " não pode estar em branco.");
                continue;
            }
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", header + " deve estar no formato yyyy-mm-dd.");
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
}