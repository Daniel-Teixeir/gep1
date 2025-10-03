package br.edu.ifpr.gep.panel.dados.alterar;

import br.edu.ifpr.gep.model.Portaria;
import br.edu.ifpr.gep.model.StringSearch;
import br.edu.ifpr.gep.model.repository.PortariaRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AlterarController {

    private PortariaRepository repo = PortariaRepository.INSTANCE;

    @FXML
    private TextField emissorField;

    @FXML
    private ComboBox<Integer> numCombo;

    @FXML
    private ComboBox<Integer> anoCombo;

    @FXML
    private TextField mesField;

    @FXML
    private TextField diaField;

    @FXML
    private TextField nomeField;

    @FXML
    private Button alterarButton;

    @FXML
    private Button voltarButton;

    @FXML
    public void initialize() {
        emissorField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                String emissor = newValue.trim().toLowerCase();
                System.out.println("Buscando emissor: " + emissor);
                List<Integer> nums = repo.findByEmissor(emissor, StringSearch.EXACT)
                        .stream()
                        .map(Portaria::getNumero)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
                System.out.println("Números encontrados: " + nums);
                numCombo.getItems().setAll(nums);
                anoCombo.getItems().clear();
                clearDetails();
            } else {
                numCombo.getItems().clear();
                anoCombo.getItems().clear();
                clearDetails();
            }
        });

        numCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String emissor = emissorField.getText().trim().toLowerCase();
                System.out.println("Número selecionado: " + newValue + ", Emissor: " + emissor);
                List<Integer> anos = repo.findByEmissor(emissor, StringSearch.EXACT)
                        .stream()
                        .filter(p -> p.getNumero().equals(newValue))
                        .map(p -> p.getPublicacao().getYear())
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
                System.out.println("Anos encontrados: " + anos);
                anoCombo.getItems().setAll(anos);
                clearDetails();
            } else {
                anoCombo.getItems().clear();
                clearDetails();
            }
        });

        anoCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String emissor = emissorField.getText().trim().toLowerCase();
                Integer num = numCombo.getValue();
                System.out.println("Ano selecionado: " + newValue + ", Número: " + num + ", Emissor: " + emissor);
                if (num != null) {
                    Optional<Portaria> opt = repo.findPortaria(emissor, num, newValue);
                    System.out.println("Portaria encontrada: " + opt.map(Portaria::toString).orElse("Nenhuma"));
                    opt.ifPresentOrElse(
                            portaria -> {
                                LocalDate data = portaria.getPublicacao();
                                System.out.println("Dados da Portaria: Emissor=" + portaria.getEmissor() +
                                        ", Número=" + portaria.getNumero() +
                                        ", Data=" + data +
                                        ", Membro=" + portaria.getMembro());
                                mesField.setText(String.valueOf(data.getMonthValue()));
                                diaField.setText(String.valueOf(data.getDayOfMonth()));
                                nomeField.setText(portaria.getMembro());
                            },
                            this::clearDetails
                    );
                } else {
                    clearDetails();
                }
            } else {
                clearDetails();
            }
        });
    }

    private void clearDetails() {
        mesField.clear();
        diaField.clear();
        nomeField.clear();
    }

    @FXML
    private void alterar() {
        try {
            String emissor = emissorField.getText().trim();
            if (emissor.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "O emissor não pode estar em branco.");
                return;
            }
            String emissorLower = emissor.toLowerCase();

            Integer num = numCombo.getValue();
            if (num == null) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Selecione um número válido.");
                return;
            }

            Integer ano = anoCombo.getValue();
            if (ano == null) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Selecione um ano válido.");
                return;
            }

            String mesText = mesField.getText().trim();
            if (mesText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "O mês não pode estar em branco.");
                return;
            }
            int mes = Integer.parseInt(mesText);

            String diaText = diaField.getText().trim();
            if (diaText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "O dia não pode estar em branco.");
                return;
            }
            int dia = Integer.parseInt(diaText);

            String nome = nomeField.getText().trim();
            if (nome.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro", "O nome não pode estar em branco.");
                return;
            }

            LocalDate newData = LocalDate.of(ano, mes, dia);

            Optional<Portaria> opt = repo.findPortaria(emissorLower, num, ano);
            if (opt.isPresent()) {
                Portaria portaria = opt.get();
                portaria.setPublicacao(newData);
                portaria.setMembro(nome);
                if (repo.update(portaria)) {
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Portaria alterada com sucesso!");
                    emissorField.clear();
                    numCombo.getItems().clear();
                    anoCombo.getItems().clear();
                    clearDetails();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao alterar portaria.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Portaria não encontrada.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Valores de mês ou dia inválidos: devem ser numéricos.");
        } catch (DateTimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Data inválida: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void voltar() throws Exception {
        Stage stage = (Stage) voltarButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ifpr/gep/panel/dados/DadosPanel.fxml"));
        Scene scene = new Scene(loader.load(), 400, 400);
        scene.getStylesheets().add(getClass().getResource("/br/edu/ifpr/gep/panel/dados/DesignPanel.css").toExternalForm());
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