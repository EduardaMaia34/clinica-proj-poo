package com.eduardamaia.clinica.projetopooclinica.repository;

import java.util.List;
import java.util.Optional; // Para retornar Optional em buscarPorId, um bom padrão
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query; // Import para org.hibernate.query.Query

import com.eduardamaia.clinica.projetopooclinica.entities.Consulta; // Confirme o nome correto da sua entidade
import com.eduardamaia.clinica.projetopooclinica.util.HibernateUtil; // Reutiliza sua classe de utilidade Hibernate

public class ConsultaRepository {

    // 🔒 Instância Singleton
    private static ConsultaRepository instancia;


    public ConsultaRepository() {

    }

    // 🌐 Acesso à instância única
    public static synchronized ConsultaRepository getInstance() {
        if (instancia == null) {
            instancia = new ConsultaRepository();
        }
        return instancia;
    }

    public Consulta salvar(Consulta consulta) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(consulta); // Usar saveOrUpdate para persistir ou atualizar
            tx.commit();
            return consulta; // Retorna a mesma instância, que agora está em estado persistente
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Erro ao salvar/atualizar consulta: " + e.getMessage(), e);
        }
    }

    /**
     * Busca uma consulta por ID.
     * @param id O ID da consulta.
     * @return Um Optional contendo a consulta se encontrada, ou um Optional vazio.
     * @throws RuntimeException se ocorrer um erro durante a operação.
     */
    public Optional<Consulta> buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Usa Optional.ofNullable para lidar com o caso de não encontrar a consulta
            return Optional.ofNullable(session.get(Consulta.class, id));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar consulta por ID: " + e.getMessage(), e);
        }
    }

    /**
     * Lista todas as consultas existentes no banco de dados.
     * @return Uma lista de todas as consultas.
     * @throws RuntimeException se ocorrer um erro durante a operação.
     */
    public List<Consulta> listarTodas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Usar HQL (Hibernate Query Language)
            Query<Consulta> query = session.createQuery("FROM Consultas", Consulta.class);
            return query.list(); // Retorna a lista de resultados
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar todas as consultas: " + e.getMessage(), e);
        }
    }

    /**
     * Deleta uma consulta por ID.
     * @param id O ID da consulta a ser deletada.
     * @throws RuntimeException se ocorrer um erro durante a operação.
     */
    public void deletarPorId(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Consulta consulta = session.get(Consulta.class, id); // Busca a consulta primeiro
            if (consulta != null) {
                session.delete(consulta); // Se encontrada, deleta
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Erro ao deletar consulta por ID: " + e.getMessage(), e);
        }
    }


}