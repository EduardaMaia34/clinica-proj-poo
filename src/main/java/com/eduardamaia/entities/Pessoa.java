package com.eduardamaia.entities;

public abstract class Pessoa {
    protected String nome;
    protected String cpf;
    protected String endereco;
    protected int id;

    public Pessoa(String nome, String cpf, String endereco, int id) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.id = id;
    }

    public abstract void editar(int id);
    public abstract void excluir(int id);
    public abstract int buscar(String nomeOuCodigo);

}
