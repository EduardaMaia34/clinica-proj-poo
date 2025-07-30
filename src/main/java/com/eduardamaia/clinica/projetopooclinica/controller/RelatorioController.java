package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.entities.Relatorio;
import com.eduardamaia.clinica.projetopooclinica.service.MedicoService;
import com.eduardamaia.clinica.projetopooclinica.service.PacienteService;
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
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RelatorioController implements Initializable {

    //region FXML Injections - Filtros de Busca (Mantidos)
    @FXML
    private ComboBox<Medico> comboBuscaMedico;
    @FXML
    private ComboBox<Paciente> comboBuscaPaciente;
    @FXML
    private DatePicker datePickerBuscaPeriodo1;
    @FXML
    private DatePicker datePickerBuscaPeriodo2;
    //endregion

    //region FXML Injections - Tabela (Mantidos)
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
    @FXML
    private TableColumn<Relatorio, LocalDate> colPeriodo2;
    //endregion

    // As injeções FXML para o formulário foram REMOVIDAS

    private RelatorioService relatorioService;
    private MedicoService medicoService;
    private PacienteService pacienteService;
    private ObservableList<Relatorio> obsListRelatorios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.relatorioService = new RelatorioService();
        this.medicoService = new MedicoService();
        this.pacienteService = new PacienteService();

        initializeNodes();
        loadFilterComboBoxes();
        updateTableView();
    }

    private void initializeNodes() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPeriodo1.setCellValueFactory(new PropertyValueFactory<>("periodo1"));
        colPeriodo2.setCellValueFactory(new PropertyValueFactory<>("periodo2"));

        colMedico.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedico().getNome()));
        colPaciente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaciente().getNome()));

        // O listener para atualizar o formulário foi REMOVIDO
    }

    private void updateTableView() {
        List<Relatorio> list = relatorioService.listartodos();
        obsListRelatorios = FXCollections.observableArrayList(list);
        tableViewRelatorios.setItems(obsListRelatorios);
        tableViewRelatorios.refresh();
    }

    private void loadFilterComboBoxes() {
        List<Medico> medicoList = medicoService.listarMedicos();
        comboBuscaMedico.setItems(FXCollections.observableArrayList(medicoList));

        List<Paciente> pacienteList = pacienteService.listarPacientes();
        comboBuscaPaciente.setItems(FXCollections.observableArrayList(pacienteList));
    }

    // Os métodos onBtnNovoAction, onBtnSalvarAction e onBtnExcluirAction foram REMOVIDOS

    @FXML
    public void onBtnBuscarAction() {
        List<Relatorio> filteredList = relatorioService.listartodos();

        Medico medicoFiltro = comboBuscaMedico.getValue();
        if (medicoFiltro != null) {
            filteredList = filteredList.stream()
                    .filter(r -> r.getMedico().equals(medicoFiltro))
                    .collect(Collectors.toList());
        }

        Paciente pacienteFiltro = comboBuscaPaciente.getValue();
        if (pacienteFiltro != null) {
            filteredList = filteredList.stream()
                    .filter(r -> r.getPaciente().equals(pacienteFiltro))
                    .collect(Collectors.toList());
        }

        LocalDate inicio = datePickerBuscaPeriodo1.getValue();
        LocalDate fim = datePickerBuscaPeriodo2.getValue();
        if (inicio != null && fim != null) {
            if (inicio.isAfter(fim)) {
                showAlertDialog(Alert.AlertType.ERROR, "Erro de Busca", "A data de início não pode ser posterior à data de fim.");
                return;
            }
            filteredList = filteredList.stream()
                    .filter(r -> !r.getPeriodo1().isBefore(inicio) && !r.getPeriodo2().isAfter(fim))
                    .collect(Collectors.toList());
        } else if (inicio != null || fim != null) {
            showAlertDialog(Alert.AlertType.WARNING, "Filtro Incompleto", "Para filtrar por período, ambas as datas (início e fim) devem ser preenchidas.");
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

    // Os métodos getFormData, updateFormData e clearForm foram REMOVIDOS

    private void showAlertDialog(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //region Métodos de Navegação (Mantidos)
    @FXML
    private void handlePacientesButton(ActionEvent event) {
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
        // Apenas recarrega a tabela, pois já estamos na tela
        updateTableView();
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
            System.err.println("Erro ao carregar a tela: " + fxmlPath);
            e.printStackTrace();
            showAlertDialog(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela: " + title);
        }
    }
}