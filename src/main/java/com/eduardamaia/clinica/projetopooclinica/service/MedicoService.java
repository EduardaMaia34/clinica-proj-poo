package com.eduardamaia.clinica.projetopooclinica.service;

import java.util.List;

import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.repository.MedicoRepository;

public class MedicoService {
    private MedicoRepository MedicoRepository = new MedicoRepository();

    public void cadastrarMedico(Medico medico) {
        if (medico.getValorConsulta() <= 0) {
            throw new IllegalArgumentException("Valor da consulta deve ser maior que zero.");
        }
        if (medico.getCodigoConselho() == null || medico.getCodigoConselho().isEmpty()) {
            throw new IllegalArgumentException("Código do conselho é obrigatório.");
        }
        MedicoRepository.salvar(medico);
    }

    public Medico buscarMedico(int id) {
        return MedicoRepository.buscarPorId(id);
    }

    public void excluirMedico(int id) {
        MedicoRepository.excluir(id);
    }

    public List<Medico> listarMedicos() {
        return MedicoRepository.listarTodos();
    }
}
