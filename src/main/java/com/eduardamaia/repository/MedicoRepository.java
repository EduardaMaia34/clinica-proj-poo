package com.eduardamaia.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.eduardamaia.entities.Medico;
import com.eduardamaia.util.HibernateUtil;

public class MedicoRepository {

    public void salvar(Medico medico) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(medico);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Medico buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Medico.class, id);
        }
    }

    public void excluir(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Medico medico = session.get(Medico.class, id);
            if (medico != null) {
                session.remove(medico);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public List<Medico> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Medico", Medico.class).list();
        }
    }
}
