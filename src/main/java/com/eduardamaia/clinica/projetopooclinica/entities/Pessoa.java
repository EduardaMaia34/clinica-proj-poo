package com.eduardamaia.clinica.projetopooclinica.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // MUDANÇA: Usar Integer para permitir 'null' em novos objetos

    protected String nome;
    protected String cpf;
    protected String endereco;

    public Pessoa() {
        // Construtor padrão (necessário para Hibernate)
    }

    // CONSTRUTOR EXISTENTE - AGORA CORRIGIDO E USANDO Integer para id
    public Pessoa(String nome, String cpf, String endereco, Integer id) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.id = id; // CORREÇÃO: Inicializa o campo 'id'
    }

    // NOVO CONSTRUTOR - Para novos objetos onde o ID será gerado pelo BD
    public Pessoa(String nome, String cpf, String endereco) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        // O id não é inicializado aqui, pois será gerado automaticamente pelo banco de dados
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

    public Integer getId() { // MUDANÇA: Retorna Integer
        return id;
    }

    public void setId(Integer id) { // MUDANÇA: Aceita Integer
        this.id = id;
    }
}