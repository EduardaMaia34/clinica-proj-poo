package com.eduardamaia.clinica.projetopooclinica.repository;

import com.eduardamaia.clinica.projetopooclinica.entities.Usuario;
// Remove HibernateUtil import from Repository, as Session is passed in
// import com.eduardamaia.clinica.projetopooclinica.util.HibernateUtil; // <--- REMOVE THIS
import org.hibernate.Session;
import org.hibernate.Transaction; // Transaction is not used directly in repository anymore, but keep for now if other methods use it
import org.hibernate.query.Query;

import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {

    private final Session session; // <--- The session is now injected

    public UsuarioRepository(Session session) {
        this.session = session;
    }


    public void save(Usuario usuario) {
        // Transaction is managed by the service layer
        session.persist(usuario);
    }

    public Optional<Usuario> findById(Long id) {
        return Optional.ofNullable(session.get(Usuario.class, id));
    }

    public List<Usuario> findAll() {
        return session.createQuery("from Usuario", Usuario.class).list();
    }

    public void update(Usuario usuario) {
        session.merge(usuario);
    }

    public void delete(Long id) {
        Usuario usuario = session.get(Usuario.class, id);
        if (usuario != null) {
            session.remove(usuario);
        }
    }

    public Optional<Usuario> findByUsername(String username) {
        Query<Usuario> query = session.createQuery(
                "SELECT u FROM Usuario u WHERE u.username = :username", Usuario.class);
        query.setParameter("username", username);
        return query.uniqueResultOptional();
    }

    public List<Usuario> findAdministrators() {
        Query<Usuario> query = session.createQuery(
                "SELECT u FROM Usuario u WHERE u.administrador = true", Usuario.class);
        return query.list();
    }
}