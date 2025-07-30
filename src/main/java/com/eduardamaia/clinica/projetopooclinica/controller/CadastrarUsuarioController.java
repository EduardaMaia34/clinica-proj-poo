package com.eduardamaia.clinica.projetopooclinica.controller;
import com.eduardamaia.clinica.projetopooclinica.entities.Usuario;
import com.eduardamaia.clinica.projetopooclinica.service.LoginService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.paint.Color; // Importe Color para usar Color.RED/GREEN
import java.io.IOException;


public class CadastrarUsuarioController {

    @FXML
    private TextField newUsernameField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button registerUserButton;
    @FXML
    private Button backToLoginButton;
    @FXML
    private Label registrationMessageLabel;

    private LoginService loginService = new LoginService();


    @FXML
    public void initialize() {

    }


    @FXML
    public void handleRegisterUserButton(ActionEvent event) {
        String username = newUsernameField.getText().trim();
        String password = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();



        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            registrationMessageLabel.setText("Por favor, preencha todos os campos.");
            registrationMessageLabel.setTextFill(Color.RED);
            return;
        }


        if (!password.equals(confirmPassword)) {
            registrationMessageLabel.setText("As senhas não conferem.");
            registrationMessageLabel.setTextFill(Color.RED);

            newPasswordField.clear();
            confirmPasswordField.clear();
            return;
        }


        if (password.length() < 6) {
            registrationMessageLabel.setText("A senha deve ter no mínimo 6 caracteres.");
            registrationMessageLabel.setTextFill(Color.RED);
            return;
        }

        try {
            Usuario newUser = loginService.cadastrarUsuario(username, password, false);

            registrationMessageLabel.setText("Usuário '" + newUser.getUsername() + "' cadastrado com sucesso!");
            registrationMessageLabel.setTextFill(Color.GREEN);
            newUsernameField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();

        } catch (Exception e) {

            registrationMessageLabel.setText(e.getMessage());
            registrationMessageLabel.setTextFill(Color.RED);
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void handleBackToLoginButton(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();


            stage.setScene(new Scene(root));
            stage.setTitle("Clínica - Login");


            stage.setMaximized(true);

            stage.show();

        } catch (IOException e) {
            System.err.println("Failed to load the login view: " + e.getMessage());
            e.printStackTrace();

            registrationMessageLabel.setText("Erro ao retornar à tela de login.");
            registrationMessageLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }
}