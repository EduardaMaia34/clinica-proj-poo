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

// You might need to import your user model/service if you have one
// import com.eduardamaia.clinica.projetopooclinica.model.User;
// import com.eduardamaia.clinica.projetopooclinica.service.UserService;

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
        // Any initialization logic can go here.
        // For example, setting initial focus or adding listeners.
    }

    /**
     * Handles the action when the "Cadastrar" (Register) button is clicked.
     * It validates the input and attempts to register a new user.
     * @param event The ActionEvent that triggered this method.
     */
    @FXML
    public void handleRegisterUserButton(ActionEvent event) {
        String username = newUsernameField.getText().trim(); // Trim whitespace
        String password = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();


        // 1. Basic input validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            registrationMessageLabel.setText("Por favor, preencha todos os campos.");
            registrationMessageLabel.setTextFill(Color.RED); // Use Color
            return;
        }

        // 2. Check if passwords match
        if (!password.equals(confirmPassword)) {
            registrationMessageLabel.setText("As senhas não conferem.");
            registrationMessageLabel.setTextFill(Color.RED);
            // Clear password fields for security
            newPasswordField.clear();
            confirmPasswordField.clear();
            return;
        }

        // 3. Password strength (optional, but recommended for production)
        if (password.length() < 6) { // Example: minimum 6 characters
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
            // Catch any exceptions thrown by the service (e.g., username already exists)
            registrationMessageLabel.setText(e.getMessage()); // Display the error message from the service
            registrationMessageLabel.setTextFill(Color.RED);
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for debugging
        }
    }


    /**
     * Handles the action when the "Voltar ao Login" (Back to Login) button is clicked.
     * It loads the login view and sets it on the current stage.
     * @param event The ActionEvent that triggered this method.
     */
    @FXML
    public void handleBackToLoginButton(ActionEvent event) {
        try {
            // Load the FXML for the login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml")); // Assuming your login FXML is named login-view.fxml
            Parent root = loader.load();

            // Get the current stage (window) from the button's scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Set the new scene to the current stage
            stage.setScene(new Scene(root));
            stage.setTitle("Clínica - Login"); // Define um título para a janela de login

            // --- ADICIONADA ESTA LINHA PARA GARANTIR QUE A TELA PERMANEÇA MAXIMIZADA ---
            stage.setMaximized(true);

            stage.show(); // Exibe a janela de login (já estava visível, apenas atualiza a cena)

        } catch (IOException e) {
            System.err.println("Failed to load the login view: " + e.getMessage());
            e.printStackTrace();
            // Display an error message to the user if navigation fails
            registrationMessageLabel.setText("Erro ao retornar à tela de login.");
            registrationMessageLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }
}