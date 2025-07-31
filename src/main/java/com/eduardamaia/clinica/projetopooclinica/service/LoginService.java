package com.eduardamaia.clinica.projetopooclinica.service;

import com.eduardamaia.clinica.projetopooclinica.repository.UsuarioRepository;
import com.eduardamaia.clinica.projetopooclinica.entities.Usuario; // Ensure this points to your Usuario entity
import com.eduardamaia.clinica.projetopooclinica.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.eduardamaia.clinica.projetopooclinica.exception.LoginFailedException;
import org.hibernate.HibernateException;

import java.util.Optional;

public class LoginService {


    public LoginService() {

    }


    public Usuario authenticateUser(String username, String password) throws LoginFailedException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UsuarioRepository usuarioRepository = new UsuarioRepository(session);
            Optional<Usuario> userOptional = usuarioRepository.findByUsername(username);

            if (userOptional.isPresent() && password.equals(userOptional.get().getSenha())) {
                return userOptional.get();
            } else {
                throw new LoginFailedException("Usuário ou senha inválidos.");
            }
        } catch (HibernateException e) {
            System.err.println("Erro de banco de dados durante a autenticação: " + e.getMessage());
            throw new LoginFailedException("Erro ao conectar ao banco de dados.", e);
        }
    }

    public Usuario cadastrarUsuario(String username, String senha, Boolean administrador) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            UsuarioRepository usuarioRepository = new UsuarioRepository(session);

            Optional<Usuario> existingUser = usuarioRepository.findByUsername(username);
            if (existingUser.isPresent()) {
                transaction.rollback();
                throw new Exception("Nome de usuário já existe. Por favor, escolha outro.");
            }


            Usuario novoUsuario = new Usuario(username, senha);

            novoUsuario.setAdministrador(administrador != null ? administrador : false);

            usuarioRepository.save(novoUsuario); // Save the new user

            transaction.commit();
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
                session.close(); // Close the session
            }
        }
    }


    public Optional<Usuario> getUserByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UsuarioRepository usuarioRepository = new UsuarioRepository(session);
            return usuarioRepository.findByUsername(username);
        } catch (Exception e) {
            System.err.println("Erro ao buscar usuário por username: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}