package com.eduardamaia.clinica.projetopooclinica.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="Medico")
public class Medico extends Pessoa{
    private double valorConsulta;
    private String codigoConselho;

    public Medico() {
        super();
    }

    // CONSTRUTOR EXISTENTE (com ID)
    public Medico(String nome, String cpf, String endereco, double valorConsulta, String codigoConselho, int id) {
        super(nome, cpf, endereco, id);
        this.valorConsulta = valorConsulta;
        this.codigoConselho = codigoConselho;
    }

    // NOVO CONSTRUTOR: Sem o ID, para ser usado no cadastro de novos médicos
    public Medico(String nome, String cpf, String endereco, double valorConsulta, String codigoConselho) {
        super(nome, cpf, endereco); // Chama o construtor de Pessoa sem ID
        this.valorConsulta = valorConsulta;
        this.codigoConselho = codigoConselho;
    }

    @Override
    public String toString() {
        return "Médico: " + getNome() + " (CRM: " + codigoConselho + ")";
    }


    public double getValorConsulta() {
        return valorConsulta;
    }

    public void setValorConsulta(double valorConsulta) {
        this.valorConsulta = valorConsulta;
    }

    public String getCodigoConselho() {
        return codigoConselho;
    }

    public void setCodigoConselho(String codigoConselho) {
        this.codigoConselho = codigoConselho;
    }
}