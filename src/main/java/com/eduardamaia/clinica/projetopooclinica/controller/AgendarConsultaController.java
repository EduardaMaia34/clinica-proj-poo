package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Consulta;
import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.repository.ConsultaRepository;
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

    private ObservableList<Paciente> listaPacientes = FXCollections.observableArrayList();
    private ObservableList<Medico> listaMedicos = FXCollections.observableArrayList();

    private final PacienteRepository pacienteRepository = new PacienteRepository();
    private final MedicoRepository medicoRepository = new MedicoRepository();
    private final ConsultaRepository consultasRepository = new ConsultaRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarPacientes();
        carregarMedicos();
    }

    private void carregarPacientes() {
        listaPacientes.addAll(pacienteRepository.listarTodos());
        comboPaciente.setItems(listaPacientes);
        // Optional: Set a cell factory to display patient names properly if ComboBox shows full object toString()
        comboPaciente.setCellFactory(lv -> new ListCell<Paciente>() {
            @Override
            protected void updateItem(Paciente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNome());
            }
        });
        comboPaciente.setButtonCell(new ListCell<Paciente>() {
            @Override
            protected void updateItem(Paciente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNome());
            }
        });
    }

    private void carregarMedicos() {
        listaMedicos.addAll(medicoRepository.listarTodos());
        comboMedico.setItems(listaMedicos);
        // Optional: Set a cell factory to display doctor names properly
        comboMedico.setCellFactory(lv -> new ListCell<Medico>() {
            @Override
            protected void updateItem(Medico item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNome());
            }
        });
        comboMedico.setButtonCell(new ListCell<Medico>() {
            @Override
            protected void updateItem(Medico item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNome());
            }
        });
    }

    @FXML
    private void handleSalvar() {
        if (!validarCampos()) return;

        try {
            Paciente pacienteSelecionado = comboPaciente.getSelectionModel().getSelectedItem();
            Medico medicoSelecionado = comboMedico.getSelectionModel().getSelectedItem();
            LocalDate data = datePickerData.getValue();
            LocalTime hora = LocalTime.parse(campoHora.getText(), DateTimeFormatter.ofPattern("HH:mm"));

            // --- ESTE É O BLOCO DE CÓDIGO QUE PRECISA DE MUDANÇA ---
            // A sua entidade Consultas espera objetos Paciente e Medico, não apenas os IDs.
            Consulta novaConsulta = new Consulta(
                    pacienteSelecionado, // Passa o objeto Paciente selecionado
                    medicoSelecionado,   // Passa o objeto Medico selecionado
                    data,
                    hora.toString()
            );
            // Remova as linhas antigas que causavam o erro:
            // novaConsulta.setPaciente(pacienteSelecionado.getId());
            // novaConsulta.setMedico(medicoSelecionado.getId());
            // novaConsulta.setData(LocalDate.parse(data.toString())); // data já é LocalDate, não precisa parsear de novo
            // novaConsulta.setHora(hora.toString()); // Hora já é LocalTime, converter para String se o setter esperar String

            consultasRepository.salvar(novaConsulta);
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Consulta agendada com sucesso!");
            fecharJanela();

        } catch (DateTimeParseException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Formato", "A hora deve estar no formato HH:mm (ex: 14:30).");
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao agendar consulta: " + e.getMessage());
            e.printStackTrace(); // Always good to print stack trace during development
        }
    }

    @FXML
    private void handleCancelar() {
        fecharJanela();
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