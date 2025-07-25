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
import javafx.scene.control.Button; // Importe Button para o cast no handleRegisterButton

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
            // Carrega o FXML para a tela de registro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CadastrarUsuarioView.fxml"));
            Parent root = loader.load();

            // Pega o Stage (janela) atual do botão que disparou o evento
            // O cast para Button é seguro aqui porque event.getSource() é o Button
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Define a nova cena no Stage atual
            stage.setScene(new Scene(root));
            stage.setTitle("Clínica - Cadastro de Usuário"); // Define um título para a nova janela

            // --- ADICIONE ESTA LINHA AQUI PARA MANTER A JANELA MAXIMIZADA ---
            stage.setMaximized(true);

            stage.show(); // Exibe a nova janela (já estava visível, apenas atualiza a cena)

        } catch (IOException e) {
            System.err.println("Falha ao carregar a tela de registro: " + e.getMessage());
            e.printStackTrace();
            if (errorMessageLabel != null) { // Verifica se errorMessageLabel não é nulo antes de usá-lo
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

        if (loginService.authenticateUser(username, password)) {
            System.out.println("Login Successful!");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DashboardView.fxml"));
                Parent root = loader.load();

                // Pega o Stage atual a partir do evento
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

                // Define a nova cena
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Clínica - Dashboard");

                // --- ADICIONE ESTA LINHA AQUI PARA MANTER A JANELA MAXIMIZADA ---
                stage.setMaximized(true);

                stage.show(); // Exibe a nova janela (já estava visível, apenas atualiza a cena)

            } catch (IOException e) {
                e.printStackTrace();
                if (errorMessageLabel != null) { // Verifica se errorMessageLabel não é nulo antes de usá-lo
                    errorMessageLabel.setText("Erro ao carregar a próxima tela.");
                }
            }
        } else {
            if (errorMessageLabel != null) {
                errorMessageLabel.setText("Usuário ou senha inválidos.");
            }
            System.out.println("Falha no login para: " + username);
        }
    }
}