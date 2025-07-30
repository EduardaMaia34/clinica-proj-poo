package com.eduardamaia.clinica.projetopooclinica.repository;

import com.eduardamaia.clinica.projetopooclinica.entities.Relatorio;
import com.eduardamaia.clinica.projetopooclinica.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

/**
 * CORREÇÃO: Este repositório agora usa o Hibernate para persistir os dados no banco.
 */
public class RelatorioRepository {

    public Relatorio salvar(Relatorio relatorio) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(relatorio);
            tx.commit();
            return relatorio;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erro ao salvar relatório: " + e.getMessage(), e);
        }
    }

    public Optional<Relatorio> buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Usa FetchType.EAGER na entidade Relatorio para evitar LazyInitializationException
            Relatorio relatorio = session.get(Relatorio.class, id);
            return Optional.ofNullable(relatorio);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar relatório por ID: " + e.getMessage(), e);
        }
    }

    public List<Relatorio> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // A query HQL usa o nome da CLASSE da entidade
            Query<Relatorio> query = session.createQuery("FROM Relatorio", Relatorio.class);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar todos os relatórios: " + e.getMessage(), e);
        }
    }

    public void excluir(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Relatorio relatorio = session.get(Relatorio.class, id);
            if (relatorio != null) {
                session.delete(relatorio);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erro ao excluir relatório: " + e.getMessage(), e);
        }
    }
}