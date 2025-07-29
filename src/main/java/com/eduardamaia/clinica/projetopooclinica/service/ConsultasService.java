package com.eduardamaia.clinica.projetopooclinica.service;

import com.eduardamaia.clinica.projetopooclinica.entities.Consultas;
import com.eduardamaia.clinica.projetopooclinica.repository.ConsultasRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * Classe de serviço para gerenciar a lógica de negócio relacionada a Consultas.
 * Esta classe utiliza o ConsultasRepository para acessar os dados e gerencia
 * as transações para garantir a consistência dos dados.
 */
public class ConsultasService {

    private final ConsultasRepository consultasRepository;

    public ConsultasService(ConsultasRepository consultasRepository) {
        this.consultasRepository = consultasRepository;
    }


    public Consultas criarConsulta(Consultas consulta) {
        // Validações de negócio podem ser adicionadas aqui antes de salvar.
        // Por exemplo, verificar se o médico ou paciente existem,
        // ou se já há uma consulta no mesmo horário para o médico.
        return consultasRepository.salvar(consulta);
    }

    /**
     * Busca uma consulta pelo seu ID.
     * A anotação @Transactional(readOnly = true) otimiza a transação para operações de leitura.
     *
     * @param id O ID da consulta a ser buscada.
     * @return A entidade Consultas encontrada.
     * @throws EntityNotFoundException se nenhuma consulta for encontrada com o ID fornecido.
     */
    public Consultas buscarConsultaPorId(int id) {
        Consultas consulta = consultasRepository.buscarPorId(id);
        if (consulta == null) {
            throw new EntityNotFoundException("Consulta com ID " + id + " não encontrada.");
        }
        return consulta;
    }

    /**
     * Lista todas as consultas cadastradas.
     *
     * @return Uma lista de todas as entidades Consultas.
     */
    public List<Consultas> listarTodasConsultas() {
        return consultasRepository.listarTodas();
    }

    /**
     * Atualiza os dados de uma consulta existente.
     *
     * @param id                 O ID da consulta a ser atualizada.
     * @param dadosParaAtualizar A entidade Consultas com os novos dados.
     * @return A entidade Consultas atualizada.
     * @throws EntityNotFoundException se a consulta a ser atualizada não for encontrada.
     */
    public Consultas atualizarConsulta(int id, Consultas dadosParaAtualizar) {
        // Busca a consulta existente para garantir que ela está no contexto de persistência.
        Consultas consultaExistente = buscarConsultaPorId(id); // Reutiliza o método que já lança exceção

        // Atualiza os campos da entidade existente com os novos dados.
        // As validações nos setters da entidade Consultas serão acionadas aqui.
        consultaExistente.setPaciente(dadosParaAtualizar.getPaciente());
        consultaExistente.setMedico(dadosParaAtualizar.getMedico());
        consultaExistente.setData(dadosParaAtualizar.getData());
        consultaExistente.setHora(dadosParaAtualizar.getHora());

        // O método 'salvar' (que usa merge) irá atualizar a entidade no banco de dados.
        return consultasRepository.salvar(consultaExistente);
    }

    /**
     * Deleta uma consulta com base no seu ID.
     *
     * @param id O ID da consulta a ser deletada.
     * @throws EntityNotFoundException se nenhuma consulta for encontrada para deletar.
     */
    public void deletarConsulta(int id) {
        // Verifica se a consulta existe antes de tentar deletar.
        Consultas consultaParaDeletar = buscarConsultaPorId(id);
        consultasRepository.deletarPorId(consultaParaDeletar.getId());
    }
}