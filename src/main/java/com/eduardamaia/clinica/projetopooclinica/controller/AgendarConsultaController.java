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
import javafx.util.StringConverter;

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
    private final ConsultaRepository consultaRepository = ConsultaRepository.getInstance();
    private final ConsultaService consultaService = new ConsultaService(consultaRepository);

    private Consulta consultaParaEditar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Carrega os dados e DEPOIS configura como eles são exibidos
        carregarPacientes();
        carregarMedicos();
        configurarDisplayComboBoxes();
    }

   //garante que os nomes sejam exibidos corretamente
    private void configurarDisplayComboBoxes() {
        //configura como exibir o nome do Paciente
        comboPaciente.setConverter(new StringConverter<>() {
            @Override
            public String toString(Paciente paciente) {
                return paciente == null ? "" : "Paciente: " + paciente.getNome() + " (Prontuário: " + paciente.getProntuario() + ")";
            }

            @Override
            public Paciente fromString(String string) {
                return null;
            }
        });

        //configura como exibir o nome do Médico
        comboMedico.setConverter(new StringConverter<>() {
            @Override
            public String toString(Medico medico) {
                return medico == null ? "" : "Médico: " + medico.getNome() + " (CRM: " + medico.getCodigoConselho() + ")";
            }

            @Override
            public Medico fromString(String string) {
                return null; // Não precisamos converter de String para Médico
            }
        });
    }


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

            if (consultaParaEditar == null) {
                Consulta novaConsulta = new Consulta(pacienteSelecionado, medicoSelecionado, data, horaTexto);
                consultaService.criarConsulta(novaConsulta);
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Consulta agendada com sucesso!");
            } else {
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

    private void carregarPacientes() {
        ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList(pacienteRepository.listarTodos());
        comboPaciente.setItems(listaPacientes);
    }
    private void carregarMedicos() {
        ObservableList<Medico> listaMedicos = FXCollections.observableArrayList(medicoRepository.listarTodos());
        comboMedico.setItems(listaMedicos);
    }
    @FXML private void handleCancelar() { fecharJanela(); }
    private boolean validarCampos() {
        if (comboPaciente.getSelectionModel().isEmpty() ||
                comboMedico.getSelectionModel().isEmpty() ||
                datePickerData.getValue() == null ||
                campoHora.getText().isBlank()) {
            exibirAlerta(Alert.AlertType.WARNING, "Campos Incompletos", "Por favor, preencha todos os campos.");
            return false;
        }
        return true;
    }
    private void exibirAlerta(Alert.AlertType tipo, String titulo, String conteudo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
    private void fecharJanela() {
        Stage stage = (Stage) botaoSalvar.getScene().getWindow();
        stage.close();
    }
}
