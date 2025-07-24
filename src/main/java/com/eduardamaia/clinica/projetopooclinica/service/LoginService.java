package com.eduardamaia.clinica.projetopooclinica.service;

import com.eduardamaia.clinica.projetopooclinica.repository.UsuarioRepository;
import com.eduardamaia.clinica.projetopooclinica.entities.Usuario;
import com.eduardamaia.clinica.projetopooclinica.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class LoginService {

    // The repository is now created within the methods, receiving the Session
    // private final UsuarioRepository usuarioRepository; // Not needed as a field if created per method

    // Constructor (optional, but good for future dependency injection)
    public LoginService() {
        // No direct repository instantiation here if it depends on a Session that's created per method call.
    }


    public boolean authenticateUser(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UsuarioRepository usuarioRepository = new UsuarioRepository(session); // <--- Pass the session
            Optional<Usuario> userOptional = usuarioRepository.findByUsername(username);

            return userOptional.map(user -> password.equals(user.getSenha())).orElse(false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario cadastrarUsuario(String username, String senha, Boolean administrador) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            UsuarioRepository usuarioRepository = new UsuarioRepository(session); // <--- Pass the session

            Optional<Usuario> existingUser = usuarioRepository.findByUsername(username);
            if (existingUser.isPresent()) {
                transaction.rollback();
                throw new Exception("Nome de usuário já existe. Por favor, escolha outro.");
            }

            // IMPORTANT: HASH THE PASSWORD HERE BEFORE STORING IT!
            Usuario novoUsuario = new Usuario(username, senha);
            novoUsuario.setAdministrador(administrador != null ? administrador : false);

            usuarioRepository.save(novoUsuario); // Now save uses the session passed to the repository

            transaction.commit(); // Commit the service-level transaction
            return novoUsuario;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erro no serviço de cadastro de usuário: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Erro ao cadastrar usuário: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close(); // Close the service-level session
            }
        }
    }

    public Optional<Usuario> getUserByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UsuarioRepository usuarioRepository = new UsuarioRepository(session); // <--- Pass the session
            return usuarioRepository.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}