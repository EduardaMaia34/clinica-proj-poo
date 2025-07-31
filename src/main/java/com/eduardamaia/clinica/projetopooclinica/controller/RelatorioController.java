package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Consulta;
import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.repository.ConsultaRepository;
import com.eduardamaia.clinica.projetopooclinica.repository.MedicoRepository;
import com.eduardamaia.clinica.projetopooclinica.repository.PacienteRepository;
import com.eduardamaia.clinica.projetopooclinica.service.ConsultaService;
import com.eduardamaia.clinica.projetopooclinica.service.MedicoService;
import com.eduardamaia.clinica.projetopooclinica.service.PacienteService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RelatorioController implements Initializable {

    @FXML
    private ComboBox<Medico> comboBuscaMedico;
    @FXML
    private ComboBox<Paciente> comboBuscaPaciente;
    @FXML
    private DatePicker datePickerBuscaInicio;
    @FXML
    private DatePicker datePickerBuscaFim;
    @FXML
    private TableView<Consulta> tableViewConsultas;
    @FXML
    private TableColumn<Consulta, Integer> colId;
    @FXML
    private TableColumn<Consulta, String> colPaciente;
    @FXML
    private TableColumn<Consulta, String> colMedico;
    @FXML
    private TableColumn<Consulta, LocalDate> colData;
    @FXML
    private TableColumn<Consulta, String> colHora;

    private final ConsultaRepository consultaRepository = new ConsultaRepository();

    private final ConsultaService consultaService = new ConsultaService(consultaRepository);
    private final MedicoService medicoService = new MedicoService();
    private final PacienteService pacienteService = new PacienteService();

    private ObservableList<Consulta> obsListConsultas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // A inicialização dos serviços agora é feita na declaração dos campos
        initializeNodes();
        loadFilterComboBoxes();
        updateTableView();
    }

    private void initializeNodes() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colPaciente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaciente().getNome()));
        colMedico.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedico().getNome()));
    }

    private void updateTableView() {
        List<Consulta> list = consultaService.listarTodasConsultas();
        obsListConsultas = FXCollections.observableArrayList(list);
        tableViewConsultas.setItems(obsListConsultas);
        tableViewConsultas.refresh();
    }

    private void loadFilterComboBoxes() {
        List<Medico> medicoList = medicoService.listarMedicos();
        comboBuscaMedico.setItems(FXCollections.observableArrayList(medicoList));
        configurarDisplayMedico(comboBuscaMedico);

        List<Paciente> pacienteList = pacienteService.listarPacientes();
        comboBuscaPaciente.setItems(FXCollections.observableArrayList(pacienteList));
        configurarDisplayPaciente(comboBuscaPaciente);
    }

    @FXML
    public void onBtnBuscarAction() {
        List<Consulta> listaFiltrada = consultaService.listarTodasConsultas();

        Medico medicoFiltro = comboBuscaMedico.getValue();
        if (medicoFiltro != null) {
            listaFiltrada = listaFiltrada.stream()
                    .filter(c -> c.getMedico().getId().equals(medicoFiltro.getId()))
                    .collect(Collectors.toList());
        }

        Paciente pacienteFiltro = comboBuscaPaciente.getValue();
        if (pacienteFiltro != null) {
            listaFiltrada = listaFiltrada.stream()
                    .filter(c -> c.getPaciente().getId().equals(pacienteFiltro.getId()))
                    .collect(Collectors.toList());
        }

        LocalDate dataInicio = datePickerBuscaInicio.getValue();
        LocalDate dataFim = datePickerBuscaFim.getValue();
        if (dataInicio != null && dataFim != null) {
            if (dataInicio.isAfter(dataFim)) {
                showAlertDialog(Alert.AlertType.ERROR, "Erro de Período", "A data de início não pode ser posterior à data de fim.");
                return;
            }
            listaFiltrada = listaFiltrada.stream()
                    .filter(c -> !c.getData().isBefore(dataInicio) && !c.getData().isAfter(dataFim))
                    .collect(Collectors.toList());
        }

        tableViewConsultas.setItems(FXCollections.observableArrayList(listaFiltrada));
    }

    @FXML
    public void onBtnLimparFiltroAction() {
        comboBuscaMedico.setValue(null);
        comboBuscaPaciente.setValue(null);
        datePickerBuscaInicio.setValue(null);
        datePickerBuscaFim.setValue(null);
        updateTableView();
    }

    private void showAlertDialog(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void configurarDisplayMedico(ComboBox<Medico> comboBox) {
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Medico medico) {
                return medico == null ? "Todos" : medico.getNome();
            }
            @Override
            public Medico fromString(String string) { return null; }
        });
    }

    private void configurarDisplayPaciente(ComboBox<Paciente> comboBox) {
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Paciente paciente) {
                return paciente == null ? "Todos" : paciente.getNome();
            }
            @Override
            public Paciente fromString(String string) { return null; }
        });
    }

    // Métodos para os botões do menu de navegação
    @FXML
    private void handlePacientesButton() {
        abrirNovaTela("/views/PacienteView.fxml", "Pacientes");
    }

    @FXML
    private void handleMedicosButton() {
        abrirNovaTela("/views/MedicoView.fxml", "Médicos");
    }

    @FXML
    private void handleConsultasButton() {
        abrirNovaTela("/views/ConsultaView.fxml", "Consultas");
    }

    @FXML
    private void handleRelatoriosButton() {
        // Já estamos na tela de relatórios, não faz nada
    }

    private void abrirNovaTela(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) tableViewConsultas.getScene().getWindow();
            stage.setTitle(titulo);
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            showAlertDialog(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela: " + titulo);
            e.printStackTrace();
        }
    }
}