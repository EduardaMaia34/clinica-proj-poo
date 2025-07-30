package com.eduardamaia.clinica.projetopooclinica.service;

import com.eduardamaia.clinica.projetopooclinica.entities.Relatorio;
import com.eduardamaia.clinica.projetopooclinica.repository.RelatorioRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * CORREÇÃO: Este serviço agora usa o RelatorioRepository baseado em Hibernate.
 */
public class RelatorioService {

    // Injeção de dependência (melhor prática)
    private final RelatorioRepository relatorioRepository;

    public RelatorioService() {
        // A instância do repositório é criada aqui
        this.relatorioRepository = new RelatorioRepository();
    }

    public Relatorio criarRelatorio(Relatorio relatorio) {
        if (relatorio == null) {
            throw new IllegalArgumentException("Relatório não pode ser nulo.");
        }
        if (relatorio.getMedico() == null || relatorio.getPaciente() == null) {
            throw new IllegalArgumentException("O relatório precisa de um médico e um paciente.");
        }
        // Outras regras de negócio podem ser adicionadas aqui

        return relatorioRepository.salvar(relatorio);
    }

    public Relatorio buscarPorId(int id) {
        return relatorioRepository.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Relatório com ID " + id + " não encontrado."));
    }

    public List<Relatorio> listartodos() {
        return relatorioRepository.listarTodos();
    }

    public void excluirRelatorio(int id) {
        // Verifica se existe antes de tentar deletar
        buscarPorId(id);
        relatorioRepository.excluir(id);
    }

    public Relatorio atualizarRelatorio(int id, Relatorio relatorioAtualizado) {
        Relatorio relatorioExistente = buscarPorId(id);

        // Atualiza os campos
        relatorioExistente.setConteudo(relatorioAtualizado.getConteudo());
        relatorioExistente.setMedico(relatorioAtualizado.getMedico());
        relatorioExistente.setPaciente(relatorioAtualizado.getPaciente());
        relatorioExistente.setPeriodo1(relatorioAtualizado.getPeriodo1());
        relatorioExistente.setPeriodo2(relatorioAtualizado.getPeriodo2());

        return relatorioRepository.salvar(relatorioExistente);
    }
}