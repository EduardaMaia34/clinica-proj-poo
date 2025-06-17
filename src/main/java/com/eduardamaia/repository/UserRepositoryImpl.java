package com.rayannegsilva.repository;

import com.rayannegsilva.entities.User;
import com.rayannegsilva.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;


// TODO: Implemente os métodos adicionados na interface.
public class UserRepositoryImpl implements  UserRepository {
    private final EntityManager em = JPAUtil.getEntityManagerFactory();

    @Override
    public User findById(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("FROM User", User.class).getResultList();
    }

    @Override
    public void save(User user) {
        EntityTransaction ts = em.getTransaction();
        try {
            ts.begin();
            em.persist(user);
            ts.commit();
        } catch (RuntimeException e) {
            if (ts.isActive()) {
                ts.rollback();
            }
            throw new RuntimeException("Erro ao salvar usuário", e);
        }
    }


}
