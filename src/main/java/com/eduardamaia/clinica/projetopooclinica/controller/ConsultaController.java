package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Consulta;
import com.eduardamaia.clinica.projetopooclinica.repository.ConsultaRepository;
import com.eduardamaia.clinica.projetopooclinica.service.ConsultaService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    @FXML
    private void handleAgendarConsulta() {
        abrirFormularioConsulta(null);
    }

    @FXML
    private void handleEditarConsulta() {
        Consulta consultaSelecionada = tabelaConsultas.getSelectionModel().getSelectedItem();
        if (consultaSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nenhuma Seleção", "Por favor, selecione uma consulta para editar.");
            return;
        }
        abrirFormularioConsulta(consultaSelecionada);
    }

    private void abrirFormularioConsulta(Consulta consulta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AgendarConsultaView.fxml"));
            Parent root = loader.load();

            AgendarConsultaController controller = loader.getController();
            if (consulta != null) {
                controller.setConsultaParaEditar(consulta);
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle(consulta == null ? "Agendar Nova Consulta" : "Editar Consulta");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(botaoAgendar.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

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


    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}