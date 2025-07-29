package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Usuario; // Import your User model
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    private Usuario loggedInUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("DashboardController initialized!");
        // Any initial setup for the dashboard itself can go here.
        // For example, display welcome message to loggedInUser if needed.
    }

    public void setLoggedInUser(Usuario user) {
        this.loggedInUser = user;
        System.out.println("User " + user.getUsername() + " logged in to Dashboard. Admin status: " + (user.getAdministrador() != null ? user.getAdministrador() : "false/null"));
    }

    @FXML
    private void handlePacientesButton(ActionEvent event) {
        System.out.println("Botão 'Pacientes' clicado! Navegando...");
        // If PacienteController needs the user role, its initialize method should
        // also check SessionManager.
        loadView("/views/PacienteView.fxml", "Gerenciar Pacientes", event);
    }

    @FXML
    private void handleMedicosButton(ActionEvent event) {
        System.out.println("Botão 'Médicos' clicado! Navegando...");
        // Simply load the MedicoView. The MedicoController's initialize method
        // will automatically check SessionManager for admin access.
        loadView("/views/MedicoView.fxml", "Gerenciar Médicos", event);
    }

    @FXML
    private void handleConsultasButton(ActionEvent event) {
        System.out.println("Botão 'Consultas' clicado! Navegando...");
        loadView("/views/ConsultasView.fxml", "Gerenciar Consultas", event); // Assuming ConsultaView, not ConsultasView
    }

    @FXML
    private void handleRelatoriosButton(ActionEvent event) {
        System.out.println("Botão 'Relatórios' clicado! Navegando...");
        loadView("/views/RelatorioView.fxml", "Visualizar Relatórios", event); // Assuming RelatorioView, not RelatoriosView
    }

    private void loadView(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Clínica - " + title);
            stage.setMaximized(true); // Keep maximized if desired
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar a tela: " + fxmlPath + " - " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Navegação");
            alert.setHeaderText("Não foi possível carregar a tela.");
            alert.setContentText("Verifique se o arquivo FXML existe e o caminho está correto: " + fxmlPath + "\nDetalhes: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Inesperado");
            alert.setHeaderText("Ocorreu um erro ao tentar abrir a tela.");
            alert.setContentText("Detalhes: " + e.getMessage());
            alert.showAndWait();
        }
    }
}