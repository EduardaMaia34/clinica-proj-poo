package com.eduardamaia.clinica.projetopooclinica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

import com.eduardamaia.clinica.projetopooclinica.util.HibernateUtil;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // Inicializa o Hibernate (ok manter)
        HibernateUtil.getSessionFactory();

        // Carrega o FXML da tela de Login
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
        Parent root = fxmlLoader.load();

        // Altere esta linha: Remova os tamanhos fixos
        Scene scene = new Scene(root); // Apenas o Parent root, o tamanho será ajustado pela maximização

        stage.setTitle("Clínica - Login");
        stage.setScene(scene);

        stage.setResizable(true);

        // --- ADICIONE ESTA LINHA PARA MAXIMIZAR A JANELA INICIAL ---
        stage.setMaximized(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}