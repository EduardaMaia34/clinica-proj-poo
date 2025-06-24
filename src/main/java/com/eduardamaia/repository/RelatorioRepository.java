package com.eduardamaia.repository;

import com.eduardamaia.entities.Medico;
import com.eduardamaia.entities.Paciente;
import com.eduardamaia.entities.Relatorio;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class RelatorioRepository {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("clinica-pu");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void salvar(Relatorio relatorio) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin(); // Inicia a transação
            if (relatorio.getId() == 0) {
                em.persist(relatorio); // Persiste uma nova entidade
            } else {
                em.merge(relatorio); // Atualiza uma entidade existente
            }
            em.getTransaction().commit(); // Confirma a transação
        } catch (Exception e) {
            em.getTransaction().rollback(); // Desfaz a transação em caso de erro
            throw e; // Lança a exceção
        } finally {
            em.close(); // Fecha o EntityManager
        }
    }

    public Relatorio buscarPorId(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Relatorio.class, id);
        } finally {
            em.close();
        }
    }

    public void excluir(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Relatorio relatorio = em.find(Relatorio.class, id);
            if (relatorio != null) {
                em.remove(relatorio);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }


    public List<Relatorio> listarTodos() {
        EntityManager em = getEntityManager();
        try {
            // JPQL (Java Persistence Query Language) é similar ao SQL, mas opera sobre entidades.
            String jpql = "SELECT r FROM Relatorio r";
            TypedQuery<Relatorio> query = em.createQuery(jpql, Relatorio.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Relatorio> buscarPorMedico(Medico medico) {
        if (medico == null || medico.getId() == 0) {
            return List.of(); // Retorna lista vazia se o médico for nulo ou não tiver ID
        }
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT r FROM Relatorio r WHERE r.medico.id = :medicoId";
            TypedQuery<Relatorio> query = em.createQuery(jpql, Relatorio.class);
            query.setParameter("medicoId", medico.getId());
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Relatorio> buscarPorPaciente(Paciente paciente) {
        if (paciente == null || paciente.getId() == 0) {
            return List.of();
        }
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT r FROM Relatorio r WHERE r.paciente.id = :pacienteId";
            TypedQuery<Relatorio> query = em.createQuery(jpql, Relatorio.class);
            query.setParameter("pacienteId", paciente.getId());
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    public List<Relatorio> buscarPorPeriodo(LocalDate periodo1, LocalDate periodo2) {
        if (periodo1 == null || periodo2 == null) {
            throw new IllegalArgumentException("Ambas as datas de período devem ser fornecidas.");
        }
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT r FROM Relatorio r WHERE r.periodo1 >= :dataInicio AND r.periodo2 <= :dataFim";
            TypedQuery<Relatorio> query = em.createQuery(jpql, Relatorio.class);
            query.setParameter("dataInicio", periodo1);
            query.setParameter("dataFim", periodo2);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
