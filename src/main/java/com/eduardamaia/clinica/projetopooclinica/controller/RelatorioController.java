package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.entities.Relatorio;
import com.eduardamaia.clinica.projetopooclinica.service.MedicoService; // Assumindo que esta classe existe
import com.eduardamaia.clinica.projetopooclinica.service.PacienteService; // Assumindo que esta classe existe
import com.eduardamaia.clinica.projetopooclinica.service.RelatorioService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RelatorioController implements Initializable {

    //region FXML Injections - Filtros de Busca
    @FXML
    private ComboBox<Medico> comboBuscaMedico;
    @FXML
    private ComboBox<Paciente> comboBuscaPaciente;
    @FXML
    private DatePicker datePickerBuscaPeriodo1;
    @FXML
    private DatePicker datePickerBuscaPeriodo2;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnLimparFiltro;

    //region FXML Injections - Tabela
    @FXML
    private TableView<Relatorio> tableViewRelatorios;
    @FXML
    private TableColumn<Relatorio, Integer> colId;
    @FXML
    private TableColumn<Relatorio, String> colMedico;
    @FXML
    private TableColumn<Relatorio, String> colPaciente;
    @FXML
    private TableColumn<Relatorio, LocalDate> colPeriodo1;

    //region FXML Injections - Formulário de Dados
    @FXML
    private TextField textFieldId;
    @FXML
    private ComboBox<Medico> comboBoxMedico;
    @FXML
    private ComboBox<Paciente> comboBoxPaciente;
    @FXML
    private DatePicker datePickerPeriodo1;
    @FXML
    private DatePicker datePickerPeriodo2;
    @FXML
    private TextArea textAreaConteudo;
    @FXML
    private Button btnNovo;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnExcluir;

    private RelatorioService relatorioService;
    private MedicoService medicoService;
    private PacienteService pacienteService;
    private ObservableList<Relatorio> obsListRelatorios;
    private ObservableList<Medico> obsListMedicos;
    private ObservableList<Paciente> obsListPacientes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Instancia os serviços
        this.relatorioService = new RelatorioService();
        this.medicoService = new MedicoService(); // Dependência necessária
        this.pacienteService = new PacienteService(); // Dependência necessária

        initializeNodes();
        loadMedicoComboBoxes();
        loadPacienteComboBoxes();
        updateTableView();
    }

    private void initializeNodes() {
        // Configura as colunas da tabela
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPeriodo1.setCellValueFactory(new PropertyValueFactory<>("periodo1"));

        // Para exibir o nome do médico/paciente em vez do objeto
        colMedico.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedico().getNome()));
        colPaciente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaciente().getNome()));

        // Adiciona um listener para atualizar o formulário ao selecionar um item na tabela
        tableViewRelatorios.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> updateFormData(newValue)
        );
    }

    private void updateTableView() {
        List<Relatorio> list = relatorioService.listartodos();
        obsListRelatorios = FXCollections.observableArrayList(list);
        tableViewRelatorios.setItems(obsListRelatorios);
        tableViewRelatorios.refresh();
    }

    private void loadMedicoComboBoxes() {
        List<Medico> medicoList = medicoService.listarMedicos(); // Assumindo que este método existe
        obsListMedicos = FXCollections.observableArrayList(medicoList);
        comboBoxMedico.setItems(obsListMedicos);
        comboBuscaMedico.setItems(obsListMedicos);
    }

    private void loadPacienteComboBoxes() {
        List<Paciente> pacienteList = pacienteService.listarPacientes(); // Assumindo que este método existe
        obsListPacientes = FXCollections.observableArrayList(pacienteList);
        comboBoxPaciente.setItems(obsListPacientes);
        comboBuscaPaciente.setItems(obsListPacientes);
    }

    @FXML
    public void onBtnNovoAction() {
        clearForm();
        tableViewRelatorios.getSelectionModel().clearSelection();
    }

    @FXML
    public void onBtnSalvarAction() {
        try {
            Relatorio relatorio = getFormData();
            if (relatorio.getId() == 0) {
                relatorioService.criarRelatorio(relatorio);
                showAlertDialog(Alert.AlertType.INFORMATION, "Sucesso", "Relatório criado com sucesso!");
            } else {
                relatorioService.atualizarRelatorio(relatorio.getId(), relatorio);
                showAlertDialog(Alert.AlertType.INFORMATION, "Sucesso", "Relatório atualizado com sucesso!");
            }
            updateTableView();
            clearForm();
        } catch (IllegalArgumentException e) {
            showAlertDialog(Alert.AlertType.ERROR, "Erro de Validação", e.getMessage());
        }
    }

    @FXML
    public void onBtnExcluirAction() {
        Relatorio selectedRelatorio = tableViewRelatorios.getSelectionModel().getSelectedItem();
        if (selectedRelatorio == null) {
            showAlertDialog(Alert.AlertType.WARNING, "Nenhuma Seleção", "Por favor, selecione um relatório para excluir.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Você está prestes a excluir o relatório ID: " + selectedRelatorio.getId());
        alert.setContentText("Você tem certeza que deseja continuar?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (relatorioService.excluirRelatorio(selectedRelatorio.getId())) {
                showAlertDialog(Alert.AlertType.INFORMATION, "Sucesso", "Relatório excluído com sucesso!");
                updateTableView();
                clearForm();
            } else {
                showAlertDialog(Alert.AlertType.ERROR, "Erro", "Não foi possível excluir o relatório.");
            }
        }
    }

    @FXML
    public void onBtnBuscarAction() {
        List<Relatorio> filteredList;
        Medico medico = comboBuscaMedico.getValue();
        Paciente paciente = comboBuscaPaciente.getValue();
        LocalDate inicio = datePickerBuscaPeriodo1.getValue();
        LocalDate fim = datePickerBuscaPeriodo2.getValue();

        if (medico != null) {
            filteredList = relatorioService.buscarPorMedico(medico);
        } else if (paciente != null) {
            filteredList = relatorioService.buscarPorPaciente(paciente);
        } else if (inicio != null && fim != null) {
            try {
                filteredList = relatorioService.buscarPorPeriodo(inicio, fim);
            } catch (IllegalArgumentException e) {
                showAlertDialog(Alert.AlertType.ERROR, "Erro de Busca", e.getMessage());
                return;
            }
        } else {
            showAlertDialog(Alert.AlertType.WARNING, "Filtro Incompleto", "Selecione um médico, paciente ou um período de datas para buscar.");
            return;
        }
        obsListRelatorios = FXCollections.observableArrayList(filteredList);
        tableViewRelatorios.setItems(obsListRelatorios);
    }

    @FXML
    public void onBtnLimparFiltroAction() {
        comboBuscaMedico.setValue(null);
        comboBuscaPaciente.setValue(null);
        datePickerBuscaPeriodo1.setValue(null);
        datePickerBuscaPeriodo2.setValue(null);
        updateTableView();
    }

    private Relatorio getFormData() {
        Relatorio relatorio = new Relatorio();
        if (textFieldId.getText() != null && !textFieldId.getText().trim().isEmpty()) {
            relatorio.setId(Integer.parseInt(textFieldId.getText()));
        }
        relatorio.setMedico(comboBoxMedico.getValue());
        relatorio.setPaciente(comboBoxPaciente.getValue());
        relatorio.setPeriodo1(datePickerPeriodo1.getValue());
        relatorio.setPeriodo2(datePickerPeriodo2.getValue());
        relatorio.setConteudo(textAreaConteudo.getText());
        return relatorio;
    }

    private void updateFormData(Relatorio relatorio) {
        if (relatorio != null) {
            textFieldId.setText(String.valueOf(relatorio.getId()));
            comboBoxMedico.setValue(relatorio.getMedico());
            comboBoxPaciente.setValue(relatorio.getPaciente());
            datePickerPeriodo1.setValue(relatorio.getPeriodo1());
            datePickerPeriodo2.setValue(relatorio.getPeriodo2());
            textAreaConteudo.setText(relatorio.getConteudo());
        } else {
            clearForm();
        }
    }

    private void clearForm() {
        textFieldId.clear();
        comboBoxMedico.setValue(null);
        comboBoxPaciente.setValue(null);
        datePickerPeriodo1.setValue(null);
        datePickerPeriodo2.setValue(null);
        textAreaConteudo.clear();
    }

    private void showAlertDialog(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

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