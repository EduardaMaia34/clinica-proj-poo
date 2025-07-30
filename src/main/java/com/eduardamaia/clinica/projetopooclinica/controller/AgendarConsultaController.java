package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Consulta;
import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.repository.ConsultaRepository;
import com.eduardamaia.clinica.projetopooclinica.repository.MedicoRepository;
import com.eduardamaia.clinica.projetopooclinica.repository.PacienteRepository;
import com.eduardamaia.clinica.projetopooclinica.service.ConsultaService; // Importar o serviço

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class AgendarConsultaController implements Initializable {

    @FXML
    private ComboBox<Paciente> comboPaciente;
    @FXML
    private ComboBox<Medico> comboMedico;
    @FXML
    private DatePicker datePickerData;
    @FXML
    private TextField campoHora;
    @FXML
    private Button botaoSalvar;

    private final PacienteRepository pacienteRepository = new PacienteRepository();
    private final MedicoRepository medicoRepository = new MedicoRepository();
    // CORREÇÃO: Usar o serviço para encapsular a lógica de negócio
    private final ConsultaRepository consultaRepository = new ConsultaRepository();
    private final ConsultaService consultaService = new ConsultaService(consultaRepository);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarPacientes();
        carregarMedicos();
    }

    private void carregarPacientes() {
        ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList(pacienteRepository.listarTodos());
        comboPaciente.setItems(listaPacientes);
        configurarDisplayComboBoxPaciente();
    }

    private void carregarMedicos() {
        ObservableList<Medico> listaMedicos = FXCollections.observableArrayList(medicoRepository.listarTodos());
        comboMedico.setItems(listaMedicos);
        configurarDisplayComboBoxMedico();
    }

    @FXML
    private void handleSalvar() {
        if (!validarCampos()) return;

        try {
            Paciente pacienteSelecionado = comboPaciente.getSelectionModel().getSelectedItem();
            Medico medicoSelecionado = comboMedico.getSelectionModel().getSelectedItem();
            LocalDate data = datePickerData.getValue();
            // Apenas valida o formato, mas salva como String, conforme a entidade
            String horaTexto = campoHora.getText();
            LocalTime.parse(horaTexto, DateTimeFormatter.ofPattern("HH:mm"));

            Consulta novaConsulta = new Consulta(
                    pacienteSelecionado,
                    medicoSelecionado,
                    data,
                    horaTexto
            );

            // CORREÇÃO: Usar o serviço para criar a consulta
            consultaService.criarConsulta(novaConsulta);

            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Consulta agendada com sucesso!");
            fecharJanela();

        } catch (DateTimeParseException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Formato", "A hora deve estar no formato HH:mm (ex: 14:30).");
        } catch (IllegalStateException e) { // Captura o erro de conflito do serviço
            exibirAlerta(Alert.AlertType.ERROR, "Conflito de Horário", e.getMessage());
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro Inesperado", "Erro ao agendar consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    // Métodos de ajuda (validar, alerta, fechar, etc.)
    private boolean validarCampos() { /* ... seu código de validação ... */ return true; }
    private void exibirAlerta(Alert.AlertType tipo, String titulo, String conteudo) { /* ... */ }
    private void fecharJanela() {
        Stage stage = (Stage) botaoSalvar.getScene().getWindow();
        stage.close();
    }
    private void configurarDisplayComboBoxPaciente() { /* ... */ }
    private void configurarDisplayComboBoxMedico() { /* ... */ }
}