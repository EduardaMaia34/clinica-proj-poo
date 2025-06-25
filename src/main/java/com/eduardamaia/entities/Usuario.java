package com.eduardamaia.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String usuario;
    private String senha;
    private boolean admin;

    public Usuario() {
    }

    public Usuario(String usuario, String senha, boolean admin) {
        this.usuario = usuario;
        this.senha = senha;
        this.admin = admin;
    }

    // Getters e Setters completos...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getSenha() { return "********"; }
    public void setSenha(String senha) { this.senha = senha; }
    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }
    public boolean verificarSenha(String senha) { return this.senha.equals(senha); }
}
