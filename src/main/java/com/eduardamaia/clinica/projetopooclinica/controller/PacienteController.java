// src/main/java/com/eduardamaia/clinica/projetopooclinica/controller/PacienteController.java
package com.eduardamaia.clinica.projetopooclinica.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.service.PacienteService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable; // Importado para diálogos de confirmação
import javafx.scene.Parent;
import javafx.scene.Scene; // Importado para customizar células da tabela
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField; // Adicionado import para List
import javafx.scene.control.cell.PropertyValueFactory; // Importado para lidar com o resultado de alertas
import javafx.stage.Stage;

public class PacienteController implements Initializable {

    @FXML
    private TextField searchPatientField;

    @FXML
    private Button searchButton;

    @FXML
    private Button registerPatientButton;

    @FXML
    private TableView<Paciente> patientTableView;

    @FXML
    private TableColumn<Paciente, Integer> idColumn;
    @FXML
    private TableColumn<Paciente, String> nomeColumn;
    @FXML
    private TableColumn<Paciente, String> cpfColumn;

    @FXML
    private TableColumn<Paciente, String> enderecoColumn;
    @FXML
    private TableColumn<Paciente, String> prontuarioColumn;

    @FXML
    private TableColumn<Paciente, Void> editColumn;
    @FXML
    private TableColumn<Paciente, Void> deleteColumn;



    @FXML
    private Label messageLabel;


    private final ObservableList<Paciente> patientData = FXCollections.observableArrayList();
    private PacienteService pacienteService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("PacienteController initialized!");


        this.pacienteService = new PacienteService();


        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));


        enderecoColumn.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        prontuarioColumn.setCellValueFactory(new PropertyValueFactory<>("prontuario"));


        if (editColumn != null) {
            editColumn.setCellFactory(param -> new TableCell<Paciente, Void>() {
                private final Button editButton = new Button("Editar");
                {
                    // Configura a ação do botão
                    editButton.setOnAction(event -> {
                        Paciente paciente = getTableView().getItems().get(getIndex());
                        handleEditPaciente(paciente); // Chama o método de edição
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null); // Não exibe o botão se a célula estiver vazia
                    } else {
                        setGraphic(editButton); // Exibe o botão na célula
                    }
                }
            });
        }



        if (deleteColumn != null) {
            deleteColumn.setCellFactory(param -> new TableCell<Paciente, Void>() {
                private final Button deleteButton = new Button("Excluir");
                {
                    deleteButton.setOnAction(event -> {
                        Paciente paciente = getTableView().getItems().get(getIndex());
                        handleDeletePaciente(paciente); // Chama o método de exclusão
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            });
        }


        patientTableView.setItems(patientData);

        listarDadosPaciente();
    }


    private void listarDadosPaciente() {
        try {
            // patientData.clear(); // Limpa dados antigos - esta linha está correta e importante
            List<Paciente> pacientesDoBanco = pacienteService.listarPacientes();
            patientData.clear(); // Limpa dados antigos antes de adicionar os novos
            patientData.addAll(pacientesDoBanco); // Adiciona os novos dados
            messageLabel.setText("Dados dos pacientes carregados do banco de dados.");
            messageLabel.setTextFill(javafx.scene.paint.Color.BLACK); // Define cor padrão para a mensagem de sucesso
        } catch (Exception e) {
            messageLabel.setText("Erro ao carregar pacientes do banco de dados: " + e.getMessage());
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            System.err.println("Erro ao carregar pacientes: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void handleEditPaciente(Paciente paciente) {
        System.out.println("Editar paciente: " + paciente.getNome());
        messageLabel.setText("Editando paciente: " + paciente.getNome());
        messageLabel.setTextFill(javafx.scene.paint.Color.BLUE);

        // Chama o método auxiliar para carregar a view de cadastro/edição
        loadCadastroPacienteView(paciente);
    }


    private void handleDeletePaciente(Paciente paciente) {
        System.out.println("Excluir paciente: " + paciente.getNome());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Tem certeza que deseja excluir o paciente " + paciente.getNome() + "?");
        alert.setContentText("Esta ação não pode ser desfeita.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                pacienteService.excluirPaciente(paciente.getId());
                messageLabel.setText("Paciente '" + paciente.getNome() + "' excluído com sucesso!");
                messageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                listarDadosPaciente(); // Recarrega a tabela para remover o paciente excluído
            } catch (Exception e) {
                messageLabel.setText("Erro ao excluir paciente: " + e.getMessage());
                messageLabel.setTextFill(javafx.scene.paint.Color.RED);
                System.err.println("Erro ao excluir paciente: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erro de Exclusão", "Não foi possível excluir o paciente.", e.getMessage());
            }
        }
    }



    @FXML
    private void handleSearchPatientButton(ActionEvent event) {
        String searchText = searchPatientField.getText().trim();
        if (searchText.isEmpty()) {
            listarDadosPaciente(); // Se o campo de busca estiver vazio, recarrega todos os pacientes
            messageLabel.setText("Campo de busca vazio. Exibindo todos os pacientes.");
            messageLabel.setTextFill(javafx.scene.paint.Color.BLACK);
            return;
        }

        try {

            List<Paciente> resultados = pacienteService.listarPacientes().stream()
                    .filter(p -> p.getNome().toLowerCase().contains(searchText.toLowerCase()) ||
                            p.getCpf().contains(searchText)) // Busca por nome ou CPF
                    .toList(); // Usa toList() do Java 16+

            patientData.clear();
            patientData.addAll(resultados);

            if (resultados.isEmpty()) {
                messageLabel.setText("Nenhum paciente encontrado para '" + searchText + "'.");
                messageLabel.setTextFill(javafx.scene.paint.Color.ORANGE);
            } else {
                messageLabel.setText(resultados.size() + " paciente(s) encontrado(s) para '" + searchText + "'.");
                messageLabel.setTextFill(javafx.scene.paint.Color.BLACK);
            }
        } catch (Exception e) {
            messageLabel.setText("Erro ao buscar pacientes: " + e.getMessage());
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegisterPatientButton(ActionEvent event) {
        System.out.println("Botão 'Cadastrar Paciente' clicado!");
        messageLabel.setText("Abrindo formulário de cadastro de paciente.");
        messageLabel.setTextFill(javafx.scene.paint.Color.BLACK);
        loadCadastroPacienteView(null); // Passa null para indicar que é um novo cadastro
    }


    private void loadCadastroPacienteView(Paciente paciente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CadastroPacienteView.fxml"));
            Parent root = loader.load();

            // Obtenha o controlador da tela de cadastro
            CadastroPacienteController cadastroController = loader.getController();
            // CHAVE PARA RESOLVER O ERRO "Cannot resolve method 'setPacienteParaEdicao'"
            if (paciente != null) {
                cadastroController.setPacienteParaEdicao(paciente); // Define o paciente para edição
            }


            Stage stage = (Stage) patientTableView.getScene().getWindow(); // Alterado para pegar o stage da TableView
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Clínica - " + (paciente != null ? "Editar Paciente" : "Cadastro de Paciente"));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela de cadastro/edição de paciente: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de cadastro/edição.", e.getMessage());
        }
    }



    @FXML
    private void handlePacientesButton(ActionEvent event) {
        System.out.println("Botão 'Pacientes' clicado (recarrega a própria tela de Pacientes).");

        listarDadosPaciente();
        messageLabel.setText("Tabela de pacientes atualizada.");
        messageLabel.setTextFill(javafx.scene.paint.Color.BLACK);
    }

    @FXML
    private void handleMedicosButton(ActionEvent event) {
        loadView("/views/MedicoView.fxml", "Gerenciar Pacientes", event);
    }

    @FXML
    private void handleConsultasButton(ActionEvent event) {
        loadView("/views/ConsultaView.fxml", "Gerenciar Pacientes", event);
    }

    @FXML
    private void handleRelatoriosButton(ActionEvent event) {
        loadView("/views/RelatorioView.fxml", "Gerenciar Pacientes", event);
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void loadView(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Clínica - " + title);


            stage.setMaximized(true);

            stage.show(); // Exibe a janela (na maioria dos casos, já estará visível)
        } catch (IOException e) {
            e.printStackTrace();
            // Exibe um alerta de erro para o usuário se o carregamento do FXML falhar
            System.err.println("Erro ao carregar a tela: " + fxmlPath + " - " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Navegação");
            alert.setHeaderText("Não foi possível carregar a próxima tela.");
            alert.setContentText("Verifique se o arquivo FXML existe e o caminho está correto: " + fxmlPath);
            alert.showAndWait();
        } catch (Exception e) { // Captura quaisquer outras exceções inesperadas durante o carregamento/exibição
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Inesperado");
            alert.setHeaderText("Ocorreu um erro ao tentar abrir a tela.");
            alert.setContentText("Detalhes: " + e.getMessage());
            alert.showAndWait();
        }
    }
}