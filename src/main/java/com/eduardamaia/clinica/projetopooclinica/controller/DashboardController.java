package com.eduardamaia.clinica.projetopooclinica.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("DashboardController initialized!");
        // Qualquer lógica de configuração para a visualização do dashboard pode ir aqui,
        // por exemplo, carregar dados, definir estados iniciais.
    }

    // --- Button Action Handlers ---

    @FXML
    private void handlePacientesButton(ActionEvent event) {
        // Carrega e exibe a tela de Pacientes
        loadView("/views/PacienteView.fxml", "Gerenciar Pacientes", event);
    }

    @FXML
    private void handleMedicosButton(ActionEvent event) {
        System.out.println("Botão 'Médicos' clicado!");
        // Carrega e exibe a tela de Médicos
        // loadView("views/MedicosView.fxml", "Gerenciar Médicos", event);
    }

    @FXML
    private void handleConsultasButton(ActionEvent event) {
        System.out.println("Botão 'Consultas' clicado!");
        // Carrega e exibe a tela de Consultas
        // loadView("views/ConsultasView.fxml", "Gerenciar Consultas", event);
    }

    @FXML
    private void handleRelatoriosButton(ActionEvent event) {
        System.out.println("Botão 'Relatórios' clicado!");
        // Carrega e exibe a tela de Relatórios
        // loadView("/views/RelatoriosView.fxml", "Visualizar Relatórios", event);
    }


    private void loadView(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Pega o Stage atual a partir da fonte do evento
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Clínica - " + title);


            stage.setMaximized(true);

            stage.show(); // Exibe a janela (na maioria dos casos, já estará visível)
        } catch (IOException e) {
            e.printStackTrace();
            // Exibe um alerta de erro para o usuário se o carregamento do FXML falhar
            System.err.println("Erro ao carregar a tela: " + fxmlPath + " - " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Navegação");
            alert.setHeaderText("Não foi possível carregar a próxima tela.");
            alert.setContentText("Verifique se o arquivo FXML existe e o caminho está correto: " + fxmlPath);
            alert.showAndWait();
        } catch (Exception e) { // Captura quaisquer outras exceções inesperadas durante o carregamento/exibição
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Inesperado");
            alert.setHeaderText("Ocorreu um erro ao tentar abrir a tela.");
            alert.setContentText("Detalhes: " + e.getMessage());
            alert.showAndWait();
        }
    }

}