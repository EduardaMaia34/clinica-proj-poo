package com.eduardamaia.clinica.projetopooclinica.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.entities.Usuario;
import com.eduardamaia.clinica.projetopooclinica.padroes.MedicoDataChangeListener;
import com.eduardamaia.clinica.projetopooclinica.service.MedicoService;
import com.eduardamaia.clinica.projetopooclinica.util.SessionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class MedicoController implements Initializable, MedicoDataChangeListener {

    @FXML
    private TextField searchMedicoField;

    @FXML
    private Button searchButton;

    @FXML
    private Button registerMedicoButton;

    @FXML
    private TableView<Medico> medicoTableView;

    @FXML
    private TableColumn<Medico, Integer> idColumn;
    @FXML
    private TableColumn<Medico, String> nomeColumn;
    @FXML
    private TableColumn<Medico, String> cpfColumn;
    @FXML
    private TableColumn<Medico, String> enderecoColumn;
    @FXML
    private TableColumn<Medico, Double> valorConsultaColumn;
    @FXML
    private TableColumn<Medico, String> codigoConselhoColumn;

    @FXML
    private TableColumn<Medico, Void> editColumn;
    @FXML
    private TableColumn<Medico, Void> deleteColumn;

    @FXML
    private Label messageLabel;

    private final ObservableList<Medico> medicoData = FXCollections.observableArrayList();
    private MedicoService medicoService;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("MedicoController inicializado!");

        this.medicoService = new MedicoService();
        this.medicoService.addMedicoDataChangeListener(this); //padrao de projeto observador

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        enderecoColumn.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        valorConsultaColumn.setCellValueFactory(new PropertyValueFactory<>("valorConsulta"));
        codigoConselhoColumn.setCellValueFactory(new PropertyValueFactory<>("codigoConselho"));

        if (editColumn != null) {
            editColumn.setCellFactory(param -> new TableCell<Medico, Void>() {
                private final Button editButton = new Button("Editar");
                {
                    editButton.setOnAction(event -> {
                        Medico medico = getTableView().getItems().get(getIndex());
                        handleEditMedico(medico);
                    });
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {

                        editButton.setVisible(getCurrentUserIsAdmin());
                        setGraphic(editButton);
                    }
                }
            });
        }


        if (deleteColumn != null) {
            deleteColumn.setCellFactory(param -> new TableCell<Medico, Void>() {
                private final Button deleteButton = new Button("Excluir");
                {
                    deleteButton.setOnAction(event -> {
                        Medico medico = getTableView().getItems().get(getIndex());
                        handleDeleteMedico(medico);
                    });
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        // Only show the delete button if the current user is an admin
                        deleteButton.setVisible(getCurrentUserIsAdmin());
                        setGraphic(deleteButton);
                    }
                }
            });
        }

        medicoTableView.setItems(medicoData);

        boolean isAdmin = getCurrentUserIsAdmin();
        registerMedicoButton.setVisible(isAdmin);
        registerMedicoButton.setManaged(isAdmin);

        listarDadosMedicos();
        System.out.println("Status de administrador (no initialize): " + isAdmin + ". Botão 'Cadastrar Médico' visível: " + registerMedicoButton.isVisible());
    }


    private void listarDadosMedicos() {
        try {
            List<Medico> medicosDoBanco = medicoService.listarMedicos();
            medicoData.clear();
            medicoData.addAll(medicosDoBanco);
            messageLabel.setText("Dados dos médicos carregados do banco de dados.");
            messageLabel.setTextFill(javafx.scene.paint.Color.BLACK);
        } catch (Exception e) {
            messageLabel.setText("Erro ao carregar médicos do banco de dados: " + e.getMessage());
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            System.err.println("Erro ao carregar médicos: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Carregamento", "Não foi possível carregar os médicos.", e.getMessage());
        }
    }

    private void handleEditMedico(Medico medico) {
        if (!getCurrentUserIsAdmin()) {
            showAlert(Alert.AlertType.WARNING, "Acesso Negado", "Permissão Insuficiente", "Apenas administradores podem editar médicos.");
            return;
        }
        System.out.println("Editar médico: " + medico.getNome());
        messageLabel.setText("Editando médico: " + medico.getNome());
        messageLabel.setTextFill(javafx.scene.paint.Color.BLUE);

        loadCadastroMedicoView(medico);
    }

    private void handleDeleteMedico(Medico medico) {
        if (!getCurrentUserIsAdmin()) {
            showAlert(Alert.AlertType.WARNING, "Acesso Negado", "Permissão Insuficiente", "Apenas administradores podem excluir médicos.");
            return;
        }
        System.out.println("Excluir médico: " + medico.getNome());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Tem certeza que deseja excluir o médico " + medico.getNome() + "?");
        alert.setContentText("Esta ação não pode ser desfeita.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                medicoService.excluirMedico(medico.getId());

            } catch (Exception e) {
                messageLabel.setText("Erro ao excluir médico: " + e.getMessage());
                messageLabel.setTextFill(javafx.scene.paint.Color.RED);
                System.err.println("Erro ao excluir médico: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erro de Exclusão", "Não foi possível excluir o médico.", e.getMessage());
            }
        }
    }

    @FXML
    private void handleSearchMedicoButton(ActionEvent event) {
        String searchText = searchMedicoField.getText().trim();
        if (searchText.isEmpty()) {
            listarDadosMedicos();
            messageLabel.setText("Campo de busca vazio. Exibindo todos os médicos.");
            messageLabel.setTextFill(javafx.scene.paint.Color.BLACK);
            return;
        }

        try {
            List<Medico> resultados = medicoService.buscarMedicosPorNomeOuCodigo(searchText);

            medicoData.clear();
            medicoData.addAll(resultados);

            if (resultados.isEmpty()) {
                messageLabel.setText("Nenhum médico encontrado para '" + searchText + "'.");
                messageLabel.setTextFill(javafx.scene.paint.Color.ORANGE);
            } else {
                messageLabel.setText(resultados.size() + " médico(s) encontrado(s) para '" + searchText + "'.");
                messageLabel.setTextFill(javafx.scene.paint.Color.BLACK);
            }
        } catch (Exception e) {
            messageLabel.setText("Erro ao buscar médicos: " + e.getMessage());
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Busca", "Não foi possível realizar a busca.", e.getMessage());
        }
    }

    @FXML
    private void handleRegisterMedicoButton(ActionEvent event) {
        loadCadastroMedicoView(null);
    }

    private void loadCadastroMedicoView(Medico medico) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CadastroMedicoView.fxml"));
            Parent root = loader.load();

            CadastroMedicoController cadastroController = loader.getController();

            if (medico != null) {
                cadastroController.setMedicoParaEdicao(medico);
            }

            Stage stage = (Stage) medicoTableView.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Clínica - " + (medico != null ? "Editar Médico" : "Cadastro de Médico"));
            stage.setMaximized(true);
            stage.show();

            stage.setOnHidden(e -> {
                onMedicoDataChanged(null, false); // chama o método do observador para atualizar a tabela
            });
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela de cadastro/edição de médico: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de cadastro/edição de médico.", e.getMessage());
        }
    }

    @FXML
    private void handlePacientesButton(ActionEvent event) {
        System.out.println("Botão 'Pacientes' clicado! Navegando...");
        loadView("/views/PacienteView.fxml", "Gerenciar Pacientes", event);
    }

    @FXML
    private void handleMedicosButton(ActionEvent event) {
        System.out.println("Botão 'Médicos' clicado (já na tela de Médicos).");

        boolean isAdmin = getCurrentUserIsAdmin();
        registerMedicoButton.setVisible(isAdmin);
        registerMedicoButton.setManaged(isAdmin);
        System.out.println("Status de administrador (handleMedicosButton): " + isAdmin + ". Botão 'Cadastrar Médico' visível: " + registerMedicoButton.isVisible());

        listarDadosMedicos();
        messageLabel.setText("Tabela de médicos atualizada.");
        messageLabel.setTextFill(javafx.scene.paint.Color.BLACK);
    }

    @FXML
    private void handleConsultasButton(ActionEvent event) {

        loadView("/views/ConsultaView.fxml", "Gerenciar Consultas", event);
    }

    @FXML
    private void handleRelatoriosButton(ActionEvent event) {
        loadView("/views/RelatorioView.fxml", "Visualizar Relatórios", event);
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

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Clínica - " + title);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela: " + fxmlPath + " - " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela.", e.getMessage());
        }
    }

    private boolean getCurrentUserIsAdmin() {
        Usuario loggedInUser = SessionManager.getLoggedInUser();

        return loggedInUser != null && loggedInUser.getAdministrador() != null && loggedInUser.getAdministrador();
    }

    @Override
    public void onMedicoDataChanged(String medicoNome, boolean isDeleted) {

        System.out.println("Atualizando tabela.");
        listarDadosMedicos();

        String message;
        javafx.scene.paint.Color textColor;

        if (isDeleted) {
            message = "Médico '" + (medicoNome != null ? medicoNome : "desconhecido") + "' excluído com sucesso!";
            textColor = javafx.scene.paint.Color.ORANGERED;
        } else {

            message = "Dados dos médicos atualizados.";
            textColor = javafx.scene.paint.Color.BLACK;
        }

        messageLabel.setText(message);
        messageLabel.setTextFill(javafx.scene.paint.Color.ORANGERED);
    }
}