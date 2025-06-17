package com.eduardamaia.repository;

import java.util.ArrayList;
import java.util.List;

import com.eduardamaia.entities.Medico;

public class MedicoRepository {
    private List<Medico> medicos = new ArrayList<>();

    public void salvar(Medico medico) {
        medicos.add(medico);
    }

    public Medico buscarPorId(int id) {
        return medicos.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void excluir(int id) {
        medicos.removeIf(m -> m.getId() == id);
    }

    public List<Medico> listarTodos() {
        return new ArrayList<>(medicos);
    }
}

