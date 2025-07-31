// Arquivo: ConsultaRepository.java
package com.eduardamaia.clinica.projetopooclinica.repository;

import com.eduardamaia.clinica.projetopooclinica.entities.Consulta;
import com.eduardamaia.clinica.projetopooclinica.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

// padrão de projeto Lucas
public class ConsultaRepository {

    // 1. Instância estática e privada da própria classe (inicialização eager)
    private static final ConsultaRepository instance = new ConsultaRepository();

    // 2. Construtor privado para impedir a instanciação externa com 'new'
    private ConsultaRepository() {
        // Construtor privado garante que a classe não pode ser instanciada de fora.
    }

    // 3. Método público estático para fornecer o ponto de acesso global à instância
    public static ConsultaRepository getInstance() {
        return instance;
    }

    // --- Os métodos originais permanecem inalterados ---

    public Consulta salvar(Consulta consulta) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(consulta);
            tx.commit();
            return consulta;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erro ao salvar consulta: " + e.getMessage(), e);
        }
    }

    public Optional<Consulta> buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Consulta.class, id));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar consulta por ID: " + e.getMessage(), e);
        }
    }

    public List<Consulta> listarTodas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Consulta> query = session.createQuery("FROM Consulta", Consulta.class);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar todas as consultas: " + e.getMessage(), e);
        }
    }

    public void deletarPorId(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Consulta consulta = session.get(Consulta.class, id);
            if (consulta != null) {
                session.delete(consulta);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Erro ao deletar consulta por ID: " + e.getMessage(), e);
        }
    }
}