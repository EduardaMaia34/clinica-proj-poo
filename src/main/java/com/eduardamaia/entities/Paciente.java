package com.eduardamaia.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Paciente")
public class Paciente extends Pessoa{
    private String prontuario;

    public Paciente() {
        super();
    }

    public Paciente(String nome, String cpf, String endereco, String prontuario, int id) {
        super(nome,cpf,endereco,id);
        this.prontuario = prontuario;
    }

    public String getProntuario() {
        return prontuario;
    }

    public void setProntuario(String prontuario) {
        this.prontuario = prontuario;
    }

}