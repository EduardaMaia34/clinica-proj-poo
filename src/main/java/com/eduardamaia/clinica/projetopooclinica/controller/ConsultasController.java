package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Consultas;
import com.eduardamaia.clinica.projetopooclinica.service.ConsultasService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import java.time.LocalDate;
import java.util.Optional;

import java.util.List;

public class ConsultasController {
    @FXML
    private TableView<Consultas> tabelaConsultas;
    @FXML
    private TableColumn<Consultas, Integer> colunaId;
    @FXML
    private TableColumn<Consultas, Integer> colunaPacienteId;
    @FXML
    private TableColumn<Consultas, Integer> colunaMedicoId;
    @FXML
    private TableColumn<Consultas,String> colunaData;
    @FXML
    private TableColumn<Consultas, String> colunaHora;

    @FXML
    private TextField campoId;
    @FXML
    private TextField campoPacienteId;
    @FXML
    private TextField campoMedicoId;
    @FXML
    private TextField campoData;
    @FXML
    private TextField campoHora;

    @FXML
    private Button botaoNovo;
    @FXML
    private Button botaoSalvar;
    @FXML
    private Button botaoDeletar;
    @FXML
    private Button botaoEditar;

    private ConsultasService ConsultasService;

    //lista observavel para para preencher a tabela e reagir a mudanças
    private ObservableList<Consultas> ObservableListConsultas;

    @FXML
    private void initialize(){
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaPacienteId.setCellValueFactory(new PropertyValueFactory<>("pacienteid"));
        colunaMedicoId.setCellValueFactory(new PropertyValueFactory<>("medicoid"));
        colunaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colunaHora.setCellValueFactory(new PropertyValueFactory<>("hora"));

        tabelaConsultas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                        mostrarConsultasSelecionada(newValue));
    }

    private void mostrarConsultasSelecionada(Consultas newValue) {
    }

    public ConsultasController(){
    }
    public void setConsultasService(ConsultasService consultasService) {
        this.ConsultasService = consultasService;
        // Agora que o serviço está disponível, podemos carregar os dados.
        atualizarTabela();
    }
    private void atualizarTabela(){
        if(ConsultasService == null){
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ", "o serviço de consultas não foi inicializado");
            return;
        }
        List<Consultas> lista = ConsultasService.listarTodasConsultas();
        ObservableListConsultas = FXCollections.observableArrayList(lista);
        tabelaConsultas.setItems(ObservableListConsultas);
    }

    @FXML
    private void handleSalvarConsulta() {
        try {
            int pacienteId = Integer.parseInt(campoPacienteId.getText());
            int medicoId = Integer.parseInt(campoMedicoId.getText());
            String data = campoData.getText();
            String hora = campoHora.getText();

            if (data.isEmpty() || hora.isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Validação", "Data e Hora são campos obrigatórios.");
                return;
            }

            Consultas consulta = new Consultas();
            consulta.setPaciente(pacienteId);
            consulta.setMedico(medicoId);
            consulta.setData(LocalDate.parse(data));
            consulta.setHora(hora);

            if (campoId.getText() == null || campoId.getText().isEmpty()) {
                ConsultasService.criarConsulta(consulta);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Consulta agendada com sucesso!");
            } else {
                int id = Integer.parseInt(campoId.getText());
                ConsultasService.atualizarConsulta(id, consulta);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Consulta atualizada com sucesso!");
            }
            limparFormulario();
            atualizarTabela();
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Formato", "ID do Paciente e do Médico devem ser números inteiros.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro Inesperado", "Ocorreu um erro: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditarConsulta(){
        Consultas consultaSelecionada = tabelaConsultas.getSelectionModel().getSelectedItem();

        // Verifica se algo foi realmente selecionado
        if (consultaSelecionada != null) {
            // Preenche os campos de texto com os dados do objeto selecionado
            campoId.setText(String.valueOf(consultaSelecionada.getId()));
            campoPacienteId.setText(String.valueOf(consultaSelecionada.getPaciente()));
            campoMedicoId.setText(String.valueOf(consultaSelecionada.getMedico()));
            campoData.setText(String.valueOf(consultaSelecionada.getData()));
            campoHora.setText(consultaSelecionada.getHora());
        } else {
            // Se nada estiver selecionado, avisa o usuário
            mostrarAlerta(Alert.AlertType.WARNING, "Nenhuma Seleção", "Por favor, selecione uma consulta na tabela para editar.");
        }
    }
    @FXML
    private void handleNovaConsulta(){
        tabelaConsultas.getSelectionModel().clearSelection();

        campoId.clear();
        campoMedicoId.clear();
        campoPacienteId.clear();
        campoData.clear();
        campoHora.clear();

        campoPacienteId.requestFocus();
    }
    @FXML
    private void handleDeletarConsulta(){
        Consultas consultaSelecionada = tabelaConsultas.getSelectionModel().getSelectedItem();

        if(consultaSelecionada==null){
            mostrarAlerta(Alert.AlertType.WARNING,"Nenhuma seleção ",
                    "Por favor, selecione a consulta que deseja deletar.");
            return;
        }
        Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de exclusão");
        alert.setHeaderText("Você está prestes a deletar uma consulta");
        alert.setContentText("Tem certeza que deseja remover a consulta de id " + consultaSelecionada.getId() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        // Se o usuário confirmar a exclusão (clicando em "OK")
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Chama o serviço para deletar a consulta do banco de dados
                ConsultasService.deletarConsulta(consultaSelecionada.getId());
                atualizarTabela();
                // Limpa o formulário
                handleNovaConsulta();

                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Consulta deletada com sucesso!");

            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro ao Deletar", "Não foi possível deletar a consulta. Erro: " + e.getMessage());
            }
        }
    }

    private void mostrarConsultaSelecionada(Consultas consulta) {
        if (consulta != null) {
            campoId.setText(String.valueOf(consulta.getId()));
            campoPacienteId.setText(String.valueOf(consulta.getPaciente()));
            campoMedicoId.setText(String.valueOf(consulta.getMedico()));
            campoData.setText(String.valueOf(consulta.getData()));
            campoHora.setText(consulta.getHora());
        }
    }

    @FXML
    private void handleAgendarConsulta() {
        try {
            // Carrega o arquivo FXML do diálogo
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/AgendarConsultaView.fxml"));
            Parent dialogPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Agendar Nova Consulta");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);

            dialogStage.showAndWait();
            atualizarTabela(); // linha para atualizar a lista de consultas

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void limparFormulario() {
        tabelaConsultas.getSelectionModel().clearSelection();
        campoId.clear();
        campoPacienteId.clear();
        campoMedicoId.clear();
        campoData.clear();
        campoHora.clear();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void handlePacientesButton(ActionEvent event) {
        System.out.println("Botão 'Pacientes' clicado! Navegando...");
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

        loadView("/views/RelatorioView.fxml", "Visualizar Relatórios", event);
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
            System.err.println("Erro ao carregar a tela: " + fxmlPath + " - " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela.", e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
