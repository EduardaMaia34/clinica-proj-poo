package com.eduardamaia.entities;

public class Paciente extends Pessoa{
    private String prontuario;

    public Paciente(String nome, String cpf, String endereco, String prontuario, int id) {
        super(nome,cpf,endereco,id);
        this.prontuario = prontuario;
    }
    
    public void cadastrar(String nome, String cpf, String endereco, String prontuario, int id) {
        //fazer cadastro
    }
    @Override
    public int buscar(String nomeOuCodigo){
        //buscar no bd
        return 0;
    }
    @Override
    public void editar(int id) {
        //editar informacoes
    }
    
    @Override
    public void excluir(int id) {
        //excluir no bd
    }

    
}