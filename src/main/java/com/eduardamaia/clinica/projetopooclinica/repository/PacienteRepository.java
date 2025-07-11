package com.eduardamaia.clinica.projetopooclinica.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;

import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.util.HibernateUtil;

public class PacienteRepository {

    public void salvar(Paciente paciente) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(paciente);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw e;
        }
    }

    public Paciente buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Paciente.class, id);
        }
    }

    public void excluir(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Paciente paciente = session.get(Paciente.class, id);
            if (paciente != null) {
                session.delete(paciente);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw e;
        }
    }

    public List<Paciente> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            javax.persistence.TypedQuery<Paciente> query = session.createQuery("from Paciente", Paciente.class);
            return query.getResultList();
        }
    }

}
