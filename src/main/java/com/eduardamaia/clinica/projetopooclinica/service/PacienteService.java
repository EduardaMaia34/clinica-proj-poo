package com.eduardamaia.clinica.projetopooclinica.service;

import java.util.List;

import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.repository.PacienteRepository;

public class PacienteService {
    private PacienteRepository pacienteRepository = new PacienteRepository();

    public void cadastrarPaciente(Paciente paciente) {
        if (paciente.getProntuario() == null || paciente.getProntuario().isEmpty()) {
            throw new IllegalArgumentException("Prontuário é obrigatório.");
        }
        pacienteRepository.salvar(paciente);
    }

    public Paciente buscarPaciente(int id) {
        return pacienteRepository.buscarPorId(id);
    }

    public void excluirPaciente(int id) {
        pacienteRepository.excluir(id);
    }

    public List<Paciente> listarPacientes() {
        return pacienteRepository.listarTodos();
    }
}

