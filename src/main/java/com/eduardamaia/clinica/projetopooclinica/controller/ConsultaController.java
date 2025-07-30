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
    private TableColumn<Consulta, String> colunaHora; // Mantido como String para corresponder à sua entidade
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

    /**
     * CORREÇÃO PRINCIPAL: Este método agora abre a tela de agendamento.
     */
    @FXML
    private void handleAgendarConsulta() {
        try {
            // Carrega o arquivo FXML da tela de agendamento
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AgendarConsultaView.fxml"));
            Parent root = loader.load();

            // Cria um novo palco (Stage) para a janela de diálogo
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Agendar Nova Consulta");
            dialogStage.initModality(Modality.WINDOW_MODAL); // Bloqueia a janela principal
            dialogStage.initOwner(botaoAgendar.getScene().getWindow()); // Define a janela "pai"

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            // Exibe a janela e espera até que ela seja fechada
            dialogStage.showAndWait();

            // Após fechar a janela, atualiza a tabela para mostrar a nova consulta
            atualizarTabela();

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao Abrir Tela", "Não foi possível carregar a tela de agendamento.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditarConsulta() {
        Consulta consultaSelecionada = tabelaConsultas.getSelectionModel().getSelectedItem();
        if (consultaSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nenhuma Seleção", "Por favor, selecione uma consulta na tabela para editar.");
            return;
        }
        // Aqui você abriria a tela de formulário, passando a 'consultaSelecionada'
        System.out.println("Editando consulta: " + consultaSelecionada.getId());
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

    // --- MÉTODOS DE NAVEGAÇÃO ---
    @FXML
    private void handlePacientesButton(ActionEvent event) {
        loadView("/views/PacienteView.fxml", "Gerenciar Pacientes", event);
    }

    @FXML
    private void handleMedicosButton(ActionEvent event) {
        loadView("/views/MedicoView.fxml", "Gerenciar Médicos", event);
    }

    @FXML
    private void handleConsultasButton(ActionEvent event) {
        loadView("/views/ConsultaView.fxml", "Gerenciar Consultas", event);
    }

    @FXML
    private void handleRelatoriosButton(ActionEvent event) {
        loadView("/views/RelatorioView.fxml", "Visualizar Relatórios", event);
    }

    private void loadView(String fxmlPath, String title, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Control) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Clínica - " + title);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela: " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}