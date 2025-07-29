package com.eduardamaia.clinica.projetopooclinica.repository;

import com.eduardamaia.clinica.projetopooclinica.entities.Consultas;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Classe de repositório (DAO) para a entidade Consultas com implementação manual.
 * Esta classe gerencia as operações de persistência diretamente através do EntityManager,
 * sem herdar de JpaRepository.
 */
public class ConsultasRepository {

    // Injeta o EntityManager, que é a interface principal para a persistência no JPA.
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Salva ou atualiza uma consulta no banco de dados.
     * A anotação @Transactional é essencial para operações de escrita (salvar, atualizar, deletar).
     *
     * @param consulta A entidade a ser salva.
     * @return A entidade salva (agora gerenciada pelo EntityManager).
     */
    public Consultas salvar(Consultas consulta) {
        // O método merge lida tanto com a criação (persist) quanto com a atualização.
        return entityManager.merge(consulta);
    }

    /**
     * Busca uma consulta pelo seu ID.
     *
     * @param id O ID da consulta a ser encontrada.
     * @return A entidade Consultas encontrada, ou null se não existir.
     */
    public Consultas buscarPorId(int id) {
        return entityManager.find(Consultas.class, id);
    }

    /**
     * Lista todas as consultas existentes no banco de dados.
     *
     * @return Uma lista de todas as consultas.
     */
    public List<Consultas> listarTodas() {
        // Cria uma consulta usando a linguagem de consulta do JPA (JPQL).
        String jpql = "SELECT c FROM Consultas c";
        TypedQuery<Consultas> query = entityManager.createQuery(jpql, Consultas.class);
        return query.getResultList();
    }

    /**
     * Deleta uma consulta do banco de dados com base no seu ID.
     *
     * @param id O ID da consulta a ser deletada.
     */
    public void deletarPorId(int id) {
        // Primeiro, busca a entidade para que ela seja gerenciada pelo EntityManager.
        Consultas consultaParaDeletar = buscarPorId(id);
        if (consultaParaDeletar != null) {
            // O método remove só funciona em entidades gerenciadas.
            entityManager.remove(consultaParaDeletar);
        }
        // Se a consulta não existir, o método simplesmente não faz nada.
    }
}