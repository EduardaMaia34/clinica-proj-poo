package com.eduardamaia.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.eduardamaia.entities.Paciente;
import com.eduardamaia.util.HibernateUtil;

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
                session.remove(paciente);
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
            return session.createQuery("from Paciente", Paciente.class).list();
        }
    }
}
