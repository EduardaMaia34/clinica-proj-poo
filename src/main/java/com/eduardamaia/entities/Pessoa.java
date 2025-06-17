package com.eduardamaia.entities;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Pessoa {
    protected String nome;
    protected String cpf;
    protected String endereco;

    public Pessoa(String nome, String cpf, String endereco, int id) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

}
