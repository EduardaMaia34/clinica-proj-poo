package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Consultas;
import com.eduardamaia.clinica.projetopooclinica.service.ConsultasService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ConsultasController {

    @FXML
    private TableView<Consultas> tabelaconsultas;
    @FXML
    private TableColumn<Consultas, Integer> colunaId;
    @FXML
    private TableColumn<Consultas, Integer> colunapacienteId;
    @FXML
    private TableColumn<Consultas, Integer> colunamedicoId;
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
    private Button agendar;
    @FXML
    private Button cancelar;
    @FXML
    private Button deletar;

    private ConsultasService ConsultasService;

    //lista observavel para para preencher a tabela e reagir a mudanças
    private ObservableList<Consultas> ObservableListConsultas;

    private void Initialize(){
        colunaId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colunaId.setCellValueFactory(new PropertyValueFactory<>("pacienteID"));
        colunaId.setCellValueFactory(new PropertyValueFactory<>("medicoID"));
        colunaId.setCellValueFactory(new PropertyValueFactory<>("Data"));
        colunaId.setCellValueFactory(new PropertyValueFactory<>("Hora"));

        tabelaconsultas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                        mostrarConsultasSelecionada(newValue));
    }
    public ConsultasController(){
    }
    public void setConsultasService(ConsultasService consultasService) {
        this.consultasService = consultasService;
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
    private void handleSalvarConsulta(){
        try{
            int pacienteId = Integer.parseInt(campoPacienteId.getText());
        }
    }

}
