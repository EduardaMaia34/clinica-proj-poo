package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Consulta;
import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.repository.ConsultaRepository;
import com.eduardamaia.clinica.projetopooclinica.repository.MedicoRepository;
import com.eduardamaia.clinica.projetopooclinica.repository.PacienteRepository;
import com.eduardamaia.clinica.projetopooclinica.service.ConsultaService;

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
    private final ConsultaRepository consultaRepository = new ConsultaRepository();
    private final ConsultaService consultaService = new ConsultaService(consultaRepository);

    // CORREÇÃO: Campo para guardar a consulta que está sendo editada
    private Consulta consultaParaEditar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarPacientes();
        carregarMedicos();
    }

    /**
     * CORREÇÃO: Novo método para receber a consulta do controller principal e preencher o formulário.
     */
    public void setConsultaParaEditar(Consulta consulta) {
        this.consultaParaEditar = consulta;

        // Preenche os campos do formulário com os dados da consulta existente
        comboPaciente.setValue(consulta.getPaciente());
        comboMedico.setValue(consulta.getMedico());
        datePickerData.setValue(consulta.getData());
        campoHora.setText(consulta.getHora());
    }

    @FXML
    private void handleSalvar() {
        if (!validarCampos()) {
            return;
        }

        try {
            Paciente pacienteSelecionado = comboPaciente.getSelectionModel().getSelectedItem();
            Medico medicoSelecionado = comboMedico.getSelectionModel().getSelectedItem();
            LocalDate data = datePickerData.getValue();
            String horaTexto = campoHora.getText();
            LocalTime.parse(horaTexto, DateTimeFormatter.ofPattern("HH:mm"));

            // CORREÇÃO: Lógica para decidir se deve criar ou atualizar
            if (consultaParaEditar == null) {
                // Modo CRIAÇÃO: cria uma nova consulta
                Consulta novaConsulta = new Consulta(pacienteSelecionado, medicoSelecionado, data, horaTexto);
                consultaService.criarConsulta(novaConsulta);
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Consulta agendada com sucesso!");
            } else {
                // Modo EDIÇÃO: atualiza a consulta existente
                consultaParaEditar.setPaciente(pacienteSelecionado);
                consultaParaEditar.setMedico(medicoSelecionado);
                consultaParaEditar.setData(data);
                consultaParaEditar.setHora(horaTexto);
                consultaService.atualizarConsulta(consultaParaEditar.getId(), consultaParaEditar);
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Consulta atualizada com sucesso!");
            }

            fecharJanela();

        } catch (DateTimeParseException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Formato", "A hora deve estar no formato HH:mm (ex: 14:30).");
        } catch (IllegalStateException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Conflito de Horário", e.getMessage());
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro Inesperado", "Não foi possível salvar a consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Métodos de ajuda e carregamento (sem alterações) ---
    private void carregarPacientes() { /* ... */ }
    private void carregarMedicos() { /* ... */ }
    @FXML private void handleCancelar() { fecharJanela(); }
    private boolean validarCampos() { /* ... */ return true; }
    private void exibirAlerta(Alert.AlertType tipo, String titulo, String conteudo) { /* ... */ }
    private void fecharJanela() { /* ... */ }
    private void configurarDisplayComboBoxPaciente() { /* ... */ }
    private void configurarDisplayComboBoxMedico() { /* ... */ }
}