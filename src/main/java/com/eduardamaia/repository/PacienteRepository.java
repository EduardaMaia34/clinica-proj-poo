package com.eduardamaia.repository;

import java.util.ArrayList;
import java.util.List;

import com.eduardamaia.entities.Paciente;

public class PacienteRepository {
    private List<Paciente> pacientes = new ArrayList<>();

    public void salvar(Paciente paciente) {
        pacientes.add(paciente);
    }

    public Paciente buscarPorId(int id) {
        return pacientes.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void excluir(int id) {
        pacientes.removeIf(p -> p.getId() == id);
    }

    public List<Paciente> listarTodos() {
        return new ArrayList<>(pacientes);
    }
}
