package com.eduardamaia.service;

import com.eduardamaia.entities.Consultas;
import com.eduardamaia.repository.ConsultasRepository;

import java.util.List;

public class ConsultasService {

    private final ConsultasRepository consultasRepository;

    public ConsultasService() {
        this.consultasRepository = new ConsultasRepository();
    }

    public void agendarConsulta(Consultas consulta) {
        if (consulta == null) {
            System.err.println("A consulta não pode ser nula.");
            return;
        }
        consultasRepository.salvar(consulta);
    }

    public Consultas buscarConsultaPorId(int id) {
        if (id <= 0) {
            System.err.println("ID inválido.");
            return null;
        }
        return consultasRepository.buscarPorId(id);
    }

    public List<Consultas> listarTodasConsultas() {
        return consultasRepository.listarTodos();
    }

    public void atualizarConsulta(Consultas consulta) {
        if (consulta == null || consulta.getId() <= 0) {
            System.err.println("Consulta inválida para atualização.");
            return;
        }
        consultasRepository.atualizar(consulta);
    }

    public void cancelarConsulta(int id) {
        if (id <= 0) {
            System.err.println("ID inválido para exclusão.");
            return;
        }
        consultasRepository.excluir(id);
    }
}
