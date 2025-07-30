package com.eduardamaia.clinica.projetopooclinica.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList; // <--- ADICIONE ESTA IMPORTAÇÃO

import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.repository.MedicoRepository;
import com.eduardamaia.clinica.projetopooclinica.padroes.MedicoDataChangeListener;

public class MedicoService {

    private MedicoRepository medicoRepository;
    // <--- ADICIONE ESTA LINHA: Declaração e inicialização da lista de observadores
    private List<MedicoDataChangeListener> listeners = new ArrayList<>();


    public MedicoService() {
        this.medicoRepository = new MedicoRepository();
    }

    // --- Métodos para gerenciar observadores ---
    public void addMedicoDataChangeListener(MedicoDataChangeListener listener) {
        if (!listeners.contains(listener)) { // Adicionado verificação para evitar duplicatas
            listeners.add(listener);
        }
    }

    public void removeMedicoDataChangeListener(MedicoDataChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyMedicoDataChangeListeners(String medicoNome, boolean isDeleted) {
        System.out.println("Notificando observadores sobre mudança nos dados de médicos.");
        for (MedicoDataChangeListener listener : listeners) {
            listener.onMedicoDataChanged(medicoNome, isDeleted);
        }
    }
    // --- Fim dos métodos para gerenciar observadores ---

    public void salvarMedico(Medico medico) {
        if (medico.getNome() == null || medico.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do médico é obrigatório.");
        }
        if (medico.getCpf() == null || medico.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do médico é obrigatório.");
        }
        if (medico.getEndereco() == null || medico.getEndereco().trim().isEmpty()) {
            throw new IllegalArgumentException("O endereço do médico é obrigatório.");
        }
        if (medico.getValorConsulta() <= 0) {
            throw new IllegalArgumentException("Valor da consulta deve ser maior que zero.");
        }
        if (medico.getCodigoConselho() == null || medico.getCodigoConselho().trim().isEmpty()) {
            throw new IllegalArgumentException("Código do conselho é obrigatório.");
        }

        try {
            medicoRepository.salvar(medico);
            notifyMedicoDataChangeListeners(medico.getNome(), false); // Notifica após o salvamento/atualização bem-sucedido
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar médico: " + e.getMessage(), e);
        }
    }

    public Optional<Medico> buscarMedicoPorId(int id) {
        try {
            return medicoRepository.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar médico por ID: " + e.getMessage(), e);
        }
    }

    public void excluirMedico(int id) {
        String nomeMedicoExcluido = null;
        try {
            Optional<Medico> medicoOpt = medicoRepository.buscarPorId(id);
            if (medicoOpt.isPresent()) {
                nomeMedicoExcluido = medicoOpt.get().getNome();
            }

            medicoRepository.excluir(id);
            notifyMedicoDataChangeListeners(nomeMedicoExcluido, true);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir médico: " + e.getMessage(), e);
        }
    }

    public List<Medico> listarMedicos() {
        try {
            return medicoRepository.listarTodos();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar médicos: " + e.getMessage(), e);
        }
    }

    public List<Medico> buscarMedicosPorNomeOuCodigo(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listarMedicos();
        }
        try {
            return medicoRepository.buscarPorNomeOuCodigo(searchTerm.trim());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar médicos por nome ou código: " + e.getMessage(), e);
        }
    }
}