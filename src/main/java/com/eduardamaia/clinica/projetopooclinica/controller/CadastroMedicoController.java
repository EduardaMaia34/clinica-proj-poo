package com.eduardamaia.clinica.projetopooclinica.controller;

import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.service.MedicoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher; // Para validação de CPF e números
import java.util.regex.Pattern; // Para validação de CPF e números

public class CadastroMedicoController {

    @FXML
    private Label titleLabel; // Rótulo do título da janela (Cadastro ou Edição)
    @FXML
    private TextField idField; // Campo oculto para guardar o ID (importante na edição)
    @FXML
    private TextField nomeField;
    @FXML
    private TextField cpfField;
    @FXML
    private TextField enderecoField;
    @FXML
    private TextField valorConsultaField;
    @FXML
    private TextField codigoConselhoField;
    @FXML
    private Label messageLabel;

    private MedicoService medicoService;
    private Medico medicoParaEdicao;

    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");
    private static final Pattern VALOR_CONSULTA_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2}|,\\d{1,2})?$");


    public CadastroMedicoController() {
        this.medicoService = new MedicoService();
    }


    public void setMedicoParaEdicao(Medico medico) {
        this.medicoParaEdicao = medico;
        if (medico != null) {
            titleLabel.setText("Editar Médico");

            idField.setText(String.valueOf(medico.getId()));
            nomeField.setText(medico.getNome());
            cpfField.setText(medico.getCpf());
            enderecoField.setText(medico.getEndereco());

            valorConsultaField.setText(String.format("%.2f", medico.getValorConsulta()).replace('.', ','));
            codigoConselhoField.setText(medico.getCodigoConselho());
        } else {
            titleLabel.setText("Cadastrar Novo Médico");
            // Garante que o campo ID esteja limpo para novos cadastros
            idField.setText("");
        }
    }


    @FXML
    private void handleSaveMedico(ActionEvent event) {

        if (!validateFields()) {
            return;
        }

        try {

            String nome = nomeField.getText().trim();
            String cpf = cpfField.getText().trim();
            String endereco = enderecoField.getText().trim();

            double valorConsulta = Double.parseDouble(valorConsultaField.getText().trim().replace(",", "."));
            String codigoConselho = codigoConselhoField.getText().trim();

            Medico medico;

            if (medicoParaEdicao == null) {
                // É um novo cadastro
                medico = new Medico(nome, cpf, endereco, valorConsulta, codigoConselho);
            } else {
                // É uma edição
                medico = medicoParaEdicao; // Usa o objeto original
                medico.setNome(nome);
                medico.setCpf(cpf);
                medico.setEndereco(endereco);
                medico.setValorConsulta(valorConsulta);
                medico.setCodigoConselho(codigoConselho);
            }

            medicoService.salvarMedico(medico);

            messageLabel.setText("Médico salvo com sucesso!");
            messageLabel.setTextFill(Color.GREEN);

            loadView("/views/MedicoView.fxml", "Gerenciar Médicos", event);


            if (medicoParaEdicao == null) {
                clearFields();
            }

        } catch (NumberFormatException e) {
            messageLabel.setText("Erro: Valor da consulta inválido. Use apenas números (Ex: 150.00 ou 150,00).");
            messageLabel.setTextFill(Color.RED);
            System.err.println("Erro de formato de número ao salvar médico: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // Captura validações lançadas pelo serviço (ex: campos obrigatórios)
            messageLabel.setText("Erro de validação: " + e.getMessage());
            messageLabel.setTextFill(Color.RED);
            System.err.println("Erro de validação ao salvar médico: " + e.getMessage());
        } catch (RuntimeException e) {
            // Captura erros gerais de persistência do serviço/repositório
            messageLabel.setText("Erro ao salvar médico: " + e.getMessage());
            messageLabel.setTextFill(Color.RED);
            System.err.println("Erro inesperado ao salvar médico: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Salvamento", "Não foi possível salvar o médico.", e.getMessage());
        }
    }


    @FXML
    private void handleCancel(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MedicoView.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Clínica - Gerenciar Médicos");
            stage.setMaximized(true); // Opcional: maximizar a janela principal
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela de médicos: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível retornar à tela de médicos.", e.getMessage());
        }
    }

    private boolean validateFields() {
        String nome = nomeField.getText().trim();
        String cpf = cpfField.getText().trim();
        String endereco = enderecoField.getText().trim();
        String valorConsultaStr = valorConsultaField.getText().trim();
        String codigoConselho = codigoConselhoField.getText().trim();

        // Verifica campos vazios
        if (nome.isEmpty() || cpf.isEmpty() || endereco.isEmpty() || valorConsultaStr.isEmpty() || codigoConselho.isEmpty()) {
            messageLabel.setText("Por favor, preencha todos os campos obrigatórios.");
            messageLabel.setTextFill(Color.RED);
            return false;
        }

        // Validação de formato de CPF
        if (!CPF_PATTERN.matcher(cpf).matches()) {
            messageLabel.setText("CPF inválido. Use o formato: 000.000.000-00.");
            messageLabel.setTextFill(Color.RED);
            return false;
        }

        // Validação de formato de Valor da Consulta
        if (!VALOR_CONSULTA_PATTERN.matcher(valorConsultaStr).matches()) {
            messageLabel.setText("Valor da Consulta inválido. Use apenas números (Ex: 150.00 ou 150,00).");
            messageLabel.setTextFill(Color.RED);
            return false;
        }

        // Tenta converter o valor da consulta para double para verificar se é um número válido e positivo
        try {
            double valorConsulta = Double.parseDouble(valorConsultaStr.replace(",", "."));
            if (valorConsulta <= 0) {
                messageLabel.setText("O valor da consulta deve ser maior que zero.");
                messageLabel.setTextFill(Color.RED);
                return false;
            }
        } catch (NumberFormatException e) {
            // Embora o regex capture a maioria, esta é uma salvaguarda final
            messageLabel.setText("Erro de conversão: Valor da consulta não é um número válido.");
            messageLabel.setTextFill(Color.RED);
            return false;
        }

        messageLabel.setText("");
        return true;
    }

    private void clearFields() {
        nomeField.setText("");
        cpfField.setText("");
        enderecoField.setText("");
        valorConsultaField.setText("");
        codigoConselhoField.setText("");
        idField.setText(""); // Garante que o ID oculto também seja limpo
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadView(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
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