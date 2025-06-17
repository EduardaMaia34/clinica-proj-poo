package com.eduardamaia.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Medico extends Pessoa{
    private double valorConsulta;
    private String codigoConselho;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Medico(String nome, String cpf, String endereco, double valorConsulta, String codigoConselho, int id) {
        super(nome, cpf, endereco, id);
        this.valorConsulta = valorConsulta;
        this.codigoConselho = codigoConselho;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}