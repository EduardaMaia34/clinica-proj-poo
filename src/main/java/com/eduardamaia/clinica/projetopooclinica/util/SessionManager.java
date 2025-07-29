package com.eduardamaia.clinica.projetopooclinica.util;

import com.eduardamaia.clinica.projetopooclinica.entities.Usuario; // Certifique-se de importar Usuario

/**
 * Classe para gerenciar a sessão do usuário logado na aplicação.
 * Guarda o objeto Usuario atualmente autenticado.
 */
public class SessionManager {
    private static boolean isAdminLoggedIn = false;

    public static void setAdminLoggedIn(boolean isAdmin) {
        isAdminLoggedIn = isAdmin;
    }

    public static boolean isAdminLoggedIn() {
        return isAdminLoggedIn;
    }

    private static Usuario loggedInUser; // O usuário atualmente logado

    public static void login(Usuario user) {
        if (user == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo para o login.");
        }
        loggedInUser = user;
        System.out.println("Usuário logado: " + user.getUsername());
    }

    public static void logout() {
        loggedInUser = null;
        System.out.println("Usuário deslogado.");
    }

    public static Usuario getLoggedInUser() {
        return loggedInUser;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }
}