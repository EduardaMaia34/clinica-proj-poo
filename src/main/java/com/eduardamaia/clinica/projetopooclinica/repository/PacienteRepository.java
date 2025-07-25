// Exemplo de PacienteRepository.java (exemplo com Hibernate)
package com.eduardamaia.clinica.projetopooclinica.repository;

import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.util.HibernateUtil; // Supondo seu utilitário Hibernate
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class PacienteRepository {

    public void salvar(Paciente paciente) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(paciente);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao salvar paciente: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    public Paciente buscarPorId(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Paciente paciente = null;
        try {
            paciente = session.get(Paciente.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar paciente por ID: " + e.getMessage(), e);
        } finally {
            session.close();
        }
        return paciente;
    }

    public List<Paciente> listarTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Paciente> pacientes = null;
        try {
            // Cuidado com SQL Injection se não usar HQL/JPQL ou critérios seguros
            pacientes = session.createQuery("FROM Paciente", Paciente.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar pacientes: " + e.getMessage(), e);
        } finally {
            session.close();
        }
        return pacientes;
    }

    public void excluir(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Paciente paciente = session.get(Paciente.class, id);
            if (paciente != null) {
                session.delete(paciente);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao excluir paciente: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    public void atualizar(Paciente paciente) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(paciente); // ou session.merge(paciente);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao atualizar paciente: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
}