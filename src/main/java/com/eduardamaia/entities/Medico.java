package com.eduardamaia.entities;

public class Medico extends Pessoa{
    private double valorConsulta;
    private String codigoConselho;

    public Medico(String nome, String cpf, String endereco, double valorConsulta, String codigoConselho, int id) {
        super(nome, cpf, endereco, id);
        this.valorConsulta = valorConsulta;
        this.codigoConselho = codigoConselho;
    }

    public void cadastrar(String nome, String cpf, String endereco, double valorConsulta, String codigoConselho, int id) {
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