// src/main/java/com/eduardamaia/clinica/projetopooclinica/controller/CadastroPacienteController.java
package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.service.PacienteService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert; // Importação adicionada
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox; // Importação adicionada
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CadastroPacienteController implements Initializable {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField cpfField;
    @FXML
    private TextField enderecoField;
    @FXML
    private TextField prontuarioField;
    @FXML
    private Label messageLabel; // Para exibir mensagens de sucesso/erro

    private PacienteService pacienteService;
    private Paciente pacienteParaEdicao; // Campo para armazenar o paciente se estiver em modo de edição

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.pacienteService = new PacienteService();
        // Não carregamos dados aqui, apenas preparamos o serviço.
        // O método setPacienteParaEdicao() será chamado separadamente se for uma edição.
    }

    /**
     * Define o paciente a ser editado. Se for null, é um novo cadastro.
     * Este método será chamado pelo PacienteController.
     */
    public void setPacienteParaEdicao(Paciente paciente) {
        this.pacienteParaEdicao = paciente;
        if (pacienteParaEdicao != null) {
            // Preenche os campos do formulário com os dados do paciente para edição
            nomeField.setText(pacienteParaEdicao.getNome());
            cpfField.setText(pacienteParaEdicao.getCpf());
            enderecoField.setText(pacienteParaEdicao.getEndereco());
            prontuarioField.setText(pacienteParaEdicao.getProntuario());

            // Opcional: Altera o título da tela para "Editar Paciente"
            // Supondo que o primeiro Label na VBox pai do nomeField é o título
            if (nomeField.getParent() instanceof VBox) {
                VBox parentVBox = (VBox) nomeField.getParent();
                if (!parentVBox.getChildren().isEmpty() && parentVBox.getChildren().get(0) instanceof Label) {
                    ((Label) parentVBox.getChildren().get(0)).setText("Editar Paciente");
                }
            }
            messageLabel.setText("Modo de edição. Altere os campos e clique em Salvar.");
            messageLabel.setTextFill(javafx.scene.paint.Color.BLUE);
        }
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        String nome = nomeField.getText().trim();
        String cpf = cpfField.getText().trim();
        String endereco = enderecoField.getText().trim();
        String prontuario = prontuarioField.getText().trim();

        // Validações básicas (você pode adicionar mais)
        if (nome.isEmpty() || cpf.isEmpty() || endereco.isEmpty() || prontuario.isEmpty()) {
            messageLabel.setText("Por favor, preencha todos os campos.");
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }
        // Exemplo de validação de CPF (formato simples)
        if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            messageLabel.setText("Formato de CPF inválido. Use XXX.XXX.XXX-XX");
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }

        try {
            if (pacienteParaEdicao == null) {
                // MODO DE CADASTRO: Cria um novo paciente
                // O ID será gerado automaticamente pelo banco de dados (geralmente 0 ou null ao criar)
                Paciente novoPaciente = new Paciente(nome, cpf, endereco, prontuario, 0); // Ajuste este construtor conforme sua classe Paciente
                pacienteService.cadastrarPaciente(novoPaciente);
                messageLabel.setText("Paciente '" + novoPaciente.getNome() + "' cadastrado com sucesso!");
                messageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                clearFields(); // Limpa os campos após o cadastro
            } else {
                // MODO DE EDIÇÃO: Atualiza o paciente existente
                pacienteParaEdicao.setNome(nome);
                pacienteParaEdicao.setCpf(cpf);
                pacienteParaEdicao.setEndereco(endereco);
                pacienteParaEdicao.setProntuario(prontuario);

                pacienteService.atualizarPaciente(pacienteParaEdicao); // Este método precisa existir no PacienteService
                messageLabel.setText("Paciente '" + pacienteParaEdicao.getNome() + "' atualizado com sucesso!");
                messageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                // Após a edição, você pode querer voltar para a tela de lista de pacientes.
                // Para isso, chame handleCancelButton(event);
            }
        } catch (IllegalArgumentException e) {
            messageLabel.setText(e.getMessage());
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
        } catch (Exception e) {
            messageLabel.setText("Erro ao salvar paciente: " + e.getMessage());
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            System.err.println("Erro ao salvar paciente: " + e.getMessage());
            e.printStackTrace(); // Apenas para depuração, substituir em produção
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        // Volta para a tela de listagem de pacientes (PacienteView)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PacienteView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Clínica - Gerenciar Pacientes");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao voltar para a tela de pacientes: " + e.getMessage());
            e.printStackTrace(); // Apenas para depuração, substituir em produção
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível retornar à tela de pacientes.", e.getMessage());
        }
    }

    private void clearFields() {
        nomeField.clear();
        cpfField.clear();
        enderecoField.clear();
        prontuarioField.clear();
        pacienteParaEdicao = null; // Reseta para modo de cadastro
        // Opcional: Redefinir o título da tela para "Cadastro de Paciente"
        if (nomeField.getParent() instanceof VBox) {
            VBox parentVBox = (VBox) nomeField.getParent();
            if (!parentVBox.getChildren().isEmpty() && parentVBox.getChildren().get(0) instanceof Label) {
                ((Label) parentVBox.getChildren().get(0)).setText("Cadastro de Paciente");
            }
        }
    }

    /**
     * Método auxiliar para exibir alertas.
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}