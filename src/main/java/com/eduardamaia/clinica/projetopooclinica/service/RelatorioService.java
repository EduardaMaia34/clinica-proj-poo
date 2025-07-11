package com.eduardamaia.clinica.projetopooclinica.service;
import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.entities.Relatorio;
import com.eduardamaia.clinica.projetopooclinica.repository.RelatorioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class RelatorioService {
    private RelatorioRepository RelatorioRepository = new RelatorioRepository();

    public Relatorio criarRelatorio(Relatorio relatorio) {
        if (relatorio == null) {
            throw new IllegalArgumentException("Relatorio não pode ser nulo");
        }
        if (relatorio.getMedico() == null || relatorio.getPaciente() == null) {
            throw new IllegalArgumentException("O relatorio precisa de um medico ou paciente");
        }
        if (relatorio.getPeriodo1() != null && relatorio.getPeriodo1().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de busca não pode ser futura");
        }
        RelatorioRepository.Salvar(relatorio);
        return relatorio;
    }

    public Relatorio buscarPorID(int Id) {
        return RelatorioRepository.buscarPorId(Id);
    }

    public List<Relatorio> listartodos() {
        return RelatorioRepository.listartodos();
    }

    public boolean excluirRelatorio(int Id) {
        Relatorio relatorioexistente = RelatorioRepository.buscarPorId(Id);
        if (relatorioexistente != null) {
            RelatorioRepository.excluir(Id);
            return true;
        }
        return false;
    }
    public Relatorio atualizarRelatorio(int Id, Relatorio relatorioAtualizado) {
        Relatorio relatorioexistente = buscarPorID(Id);
        if (relatorioexistente == null) {
            throw new IllegalArgumentException("Relatório com ID " + Id + " não encontrado para atualização");
        }
        relatorioexistente.setConteudo(relatorioAtualizado.getConteudo());
        relatorioexistente.setMedico(relatorioAtualizado.getMedico());
        relatorioexistente.setPaciente(relatorioAtualizado.getPaciente());
        return relatorioexistente;
    }
    public List<Relatorio> buscarPorMedico(Medico medico) {
        Objects.requireNonNull(medico, "O objeto Médico não pode ser nulo.");
        return RelatorioRepository.buscarPorMedico(medico);
    }
    public List<Relatorio> buscarPorPaciente(Paciente paciente) {
        Objects.requireNonNull(paciente, "O objeto Paciente não pode ser nulo.");
        return RelatorioRepository.buscarPorpaciente(paciente);
    }
    public List<Relatorio> buscarPorPeriodo(LocalDate periodo1, LocalDate periodo2) {
        if (periodo1 == null || periodo2 == null) {
            throw new IllegalArgumentException("As datas de início e fim do período devem ser fornecidas.");
        }
        if (periodo1.isAfter(periodo2)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de fim.");
        }
        return RelatorioRepository.buscarPorPeriodo(periodo1, periodo2);
    }
}
