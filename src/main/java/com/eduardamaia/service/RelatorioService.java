package com.eduardamaia.service;

import com.eduardamaia.entities.Medico;
import com.eduardamaia.entities.Paciente;
import com.eduardamaia.entities.Relatorio;
import com.eduardamaia.repository.RelatorioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class RelatorioService {
    private final RelatorioRepository relatorioRepository = new RelatorioRepository();

    public Relatorio criarRelatorio(Relatorio relatorio) {
        if (relatorio == null) {
            throw new IllegalArgumentException("Relatorio não pode ser nulo");
        }
        if (relatorio.getMedico() == null || relatorio.getPaciente() == null) {
            throw new IllegalArgumentException("O relatorio precisa de um medico e um paciente");
        }
        if (relatorio.getPeriodo1() != null && relatorio.getPeriodo1().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de busca não pode ser futura");
        }
        
        relatorioRepository.salvar(relatorio);
        return relatorio;
    }

    public Relatorio buscarPorID(int id) {
        return relatorioRepository.buscarPorId(id);
    }

    public List<Relatorio> listartodos() {
        return relatorioRepository.listarTodos();
    }

    public boolean excluirRelatorio(int id) {
        Relatorio relatorioexistente = relatorioRepository.buscarPorId(id);
        if (relatorioexistente != null) {
            relatorioRepository.excluir(id);
            return true;
        }
        return false;
    }
    
    public Relatorio atualizarRelatorio(int id, Relatorio relatorioAtualizado) {
        Relatorio relatorioExistente = buscarPorID(id);

        if (relatorioExistente == null) {
            throw new IllegalArgumentException("Relatório com ID " + id + " não encontrado para atualização");
        }
        
        relatorioExistente.setMedico(relatorioAtualizado.getMedico());
        relatorioExistente.setPaciente(relatorioAtualizado.getPaciente());
        relatorioExistente.setPeriodo1(relatorioAtualizado.getPeriodo1());
        relatorioExistente.setPeriodo2(relatorioAtualizado.getPeriodo2());
        
        relatorioRepository.salvar(relatorioExistente);

        return relatorioExistente;
    }

    public List<Relatorio> buscarPorMedico(Medico medico) {
        Objects.requireNonNull(medico, "O objeto Médico não pode ser nulo.");
        return relatorioRepository.buscarPorMedico(medico);
    }

    public List<Relatorio> buscarPorPaciente(Paciente paciente) {
        Objects.requireNonNull(paciente, "O objeto Paciente não pode ser nulo.");
        return relatorioRepository.buscarPorPaciente(paciente);
    }

    public List<Relatorio> buscarPorPeriodo(LocalDate periodo1, LocalDate periodo2) {
        if (periodo1 == null || periodo2 == null) {
            throw new IllegalArgumentException("As datas de início e fim do período devem ser fornecidas.");
        }
        if (periodo1.isAfter(periodo2)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de fim.");
        }
        return relatorioRepository.buscarPorPeriodo(periodo1, periodo2);
    }
}

