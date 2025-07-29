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

    // Field to store the authenticated user for the entire dashboard session
    private Usuario loggedInUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("DashboardController initialized!");

    }

    public void setLoggedInUser(Usuario user) {
        this.loggedInUser = user;
        System.out.println("User " + user.getUsername() + " logged in to Dashboard. Admin status: " + user.getAdministrador());
    }

    // --- Button Action Handlers ---

    @FXML
    private void handlePacientesButton(ActionEvent event) {
        // Here, if PacienteController also needs user role (e.g., to hide a "delete" button),
        // you would modify loadView to pass the user/admin status.
        loadView("/views/PacienteView.fxml", "Gerenciar Pacientes", event, null); // Pass null for now, or update loadView
    }

    @FXML
    private void handleMedicosButton(ActionEvent event) {
        // Special handling for MedicoView to pass admin access
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MedicoView.fxml"));
            Parent root = loader.load();

            MedicoController medicoController = loader.getController();
            if (medicoController != null && loggedInUser != null) {
                // Pass the admin status to the MedicoController
                medicoController.setAdminAccess(loggedInUser.getAdministrador());
            }

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Clínica - Gerenciar Medicos");
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar a tela de Médicos: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Navegação");
            alert.setHeaderText("Não foi possível carregar a tela de médicos.");
            alert.setContentText("Verifique se o arquivo FXML existe e o caminho está correto: /views/MedicoView.fxml");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Inesperado");
            alert.setHeaderText("Ocorreu um erro ao tentar abrir a tela de médicos.");
            alert.setContentText("Detalhes: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleConsultasButton(ActionEvent event) {
        System.out.println("Botão 'Consultas' clicado!");
        loadView("/views/ConsultasView.fxml", "Gerenciar Consultas", event, null); // Assuming ConsultasView also needs a controller to pass data if required
    }

    @FXML
    private void handleRelatoriosButton(ActionEvent event) {
        System.out.println("Botão 'Relatórios' clicado!");
        loadView("/views/RelatoriosView.fxml", "Visualizar Relatórios", event, null); // Similar assumption
    }

    // This private helper method can be generalized if you need to pass the user
    // to other controllers too. For now, it's modified for general use.
    private void loadView(String fxmlPath, String title, ActionEvent event, Class<?> controllerClassToGet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // If you need to access the controller of the loaded view and pass user data,
            // you'd typically do it here, similar to what's done in handleMedicosButton.
            // This 'loadView' method might need to be specialized or the calling method
            // (like handleMedicosButton) handles the controller access directly.
            // For simplicity, the specific logic for MedicoController is kept in handleMedicosButton.

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Clínica - " + title);
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar a tela: " + fxmlPath + " - " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Navegação");
            alert.setHeaderText("Não foi possível carregar a próxima tela.");
            alert.setContentText("Verifique se o arquivo FXML existe e o caminho está correto: " + fxmlPath);
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