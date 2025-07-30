package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Consultas;
import com.eduardamaia.clinica.projetopooclinica.entities.Medico; // Import Medico
import com.eduardamaia.clinica.projetopooclinica.entities.Paciente; // Import Paciente
import com.eduardamaia.clinica.projetopooclinica.service.ConsultasService;
import com.eduardamaia.clinica.projetopooclinica.repository.MedicoRepository; // Import MedicoRepository
import com.eduardamaia.clinica.projetopooclinica.repository.PacienteRepository; // Import PacienteRepository

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty; // Add this line
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException; // Added for robust date parsing
import java.util.Optional;
import java.util.List;

public class ConsultasController {
    @FXML
    private TableView<Consultas> tabelaConsultas;
    @FXML
    private TableColumn<Consultas, Integer> colunaId;
    @FXML
    private TableColumn<Consultas, String> colunaPacienteNome; // Change to display name
    @FXML
    private TableColumn<Consultas, String> colunaMedicoNome;   // Change to display name
    @FXML
    private TableColumn<Consultas, LocalDate> colunaData;     // Change to LocalDate
    @FXML
    private TableColumn<Consultas, String> colunaHora;

    @FXML
    private TextField campoId;
    // Changed to ComboBox for selecting existing Pacientes/Medicos by object, not ID
    @FXML
    private ComboBox<Paciente> campoPaciente; // Changed from TextField to ComboBox
    @FXML
    private ComboBox<Medico> campoMedico;     // Changed from TextField to ComboBox
    @FXML
    private DatePicker campoDataPicker;       // Changed from TextField to DatePicker
    @FXML
    private TextField campoHora;

    @FXML
    private Button botaoNovo;
    @FXML
    private Button botaoSalvar;
    @FXML
    private Button botaoDeletar;
    @FXML
    private Button botaoEditar;

    private ConsultasService ConsultasService;
    private PacienteRepository pacienteRepository = new PacienteRepository(); // Initialize
    private MedicoRepository medicoRepository = new MedicoRepository();       // Initialize

    private ObservableList<Consultas> ObservableListConsultas;
    private ObservableList<Paciente> listaPacientesComboBox = FXCollections.observableArrayList();
    private ObservableList<Medico> listaMedicosComboBox = FXCollections.observableArrayList();


    @FXML
    private void initialize(){
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // To display names from related entities in TableView
        colunaPacienteNome.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPaciente() != null ?
                        cellData.getValue().getPaciente().getNome() : "N/A"));
        colunaMedicoNome.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMedico() != null ?
                        cellData.getValue().getMedico().getNome() : "N/A"));

        colunaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colunaHora.setCellValueFactory(new PropertyValueFactory<>("hora"));

        tabelaConsultas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarConsultaSelecionada(newValue)); // Corrected method name

        // Populate ComboBoxes with existing data
        carregarPacientesNoComboBox();
        carregarMedicosNoComboBox();
    }

    private void carregarPacientesNoComboBox() {
        listaPacientesComboBox.addAll(pacienteRepository.listarTodos());
        campoPaciente.setItems(listaPacientesComboBox);
        campoPaciente.setCellFactory(lv -> new ListCell<Paciente>() {
            @Override
            protected void updateItem(Paciente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNome());
            }
        });
        campoPaciente.setButtonCell(new ListCell<Paciente>() {
            @Override
            protected void updateItem(Paciente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNome());
            }
        });
    }

    private void carregarMedicosNoComboBox() {
        listaMedicosComboBox.addAll(medicoRepository.listarTodos());
        campoMedico.setItems(listaMedicosComboBox);
        campoMedico.setCellFactory(lv -> new ListCell<Medico>() {
            @Override
            protected void updateItem(Medico item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNome());
            }
        });
        campoMedico.setButtonCell(new ListCell<Medico>() {
            @Override
            protected void updateItem(Medico item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNome());
            }
        });
    }


    public ConsultasController(){
        // This constructor will be called first, services are set later
    }

    public void setConsultasService(ConsultasService consultasService) {
        this.ConsultasService = consultasService;
        // Agora que o serviço está disponível, podemos carregar os dados.
        atualizarTabela();
    }

    private void atualizarTabela(){
        if(ConsultasService == null){
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ", "o serviço de consultas não foi inicializado");
            return;
        }
        List<Consultas> lista = ConsultasService.listarTodasConsultas();
        ObservableListConsultas = FXCollections.observableArrayList(lista);
        tabelaConsultas.setItems(ObservableListConsultas);
    }

    @FXML
    private void handleSalvarConsulta() {
        try {
            // Get selected objects directly from ComboBoxes
            Paciente pacienteSelecionado = campoPaciente.getSelectionModel().getSelectedItem();
            Medico medicoSelecionado = campoMedico.getSelectionModel().getSelectedItem();
            LocalDate data = campoDataPicker.getValue(); // Get value from DatePicker
            String hora = campoHora.getText();

            if (pacienteSelecionado == null || medicoSelecionado == null || data == null || hora.isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Validação", "Todos os campos (Paciente, Médico, Data, Hora) são obrigatórios.");
                return;
            }

            Consultas consulta = new Consultas();
            consulta.setPaciente(pacienteSelecionado); // Pass the Paciente object
            consulta.setMedico(medicoSelecionado);     // Pass the Medico object
            consulta.setData(data);                   // Data is already LocalDate
            consulta.setHora(hora);

            // Determine if it's a new or existing consultation
            if (campoId.getText() == null || campoId.getText().isEmpty()) {
                ConsultasService.criarConsulta(consulta);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Consulta agendada com sucesso!");
            } else {
                Integer id = Integer.parseInt(campoId.getText()); // Use Integer for ID
                ConsultasService.atualizarConsulta(id, consulta); // Assumes your service method takes ID and entity
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Consulta atualizada com sucesso!");
            }
            limparFormulario();
            atualizarTabela();
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Formato", "ID da consulta deve ser um número inteiro, se fornecido.");
        } catch (DateTimeParseException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Formato", "Formato de data inválido. Use AAAA-MM-DD.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro Inesperado", "Ocorreu um erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditarConsulta(){
        Consultas consultaSelecionada = tabelaConsultas.getSelectionModel().getSelectedItem();

        if (consultaSelecionada != null) {
            campoId.setText(String.valueOf(consultaSelecionada.getId()));
            // Set ComboBox selections by object, not ID
            campoPaciente.getSelectionModel().select(consultaSelecionada.getPaciente());
            campoMedico.getSelectionModel().select(consultaSelecionada.getMedico());
            campoDataPicker.setValue(consultaSelecionada.getData()); // Set DatePicker value
            campoHora.setText(consultaSelecionada.getHora());
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Nenhuma Seleção", "Por favor, selecione uma consulta na tabela para editar.");
        }
    }

    @FXML
    private void handleNovaConsulta(){
        limparFormulario(); // Reusing the clear form method
        campoPaciente.requestFocus(); // Focus on the first input field
    }

    @FXML
    private void handleDeletarConsulta(){
        Consultas consultaSelecionada = tabelaConsultas.getSelectionModel().getSelectedItem();

        if(consultaSelecionada == null){
            mostrarAlerta(Alert.AlertType.WARNING,"Nenhuma seleção ",
                    "Por favor, selecione a consulta que deseja deletar.");
            return;
        }
        Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de exclusão");
        alert.setHeaderText("Você está prestes a deletar uma consulta");
        alert.setContentText("Tem certeza que deseja remover a consulta de id " + consultaSelecionada.getId() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                ConsultasService.deletarConsulta(consultaSelecionada.getId());
                atualizarTabela();
                limparFormulario(); // Use the dedicated method
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Consulta deletada com sucesso!");

            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro ao Deletar", "Não foi possível deletar a consulta. Erro: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Renamed for clarity and to avoid confusion with `mostrarConsultasSelecionada`
    private void mostrarConsultaSelecionada(Consultas consulta) {
        if (consulta != null) {
            campoId.setText(String.valueOf(consulta.getId()));
            // Set ComboBox selections by object, not by ID
            campoPaciente.getSelectionModel().select(consulta.getPaciente());
            campoMedico.getSelectionModel().select(consulta.getMedico());
            campoDataPicker.setValue(consulta.getData()); // Set DatePicker value
            campoHora.setText(consulta.getHora());
        } else {
            limparFormulario(); // Clear if no selection
        }
    }

    @FXML
    private void handleAgendarConsulta() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/AgendarConsultaView.fxml"));
            Parent dialogPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Agendar Nova Consulta");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);

            dialogStage.showAndWait();
            atualizarTabela();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro ao Abrir Agendamento", "Não foi possível abrir a tela de agendamento.", e.getMessage());
        }
    }

    private void limparFormulario() {
        tabelaConsultas.getSelectionModel().clearSelection();
        campoId.clear();
        campoPaciente.getSelectionModel().clearSelection(); // Clear ComboBox selection
        campoMedico.getSelectionModel().clearSelection();   // Clear ComboBox selection
        campoDataPicker.setValue(null);                     // Clear DatePicker
        campoHora.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // --- Navigation Methods (no changes needed here but included for context) ---
    @FXML
    private void handlePacientesButton(ActionEvent event) {
        System.out.println("Botão 'Pacientes' clicado! Navegando...");
        loadView("/views/PacienteView.fxml", "Gerenciar Pacientes", event);
    }

    @FXML
    private void handleMedicosButton(ActionEvent event) {
        loadView("/views/MedicoView.fxml", "Gerenciar Medico", event);
    }

    @FXML
    private void handleConsultasButton(ActionEvent event) {
        loadView("/views/ConsultasView.fxml", "Gerenciar Consultas", event);
    }

    @FXML
    private void handleRelatoriosButton(ActionEvent event) {
        loadView("/views/RelatorioView.fxml", "Visualizar Relatórios", event);
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

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}