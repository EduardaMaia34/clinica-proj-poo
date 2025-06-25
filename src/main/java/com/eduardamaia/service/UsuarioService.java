package com.eduardamaia.services;

import com.eduardamaia.entities.Medico;
import com.eduardamaia.entities.Usuario;
import com.eduardamaia.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    public Usuario criarUsuario(Usuario usuario) {
        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
            throw new IllegalStateException("Erro: Nome de usuário '" + usuario.getUsuario() + "' já está em uso.");
        }
        return usuarioRepository.save(usuario);
    }
    public Optional<Usuario> buscarPorId(int id) {
        return usuarioRepository.findById(id);
    }
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}
