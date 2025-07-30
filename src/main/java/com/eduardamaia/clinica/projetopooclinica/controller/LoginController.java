package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Usuario;
import com.eduardamaia.clinica.projetopooclinica.service.LoginService;
import com.eduardamaia.clinica.projetopooclinica.util.SessionManager;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CadastrarUsuarioView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Clínica - Cadastro de Usuário");
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            System.err.println("Falha ao carregar a tela de registro: " + e.getMessage());
            e.printStackTrace();
            if (errorMessageLabel != null) {
                errorMessageLabel.setText("Erro ao carregar tela de cadastro.");
                errorMessageLabel.setTextFill(javafx.scene.paint.Color.RED);
            }
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

        Usuario authenticatedUser = loginService.authenticateUser(username, password);

        if (authenticatedUser != null) {
            System.out.println("Login bem-sucedido!");

            SessionManager.setLoggedInUser(authenticatedUser);


            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DashboardView.fxml"));
                Parent root = loader.load();


                DashboardController dashboardController = loader.getController();
                if (dashboardController != null) {
                    dashboardController.setLoggedInUser(authenticatedUser);
                }

                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Clínica - Dashboard");
                stage.setMaximized(true);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                if (errorMessageLabel != null) {
                    errorMessageLabel.setText("Erro ao carregar a tela do Dashboard.");
                    errorMessageLabel.setTextFill(javafx.scene.paint.Color.RED);
                }
            }
        } else {
            if (errorMessageLabel != null) {
                errorMessageLabel.setText("Usuário ou senha inválidos.");
                errorMessageLabel.setTextFill(javafx.scene.paint.Color.RED);
            }
            System.out.println("Falha no login para: " + username);
        }
    }

}