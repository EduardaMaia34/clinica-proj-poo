package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Consulta; // CORREÇÃO: Usando a entidade correta (singular)
import com.eduardamaia.clinica.projetopooclinica.repository.ConsultaRepository;
import com.eduardamaia.clinica.projetopooclinica.service.ConsultaService; // CORREÇÃO: Usando o service correto (singular)
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

public class ConsultasController {

    // --- FXML Fields que REALMENTE existem no FXML ---
    @FXML
    private TableView<Consulta> tabelaConsultas;

    // CORREÇÃO: Os @FXML agora correspondem exatamente aos fx:id do FXML
    @FXML
    private TableColumn<Consulta, Integer> colunaId;
    @FXML
    private TableColumn<Consulta, String> colunaPaciente;
    @FXML
    private TableColumn<Consulta, String> colunaMedico;
    @FXML
    private TableColumn<Consulta, LocalDate> colunaData;
    @FXML
    private TableColumn<Consulta, LocalTime> colunaHora;

    @FXML
    private Button botaoAgendar;
    @FXML
    private Button botaoEditar;
    @FXML
    private Button botaoDeletar;

    private final ConsultaRepository consultaRepository = new ConsultaRepository();
    private final ConsultaService consultaService = new ConsultaService(consultaRepository);
    private ConsultaRepository Consulta;
    // --- Serviços e Listas ---
    // CORREÇÃO: Renomeado para seguir o padrão Java (minúsculo)
    private ObservableList<Consulta> observableListConsultas;

    @FXML
    private void initialize() {
        // Configura como cada coluna da tabela vai obter seus dados
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colunaHora.setCellValueFactory(new PropertyValueFactory<>("hora"));

        // CORREÇÃO: Lógica para exibir o NOME do paciente/médico, não o objeto inteiro.
        // Isso pega o objeto Consulta, navega até o objeto Paciente/Medico e pega o nome.
        colunaPaciente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaciente().getNome()));
        colunaMedico.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedico().getNome()));

        // Carrega os dados na tabela
        atualizarTabela();
    }

    /**
     * Busca todas as consultas no banco de dados e atualiza a tabela.
     */
    private void atualizarTabela() {
        if (consultaService == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Serviço", "O serviço de consultas não foi inicializado.");
            return;
        }
        List<Consulta> lista = consultaService.listarTodasConsultas();
        observableListConsultas = FXCollections.observableArrayList(lista);
        tabelaConsultas.setItems(observableListConsultas);
    }

    // --- Ações dos Botões ---

    @FXML
    private void handleAgendarConsulta() {
        // Lógica para abrir uma nova janela/modal para agendar uma consulta
        System.out.println("Botão Agendar Clicado!");
        // Aqui você chamaria uma nova tela FXML de formulário
    }

    @FXML
    private void handleEditarConsulta() {
        Consulta consultaSelecionada = tabelaConsultas.getSelectionModel().getSelectedItem();
        if (consultaSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nenhuma Seleção", "Por favor, selecione uma consulta na tabela para editar.");
            return;
        }
        // Aqui você abriria a tela de formulário, passando a 'consultaSelecionada' para preencher os campos
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
        alert.setHeaderText("Você está prestes a deletar a consulta do paciente " + consultaSelecionada.getPaciente().getNome() + ".");
        alert.setContentText("Tem certeza que deseja remover esta consulta?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                consultaService.deletarConsulta(consultaSelecionada.getId());
                atualizarTabela();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Consulta deletada com sucesso!");
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro ao Deletar", "Não foi possível deletar a consulta. Erro: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // --- Métodos de Navegação (mantidos como estavam) ---

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
        // Já estamos aqui, mas podemos recarregar se necessário
        loadView("/views/ConsultasView.fxml", "Gerenciar Consultas", event);
    }

    @FXML
    private void handleRelatoriosButton(ActionEvent event) {
        loadView("/views/RelatorioView.fxml", "Visualizar Relatórios", event);
    }

    private void loadView(String fxmlPath, String title, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Control) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
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