package com.eduardamaia.clinica.projetopooclinica.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert; // Added for error alerts
import javafx.scene.control.Button; // If you want to access buttons programmatically
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("DashboardController initialized!");
        // Any setup logic for the dashboard view can go here, e.g., loading data, setting initial states.
    }

    // --- Button Action Handlers ---

    @FXML
    private void handlePacientesButton(ActionEvent event) {
        System.out.println("Botão 'Pacientes' clicado!");
        // Load and display the Pacientes screen
        //loadView("/views/PacienteView.fxml", "Gerenciar Pacientes", event);
    }

    @FXML
    private void handleMedicosButton(ActionEvent event) {
        System.out.println("Botão 'Médicos' clicado!");
        // Load and display the Medicos screen
        //loadView("views/MedicosView.fxml", "Gerenciar Médicos", event);
    }

    @FXML
    private void handleConsultasButton(ActionEvent event) {
        System.out.println("Botão 'Consultas' clicado!");
        // Load and display the Consultas screen
        //loadView("views/ConsultasView.fxml", "Gerenciar Consultas", event);
    }

    @FXML
    private void handleRelatoriosButton(ActionEvent event) {
        System.out.println("Botão 'Relatórios' clicado!");
        // Load and display the Relatorios screen
        //loadView("/views/RelatoriosView.fxml", "Visualizar Relatórios", event);
    }

    // --- Generic method to load a new FXML view and switch scenes ---
    private void loadView(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Clínica - " + title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Display an error alert to the user if FXML loading fails
            System.err.println("Erro ao carregar a tela: " + fxmlPath + " - " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Navegação");
            alert.setHeaderText("Não foi possível carregar a próxima tela.");
            alert.setContentText("Verifique se o arquivo FXML existe e o caminho está correto: " + fxmlPath);
            alert.showAndWait();
        } catch (Exception e) { // Catch any other unexpected exceptions during loading/displaying
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Inesperado");
            alert.setHeaderText("Ocorreu um erro ao tentar abrir a tela.");
            alert.setContentText("Detalhes: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // --- Menu Item Handlers (Optional - you would need to add fx:id and onAction to your MenuItems in FXML for these) ---
    // @FXML
    // private void handleNewFile() {
    //     System.out.println("MenuItem 'New' clicked!");
    // }

    // @FXML
    // private void handleQuit() {
    //     System.out.println("MenuItem 'Quit' clicked!");
    //     Platform.exit(); // To gracefully exit the JavaFX application
    // }
}