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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    //endregion

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
    @FXML
    private TableColumn<Relatorio, LocalDate> colPeriodo2;
    //endregion

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
    private TextArea textAreaConteudo; // ADICIONADO: Campo que estava faltando
    @FXML
    private Button btnNovo;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnExcluir;
    //endregion

    private RelatorioService relatorioService;
    private MedicoService medicoService;
    private PacienteService pacienteService;
    private ObservableList<Relatorio> obsListRelatorios;
    private ObservableList<Medico> obsListMedicos;
    private ObservableList<Paciente> obsListPacientes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.relatorioService = new RelatorioService();
        this.medicoService = new MedicoService();
        this.pacienteService = new PacienteService();

        initializeNodes();
        loadMedicoComboBoxes();
        loadPacienteComboBoxes();
        updateTableView();
    }

    private void initializeNodes() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPeriodo1.setCellValueFactory(new PropertyValueFactory<>("periodo1"));
        colPeriodo2.setCellValueFactory(new PropertyValueFactory<>("periodo2"));

        colMedico.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedico().getNome()));
        colPaciente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaciente().getNome()));

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
        List<Medico> medicoList = medicoService.listarMedicos();
        obsListMedicos = FXCollections.observableArrayList(medicoList);
        comboBoxMedico.setItems(obsListMedicos);
        comboBuscaMedico.setItems(obsListMedicos);
    }

    private void loadPacienteComboBoxes() {
        List<Paciente> pacienteList = pacienteService.listarPacientes();
        obsListPacientes = FXCollections.observableArrayList(pacienteList);
        comboBoxPaciente.setItems(obsListPacientes);
        comboBuscaPaciente.setItems(obsListPacientes);
    }

    @FXML
    public void onBtnNovoAction() {
        clearForm();
        tableViewRelatorios.getSelectionModel().clearSelection();
        comboBoxMedico.requestFocus();
    }

    @FXML
    public void onBtnSalvarAction() {
        try {
            Relatorio relatorio = getFormData();
            // Validação simples
            if (relatorio.getMedico() == null || relatorio.getPaciente() == null || relatorio.getPeriodo1() == null || relatorio.getPeriodo2() == null) {
                throw new IllegalArgumentException("Todos os campos, exceto o conteúdo, são obrigatórios.");
            }
            if(relatorio.getPeriodo1().isAfter(relatorio.getPeriodo2())){
                throw new IllegalArgumentException("A data de início não pode ser posterior à data final.");
            }

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

        Optional<ButtonType> result = showConfirmationDialog("Confirmação de Exclusão",
                "Você está prestes a excluir o relatório ID: " + selectedRelatorio.getId(),
                "Você tem certeza que deseja continuar?");

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

    // LÓGICA DE BUSCA MELHORADA: Permite combinar filtros
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

    private Relatorio getFormData() {
        Relatorio relatorio = new Relatorio();
        if (textFieldId.getText() != null && !textFieldId.getText().trim().isEmpty()) {
            relatorio.setId(Integer.parseInt(textFieldId.getText()));
        }
        relatorio.setMedico(comboBoxMedico.getValue());
        relatorio.setPaciente(comboBoxPaciente.getValue());
        relatorio.setPeriodo1(datePickerPeriodo1.getValue());
        relatorio.setPeriodo2(datePickerPeriodo2.getValue());
        relatorio.setConteudo(textAreaConteudo.getText()); // ATUALIZADO
        return relatorio;
    }

    private void updateFormData(Relatorio relatorio) {
        if (relatorio != null) {
            textFieldId.setText(String.valueOf(relatorio.getId()));
            comboBoxMedico.setValue(relatorio.getMedico());
            comboBoxPaciente.setValue(relatorio.getPaciente());
            datePickerPeriodo1.setValue(relatorio.getPeriodo1());
            datePickerPeriodo2.setValue(relatorio.getPeriodo2());
            textAreaConteudo.setText(relatorio.getConteudo()); // ATUALIZADO
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
        textAreaConteudo.clear(); // ATUALIZADO
    }

    private void showAlertDialog(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmationDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    //region Métodos de Navegação
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
        // Já está na tela, apenas recarrega os dados
        updateTableView();
        clearForm();
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
    //endregion
}