package com.rayannegsilva.repository;

import com.rayannegsilva.entities.User;

import java.util.List;

// TODO: Adicione mais métodos
public interface UserRepository {
    User findById(Long id);
    List<User> findAll();
    void save(User user);
}
