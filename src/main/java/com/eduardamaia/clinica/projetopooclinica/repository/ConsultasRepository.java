package com.eduardamaia.clinica.projetopooclinica.repository;

import com.eduardamaia.clinica.projetopooclinica.entities.Consultas;
import jakarta.persistence.*;

import java.util.List;


public class ConsultasRepository {

    // 🔒 Instância Singleton
    private static ConsultasRepository instancia;

    // 🔧 EntityManagerFactory e EntityManager
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("clinicaPU");
    private final EntityManager entityManager;

    public ConsultasRepository() {
        this.entityManager = emf.createEntityManager();
    }

    // 🌐 Acesso à instância única
    public static synchronized ConsultasRepository getInstance() {
        if (instancia == null) {
            instancia = new ConsultasRepository();
        }
        return instancia;
    }
    
    public Consultas salvar(Consultas consulta) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Consultas merge = entityManager.merge(consulta);
            tx.commit();
            return merge;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // 🔍 Buscar por ID
    public Consultas buscarPorId(int id) {
        return entityManager.find(Consultas.class, id);
    }

    public List<Consultas> listarTodas() {
        String jpql = "SELECT c FROM Consultas c";
        TypedQuery<Consultas> query = entityManager.createQuery(jpql, Consultas.class);
        return query.getResultList();
    }

    public void deletarPorId(int id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Consultas consulta = buscarPorId(id);
            if (consulta != null) {
                entityManager.remove(consulta);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }
}
