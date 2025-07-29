package com.eduardamaia.clinica.projetopooclinica.repository;

import java.util.List;
import java.util.Optional; // Import para Optional
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query; // Import para org.hibernate.query.Query (preferível a jakarta.persistence.TypedQuery para HQL puro)

import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.util.HibernateUtil;

// Se você está usando Hibernate diretamente, org.hibernate.query.Query é geralmente mais idiomático
// do que jakarta.persistence.TypedQuery para queries HQL.
// Se sua aplicação for puramente JPA, jakarta.persistence.TypedQuery é o correto.
// Vou usar org.hibernate.query.Query aqui para consistência com as outras classes Hibernate.

public class MedicoRepository {

    public void salvar(Medico medico) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            // Usar saveOrUpdate() para lidar tanto com persistência (novo) quanto com merge (atualização)
            session.saveOrUpdate(medico);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            // Não apenas imprimir o stack trace, mas também lançar uma exceção para o service lidar
            throw new RuntimeException("Erro ao salvar/atualizar médico: " + e.getMessage(), e);
        }
    }

    public Optional<Medico> buscarPorId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Usar Optional.ofNullable para encapsular o resultado, que pode ser null
            return Optional.ofNullable(session.get(Medico.class, id));
        } catch (Exception e) {
            // Lançar exceção para o service lidar
            throw new RuntimeException("Erro ao buscar médico por ID: " + e.getMessage(), e);
        }
    }

    public void excluir(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Medico medico = session.get(Medico.class, id);
            if (medico != null) {
                session.delete(medico);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Erro ao excluir médico: " + e.getMessage(), e);
        }
    }

    public List<Medico> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Usar org.hibernate.query.Query para HQL/JPQL
            Query<Medico> query = session.createQuery("FROM Medico", Medico.class);
            return query.list(); // .list() é o equivalente a .getResultList() para org.hibernate.query.Query
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar todos os médicos: " + e.getMessage(), e);
        }
    }


    public List<Medico> buscarPorNomeOuCodigo(String searchTerm) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Usar LOWER() nas duas partes da comparação para tornar a busca case-insensitive
            Query<Medico> query = session.createQuery(
                "FROM Medico m WHERE LOWER(m.nome) LIKE LOWER(:searchTerm) OR LOWER(m.codigoConselho) LIKE LOWER(:searchTerm)", Medico.class);
            query.setParameter("searchTerm", "%" + searchTerm + "%"); // Adiciona curingas para busca parcial
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar médicos por nome ou código: " + e.getMessage(), e);
        }
    }
}