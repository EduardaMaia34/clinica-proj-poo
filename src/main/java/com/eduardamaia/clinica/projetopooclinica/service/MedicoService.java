package com.eduardamaia.clinica.projetopooclinica.service;

import java.util.List;
import java.util.Optional; // Importado para retorno de métodos que podem não encontrar resultados

import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.repository.MedicoRepository; // Seu repositório

public class MedicoService {

    private MedicoRepository medicoRepository;

    public MedicoService() {
        // O MedicoRepository agora é instanciado aqui, e ele DEVE ser responsável
        // por gerenciar suas próprias Sessions e Transactions para cada operação.
        // CUIDADO: Esta abordagem (repositório auto-gerenciado) é mais simples para um MVP,
        // mas em aplicações maiores, o serviço geralmente gerencia a transação
        // e passa a Session para o repositório.
        this.medicoRepository = new MedicoRepository();
    }

    /**
     * Salva um novo médico ou atualiza um existente.
     * Inclui validação básica de campos de Medico e Pessoa.
     *
     * @param medico O objeto Medico a ser salvo/atualizado.
     * @throws IllegalArgumentException se a validação falhar.
     * @throws RuntimeException se ocorrer um erro durante a operação de persistência.
     */
    public void salvarMedico(Medico medico) { // Renomeado de cadastrarMedico para refletir atualização também
        // Validações da entidade Pessoa (já que Medico herda de Pessoa)
        if (medico.getNome() == null || medico.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do médico é obrigatório.");
        }
        if (medico.getCpf() == null || medico.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do médico é obrigatório.");
        }
        if (medico.getEndereco() == null || medico.getEndereco().trim().isEmpty()) {
            throw new IllegalArgumentException("O endereço do médico é obrigatório.");
        }

        // Validações específicas de Medico
        if (medico.getValorConsulta() <= 0) {
            throw new IllegalArgumentException("Valor da consulta deve ser maior que zero.");
        }
        if (medico.getCodigoConselho() == null || medico.getCodigoConselho().trim().isEmpty()) {
            throw new IllegalArgumentException("Código do conselho é obrigatório.");
        }

        try {
            // O método 'salvar' do repositório deve lidar com persistir (novo) ou merge (existente)
            medicoRepository.salvar(medico);
        } catch (Exception e) {
            
            throw new RuntimeException("Erro ao salvar médico: " + e.getMessage(), e);
        }
    }

    
    public Optional<Medico> buscarMedicoPorId(int id) { // Retorna Optional<Medico>
        try {
            return medicoRepository.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar médico por ID: " + e.getMessage(), e);
        }
    }

    /**
     * Exclui um médico pelo seu ID.
     * @param id O ID do médico a ser excluído.
     * @throws RuntimeException se ocorrer um erro durante a exclusão.
     */
    public void excluirMedico(int id) {
        try {
            
            medicoRepository.excluir(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir médico: " + e.getMessage(), e);
        }
    }

    /**
     * Lista todos os médicos cadastrados.
     * @return Uma lista de todos os médicos.
     * @throws RuntimeException se ocorrer um erro durante a listagem.
     */
    public List<Medico> listarMedicos() {
        try {
            return medicoRepository.listarTodos();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar médicos: " + e.getMessage(), e);
        }
    }

    
    public List<Medico> buscarMedicosPorNomeOuCodigo(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listarMedicos(); // Se o termo de busca for vazio, retorna todos
        }
        try {
            // DELEGAR a busca ao repositório para que a filtragem ocorra no banco de dados
            return medicoRepository.buscarPorNomeOuCodigo(searchTerm.trim());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar médicos por nome ou código: " + e.getMessage(), e);
        }
    }


}