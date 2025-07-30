package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Consulta;
import com.eduardamaia.clinica.projetopooclinica.repository.ConsultaRepository;
import com.eduardamaia.clinica.projetopooclinica.service.ConsultaService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class ConsultaController {

    @FXML
    private TableView<Consulta> tabelaConsultas;
    @FXML
    private TableColumn<Consulta, Integer> colunaId;
    @FXML
    private TableColumn<Consulta, String> colunaPaciente;
    @FXML
    private TableColumn<Consulta, String> colunaMedico;
    @FXML
    private TableColumn<Consulta, LocalDate> colunaData;
    @FXML
    private TableColumn<Consulta, String> colunaHora;
    @FXML
    private Button botaoAgendar;
    @FXML
    private Button botaoEditar;
    @FXML
    private Button botaoDeletar;

    private final ConsultaRepository consultaRepository = new ConsultaRepository();
    private final ConsultaService consultaService = new ConsultaService(consultaRepository);
    private ObservableList<Consulta> observableListConsultas;

    @FXML
    private void initialize() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colunaHora.setCellValueFactory(new PropertyValueFactory<>("hora"));

        colunaPaciente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaciente().getNome()));
        colunaMedico.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedico().getNome()));

        atualizarTabela();
    }

    private void atualizarTabela() {
        List<Consulta> lista = consultaService.listarTodasConsultas();
        observableListConsultas = FXCollections.observableArrayList(lista);
        tabelaConsultas.setItems(observableListConsultas);
    }

    // --- AÇÕES DOS BOTÕES ---

    @FXML
    private void handleAgendarConsulta() {
        abrirFormularioConsulta(null); // Passa null para indicar que é uma nova consulta
    }

    /**
     * CORREÇÃO PRINCIPAL: Este método agora abre o formulário e passa a consulta selecionada.
     */
    @FXML
    private void handleEditarConsulta() {
        Consulta consultaSelecionada = tabelaConsultas.getSelectionModel().getSelectedItem();
        if (consultaSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nenhuma Seleção", "Por favor, selecione uma consulta na tabela para editar.");
            return;
        }
        abrirFormularioConsulta(consultaSelecionada); // Passa o objeto selecionado
    }

    /**
     * Método centralizado para abrir o formulário de consulta, seja para criar ou editar.
     * @param consulta A consulta a ser editada, ou null se for para criar uma nova.
     */
    private void abrirFormularioConsulta(Consulta consulta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AgendarConsultaView.fxml"));
            Parent root = loader.load();

            // Pega o controller da tela de agendamento
            AgendarConsultaController controller = loader.getController();

            // Se uma consulta foi passada, configura o formulário para edição
            if (consulta != null) {
                controller.setConsultaParaEditar(consulta);
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle(consulta == null ? "Agendar Nova Consulta" : "Editar Consulta");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(botaoAgendar.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            dialogStage.showAndWait();

            // Atualiza a tabela principal após fechar o formulário
            atualizarTabela();

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao Abrir Tela", "Não foi possível carregar o formulário de consulta.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeletarConsulta() {
        Consulta consultaSelecionada = tabelaConsultas.getSelectionModel().getSelectedItem();
        if (consultaSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nenhuma Seleção", "Por favor, selecione a consulta que deseja deletar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Deletar consulta do paciente " + consultaSelecionada.getPaciente().getNome() + "?");
        alert.setContentText("Tem certeza que deseja remover esta consulta?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                consultaService.deletarConsulta(consultaSelecionada.getId());
                atualizarTabela();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Consulta deletada com sucesso!");
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro ao Deletar", "Não foi possível deletar a consulta: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // --- MÉTODOS DE NAVEGAÇÃO E ALERTA ---
    // (sem alterações)
    private void loadView(String fxmlPath, String title, ActionEvent event) { /* ... */ }
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) { /* ... */ }
    @FXML private void handlePacientesButton(ActionEvent event) { loadView("/views/PacienteView.fxml", "Gerenciar Pacientes", event); }
    @FXML private void handleMedicosButton(ActionEvent event) { loadView("/views/MedicoView.fxml", "Gerenciar Médicos", event); }
    @FXML private void handleConsultasButton(ActionEvent event) { loadView("/views/ConsultaView.fxml", "Gerenciar Consultas", event); }
    @FXML private void handleRelatoriosButton(ActionEvent event) { loadView("/views/RelatorioView.fxml", "Visualizar Relatórios", event); }
}