package com.eduardamaia.clinica.projetopooclinica.util;

import com.eduardamaia.clinica.projetopooclinica.entities.Usuario;

public class SessionManager {
    private static Usuario loggedInUser; // Este campo ESTÁTICO mantém o usuário logado

    public static void setLoggedInUser(Usuario user) {
        loggedInUser = user;
        // Para depuração:
        if (loggedInUser != null) {
            System.out.println("SessionManager: Usuário '" + loggedInUser.getUsername() + "' setado na sessão. Admin: " + loggedInUser.getAdministrador());
        } else {
            System.out.println("SessionManager: Sessão do usuário limpa (usuário null).");
        }
    }

    public static Usuario getLoggedInUser() {
        return loggedInUser;
    }

    public static boolean isAdminLoggedIn() {
        // Garante que o usuário não é null E que o atributo administrador não é null, e é true.
        return loggedInUser != null &&
                loggedInUser.getAdministrador() != null &&
                loggedInUser.getAdministrador();
    }

    public static void clearSession() {
        loggedInUser = null;
        System.out.println("SessionManager: Sessão completamente limpa.");
    }
}