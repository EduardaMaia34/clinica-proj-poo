package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.service.LoginService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessageLabel;

    private LoginService loginService;

    @FXML
    public void initialize() {
        this.loginService = new LoginService();
        if (errorMessageLabel != null) {
            errorMessageLabel.setText("");
        }
    }

    @FXML
    public void handleRegisterButton(ActionEvent event) {
        try {
            // Load the FXML for the registration view
            // Replace "registration-view.fxml" with the actual name of your registration FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CadastrarUsuarioView.fxml"));
            Parent root = loader.load();

            // Get the current stage (window) from the button's scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Set the new scene to the current stage
            stage.setScene(new Scene(root));
            stage.setTitle("Cadastro de Usuário"); // Set a title for the new window
            stage.show(); // Display the new window

        } catch (IOException e) {
            System.err.println("Failed to load the registration view: " + e.getMessage());
            e.printStackTrace();
            errorMessageLabel.setText("Erro ao carregar tela de cadastro.");
            errorMessageLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            if (errorMessageLabel != null) {
                errorMessageLabel.setText("Por favor, preencha todos os campos.");
            }
            return;
        }

        if (loginService.authenticateUser(username, password)) {
            System.out.println("Login Successful!");
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DashboardView.fxml"));
                Parent root = loader.load();

                // Pega a stage atual a partir do evento
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

                // Define a nova cena
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Clínica - Dashboard");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                errorMessageLabel.setText("Erro ao carregar a próxima tela.");
            }
        } else {
            if (errorMessageLabel != null) {
                errorMessageLabel.setText("Usuário ou senha inválidos.");
            }
            System.out.println("Falha no login para: " + username);
        }
    }
}