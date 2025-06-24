package com.eduardamaia.repository;

import com.eduardamaia.entities.Consultas;
import com.eduardamaia.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ConsultasRepository {

    public void salvar(Consultas consulta) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(consulta);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    public Consultas buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Consultas.class, id);
        }
    }


    public List<Consultas> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // A consulta HQL utiliza o nome da Entidade Java ("Consultas"), e não da tabela no BD.
            return session.createQuery("from Consultas", Consultas.class).list();
        }
    }
    public void atualizar(Consultas consulta) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(consulta); // merge é mais seguro para entidades detached
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Consultas consulta = session.get(Consultas.class, id);
            if (consulta != null) {
                session.remove(consulta);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }
}
