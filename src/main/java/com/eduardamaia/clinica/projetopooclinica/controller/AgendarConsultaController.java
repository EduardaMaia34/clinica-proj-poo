package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Consultas;
import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;


import com.eduardamaia.clinica.projetopooclinica.repository.ConsultasRepository;
import com.eduardamaia.clinica.projetopooclinica.repository.MedicoRepository;
import com.eduardamaia.clinica.projetopooclinica.repository.PacienteRepository;
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
import java.util.List;
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

    // Listas para armazenar os dados do banco
    private ObservableList<Paciente> listaPacientes;
    private ObservableList<Medico> listaMedicos;

    private final PacienteRepository PacienteRepository;
    private final MedicoRepository MedicoRepository;

    public AgendarConsultaController(PacienteRepository pacienteRepository,
                                     MedicoRepository medicoRepository) {
        this.PacienteRepository = pacienteRepository;
        this.MedicoRepository = medicoRepository;
    }


    /**
     * Este método é chamado automaticamente quando a tela FXML é carregada.
     * É o lugar ideal para preparar a tela, como carregar dados nos ComboBoxes.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Carrega os pacientes e médicos do banco de dados e os exibe nos ComboBoxes
        carregarPacientes();
        carregarMedicos();
    }

    /**
     * Ação do botão "Salvar".
     * Valida os campos, cria um objeto Consulta e o salva no banco.
     */
    @FXML
    private void handleSalvar() {
        if (!validarCampos()) {
            return; // Se a validação falhar, interrompe a execução
        }

        try {
            Paciente pacienteSelecionado = comboPaciente.getSelectionModel().getSelectedItem();
            Medico medicoSelecionado = comboMedico.getSelectionModel().getSelectedItem();
            LocalDate data = datePickerData.getValue();
            LocalTime hora = LocalTime.parse(campoHora.getText(), DateTimeFormatter.ofPattern("HH:mm"));

            Consultas novaConsulta = new Consultas();
            novaConsulta.setPaciente(pacienteSelecionado.getId());
            novaConsulta.setMedico(medicoSelecionado.getId());
            novaConsulta.setData(String.valueOf(data));
            novaConsulta.setHora(String.valueOf(hora));

            ConsultasRepository consultasRepository = new ConsultasRepository();
            consultasRepository.salvar(novaConsulta);
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Consulta agendada com sucesso!");

            fecharJanela();

        } catch (DateTimeParseException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Formato", "A hora deve estar no formato HH:mm (ex: 14:30).");
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro Inesperado", "Ocorreu um erro ao salvar a consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ação do botão "Cancelar". Simplesmente fecha a janela.
     */
    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    private void carregarPacientes() {
        comboPaciente.setItems(listaPacientes);
    }


    private void carregarMedicos() {
        comboMedico.setItems(listaMedicos);
    }


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