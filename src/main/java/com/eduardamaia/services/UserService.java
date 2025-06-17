package com.rayannegsilva.services;

import com.rayannegsilva.entities.User;

import java.util.List;

// TODO: Adicione mais métodos
public interface UserService {
    User getById(Long id);
    List<User> getAll();
    void register(User user);
}
