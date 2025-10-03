package br.edu.ifpr.gep.view;

import br.edu.ifpr.gep.model.Portaria;
import br.edu.ifpr.gep.model.repository.PortariaRepository;
import br.edu.ifpr.gep.model.utils.EmissorTypes;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TabPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainView implements Initializable {
    private PortariaRepository repo = PortariaRepository.INSTANCE;

    @FXML private TabPane tabPane;
    @FXML private TableView<Portaria> tableView;
    @FXML private TableColumn<Portaria, String> colPortaria;
    @FXML private TableColumn<Portaria, String> colEmissor;
    @FXML private TableColumn<Portaria, Integer> colNumero;
    @FXML private TableColumn<Portaria, String> colPublicacao;
    @FXML private TableColumn<Portaria, String> colNome;
    @FXML private Label lblStatus;

    @FXML private Button simularButton;
    @FXML private Button incluirButton;
    @FXML private Button alterarButton;
    @FXML private Button excluirButton;
    @FXML private Button excluirTodosButton;
    @FXML private Button voltarButton;
    @FXML private Button limparFiltroButton;
    @FXML private Button todosButton;
    @FXML private Button portariaButton;
    @FXML private Button emissorButton;
    @FXML private Button numeroButton;
    @FXML private Button publicacaoButton;
    @FXML private Button periodoButton;
    @FXML private Button nomeButton;

    @FXML private TextField tfEmissor;
    @FXML private TextField tfNumero;
    @FXML private TextField tfAno;
    @FXML private TextField tfMembro;
    @FXML private Button btnAplicarAlteracao;

    private final ObservableList<Portaria> dados = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configura as colunas da tabela
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("membro"));

        // Coluna Emissor personalizada (nome do enum)
        colEmissor.setCellValueFactory(cellData -> {
            Portaria portaria = cellData.getValue();
            if (portaria != null && portaria.getEmissor() != null) {
                return new SimpleStringProperty(portaria.getEmissor().nome());
            }
            return new SimpleStringProperty("");
        });

        // Coluna Publicação personalizada (formato dd/MM/yyyy)
        colPublicacao.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> {
            Portaria portaria = cellData.getValue();
            if (portaria != null && portaria.getPublicacao() != null) {
                return portaria.getPublicacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
            return "";
        }));

        // Coluna "Portaria" personalizada (formato: "Portaria [Número]/[Ano]")
        colPortaria.setCellValueFactory(cellData -> {
            Portaria portaria = cellData.getValue();
            if (portaria != null) {
                return Bindings.createStringBinding(() ->
                        "Portaria " + portaria.getNumero() + "/" + portaria.getPublicacao().getYear());
            }
            return Bindings.createStringBinding(() -> "");
        });

        // Torna tabela editável e configura edição inline para Nome
        tableView.setEditable(true);
        colNome.setCellFactory(TextFieldTableCell.forTableColumn());
        colNome.setOnEditCommit(event -> {
            Portaria portaria = event.getRowValue();
            portaria.setMembro(event.getNewValue());
            repo.update(portaria);
            updateTable(); // Recarrega para refletir mudanças
            lblStatus.setText("Membro atualizado para: " + event.getNewValue());
        });

        // Listener para seleção na tabela (preenche form de editar)
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            if (newValue != null) {
                tfEmissor.setText(String.valueOf(newValue.getEmissor().index()));
                tfNumero.setText(newValue.getNumero().toString());
                tfAno.setText(String.valueOf(newValue.getPublicacao().getYear()));
                tfMembro.setText(newValue.getMembro());
                lblStatus.setText("Editando: " + newValue.getMembro());
            }
        });

        tableView.setItems(dados);
        updateTable();
    }

    // Método para atualizar a tabela com dados do JSON
    private void updateTable() {
        dados.setAll(repo.findAll());
        lblStatus.setText(dados.size() + " portarias carregadas.");
    }

    @FXML
    private void simularDados() {
        repo.insert(new Portaria(1, 234, LocalDate.of(2000, 5, 30), "Alana Beatriz Pereira"));
        repo.insert(new Portaria(2, 74, LocalDate.of(2015, 7, 11), "Pietra Maya Souza"));
        repo.insert(new Portaria(3, 112, LocalDate.of(2001, 3, 27), "Sueli Lúcia Gabriela dos Santos"));
        repo.insert(new Portaria(4, 234, LocalDate.of(2020, 11, 10), "Eduardo Cauã Martins"));
        repo.insert(new Portaria(5, 3, LocalDate.of(2005, 9, 12), "Hugo Fernando Melo"));
        repo.insert(new Portaria(1, 1001, LocalDate.of(2008, 2, 20), "Hadassa Isabella Esther Campos"));
        repo.insert(new Portaria(2, 79, LocalDate.of(2011, 12, 1), "Juan Raul Danilo de Paula"));
        repo.insert(new Portaria(3, 33, LocalDate.of(2000, 6, 30), "Débora Joana Farias"));
        repo.insert(new Portaria(4, 79, LocalDate.of(2019, 5, 3), "Marcos Pedro Bryan Vieira"));
        repo.insert(new Portaria(5, 98, LocalDate.of(2002, 4, 19), "Murilo Enzo Pedro Araújo"));
        repo.insert(new Portaria(1, 101, LocalDate.of(2010, 10, 21), "Davi Thales Teixeira"));
        repo.insert(new Portaria(2, 234, LocalDate.of(2018, 12, 2), "Anderson Thomas Miguel Lima"));
        repo.insert(new Portaria(3, 7, LocalDate.of(2010, 1, 10), "Juliana Adriana Mariah Jesus"));
        repo.insert(new Portaria(4, 234, LocalDate.of(2022, 6, 15), "Juliana Adriana Mariah Jesus"));
        repo.insert(new Portaria(5, 11, LocalDate.of(2000, 5, 30), "Louise Aurora Sophia da Conceição"));
        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Dados simulados com sucesso!");
        updateTable();
    }

    @FXML
    private void incluir() {
        try {
            Integer index = promptInteger("Incluir Portaria", "Digite o índice do emissor (ex: 1 para Reitoria):", true);
            if (index == null) {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de inclusão cancelada.");
                return;
            }

            Integer num = promptInteger("Incluir Portaria", "Digite o número:", true);
            if (num == null) return;

            LocalDate data = promptDate("Incluir Portaria", "Digite a data de publicação (yyyy-mm-dd):", true);
            if (data == null) return;

            String membro = promptInput("Incluir Portaria", "Digite o membro:", true);
            if (membro == null) return;

            Portaria portaria = new Portaria(index, num, data, membro);
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
            Integer index = Integer.parseInt(tfEmissor.getText());
            Integer num = Integer.parseInt(tfNumero.getText());
            Integer ano = Integer.parseInt(tfAno.getText());
            String novoMembro = tfMembro.getText().trim();

            if (index == null || num == null || ano == null || novoMembro.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Preencha todos os campos.");
                return;
            }

            String emissorNome = EmissorTypes.fromValue(index).nome();
            Optional<Portaria> opt = repo.findPortaria(emissorNome, num, ano);
            if (opt.isPresent()) {
                Portaria portaria = opt.get();
                portaria.setMembro(novoMembro);
                if (repo.update(portaria)) {
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Portaria alterada!");
                    updateTable();
                    tabPane.getSelectionModel().select(0);
                    lblStatus.setText("Alteração aplicada com sucesso.");
                    tfEmissor.clear(); tfNumero.clear(); tfAno.clear(); tfMembro.clear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao alterar.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Portaria não encontrada.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Índice, número ou ano inválidos.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void excluir() {
        try {
            Integer index = promptInteger("Excluir Portaria", "Digite o índice do emissor:", true);
            if (index == null) {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", "Operação de exclusão cancelada.");
                return;
            }

            Integer num = promptInteger("Excluir Portaria", "Digite o número:", true);
            if (num == null) return;

            Integer ano = promptInteger("Excluir Portaria", "Digite o ano:", true);
            if (ano == null) return;

            String emissorNome = EmissorTypes.fromValue(index).nome();
            if (repo.delete(emissorNome, num, ano)) {
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
        if (new Alert(Alert.AlertType.CONFIRMATION, "Excluir todas as portarias?").showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            int regs = repo.deleteAll();
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", regs + " portarias excluídas!");
            updateTable();
        }
    }

    @FXML
    private void consultarTodos() {
        List<Portaria> list = repo.findAll();
        dados.setAll(list);
        tabPane.getSelectionModel().select(0);
        lblStatus.setText("Consulta realizada: " + list.size() + " resultados.");
    }

    @FXML
    private void consultarPortaria() {
        try {
            Integer index = promptInteger("Consultar Portaria", "Digite o índice do emissor:", true);
            if (index == null) return;

            Integer num = promptInteger("Consultar Portaria", "Digite o número:", true);
            if (num == null) return;

            Integer ano = promptInteger("Consultar Portaria", "Digite o ano:", true);
            if (ano == null) return;

            String emissorNome = EmissorTypes.fromValue(index).nome();
            Optional<Portaria> opt = repo.findPortaria(emissorNome, num, ano);
            if (opt.isPresent()) {
                dados.setAll(List.of(opt.get())); // Mostra só essa
                tabPane.getSelectionModel().select(0);
                lblStatus.setText("Portaria encontrada: " + opt.get().toString());
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
        String nome = promptInput("Consultar por Emissor", "Digite o nome do emissor (parcial):", true);
        if (nome == null) return;
        List<Portaria> list = repo.findByEmissor(nome, false);
        dados.setAll(list);
        tabPane.getSelectionModel().select(0);
        lblStatus.setText("Filtrado por emissor '" + nome + "': " + list.size() + " resultados.");
    }

    @FXML
    private void consultarNumero() {
        try {
            Integer num = promptInteger("Consultar por Número", "Digite o número:", true);
            if (num == null) return;
            List<Portaria> list = repo.findByNumero(num);
            dados.setAll(list);
            tabPane.getSelectionModel().select(0);
            lblStatus.setText("Filtrado por número " + num + ": " + list.size() + " resultados.");
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
            dados.setAll(list);
            tabPane.getSelectionModel().select(0);
            lblStatus.setText("Filtrado por publicação " + data + ": " + list.size() + " resultados.");
        } catch (Exception e) {
            System.err.println("Erro na consulta por publicação: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void consultarPeriodo() {
        try {
            LocalDate start = promptDate("Consultar por Período", "Digite a data de início (yyyy-mm-dd):", true);
            if (start == null) return;

            LocalDate end = promptDate("Consultar por Período", "Digite a data de fim (yyyy-mm-dd):", true);
            if (end == null) return;

            List<Portaria> list = repo.findByPeriodo(start, end);
            dados.setAll(list);
            tabPane.getSelectionModel().select(0);
            lblStatus.setText("Filtrado por período " + start + " a " + end + ": " + list.size() + " resultados.");
        } catch (Exception e) {
            System.err.println("Erro na consulta por período: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void consultarNome() {
        String nome = promptInput("Consultar por Nome", "Digite o nome do membro (parcial):", true);
        if (nome == null) return;
        List<Portaria> list = repo.findByMembro(nome, false);
        dados.setAll(list);
        tabPane.getSelectionModel().select(0);
        lblStatus.setText("Filtrado por nome '" + nome + "': " + list.size() + " resultados.");
    }

    @FXML
    private void voltar() throws Exception {
        Stage stage = (Stage) voltarButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifpr/gep/view/MainView.fxml"));

        if (loader.getLocation() == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Arquivo FXML não encontrado.");
            return;
        }

        TabPane tabPaneNew = loader.load();
        Scene scene = new Scene(tabPaneNew, 1000, 700);

        var cssResource = getClass().getResource("/br/edu/ifpr/gep/view/MainView.css");
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        } else {
            System.err.println("CSS não encontrado.");
        }

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void cancelar() {
        showAlert(Alert.AlertType.INFORMATION, "Cancelado", "Ação cancelada pelo usuário.");
    }

    // Métodos auxiliares para prompts
    private String promptInput(String title, String header, boolean allowCancel) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) {
            if (allowCancel) {
                return null;
            } else {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", title + " cancelada. Tente novamente.");
                return promptInput(title, header, allowCancel);
            }
        }
        String input = result.get().trim();
        if (input.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", header + " não pode estar em branco.");
            return promptInput(title, header, allowCancel);
        }
        return input;
    }

    private String promptInput(String title, String header, boolean allowCancel, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) {
            if (allowCancel) {
                return null;
            } else {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", title + " cancelada. Tente novamente.");
                return promptInput(title, header, allowCancel, defaultValue);
            }
        }
        String input = result.get().trim();
        if (input.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", header + " não pode estar em branco.");
            return promptInput(title, header, allowCancel, defaultValue);
        }
        return input;
    }

    private Integer promptInteger(String title, String header, boolean allowCancel) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) {
            if (allowCancel) {
                return null;
            } else {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", title + " cancelada. Tente novamente.");
                return promptInteger(title, header, allowCancel);
            }
        }
        String input = result.get().trim();
        if (input.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", header + " não pode estar em branco.");
            return promptInteger(title, header, allowCancel);
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", header + " deve ser um valor numérico válido.");
            return promptInteger(title, header, allowCancel);
        }
    }

    @FXML
    private void limparFiltro() {
        updateTable(); // Recarrega todos os dados
        tabPane.getSelectionModel().select(0); // Vai pra aba Geral
        lblStatus.setText("Filtro limpo: " + dados.size() + " portarias totais.");
    }

    private LocalDate promptDate(String title, String header, boolean allowCancel) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) {
            if (allowCancel) {
                return null;
            } else {
                showAlert(Alert.AlertType.WARNING, "Ação Cancelada", title + " cancelada. Tente novamente.");
                return promptDate(title, header, allowCancel);
            }
        }
        String input = result.get().trim();
        if (input.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", header + " não pode estar em branco.");
            return promptDate(title, header, allowCancel);
        }
        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", header + " deve estar no formato yyyy-mm-dd.");
            return promptDate(title, header, allowCancel);
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