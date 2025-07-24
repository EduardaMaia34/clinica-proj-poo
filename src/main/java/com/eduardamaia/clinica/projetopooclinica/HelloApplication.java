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

        HibernateUtil.getSessionFactory();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));


        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Clínica - Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}