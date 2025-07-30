package com.eduardamaia.clinica.projetopooclinica.service;

import com.eduardamaia.clinica.projetopooclinica.entities.Consulta;
import com.eduardamaia.clinica.projetopooclinica.repository.ConsultaRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;


public class ConsultaService {

    private final ConsultaRepository consultasRepository;

    public ConsultaService(ConsultaRepository consultasRepository) {
        this.consultasRepository = consultasRepository;
    }


    public Consulta criarConsulta(Consulta consulta) {

        // ⏰ Verifica se o médico já tem uma consulta no mesmo horário
        List<Consulta> consultasExistentes = consultasRepository.listarTodas();

        boolean conflito = consultasExistentes.stream()
                .anyMatch(c ->
                        c.getMedico() == consulta.getMedico() &&
                                c.getData().equals(consulta.getData()) &&
                                c.getHora().equals(consulta.getHora())
                );

        if (conflito) {
            throw new IllegalStateException("O médico já possui uma consulta nesse horário.");
        }
        return consultasRepository.salvar(consulta);
    }


    public Consulta buscarConsultaPorId(int id) {
        Optional<Consulta> consulta = consultasRepository.buscarPorId(id);
        if (consulta.isEmpty()) {
            throw new EntityNotFoundException("Consulta com ID " + id + " não encontrada.");
        }
        return consulta.get();
    }


    public List<Consulta> listarTodasConsultas() {
        return consultasRepository.listarTodas();
    }

    public Consulta atualizarConsulta(int id, Consulta dadosParaAtualizar) {
        // Busca a consulta existente para garantir que ela está no contexto de persistência.
        Consulta consultaExistente = buscarConsultaPorId(id);

        // Atualiza os campos da entidade existente com os novos dados.
        // As validações nos setters da entidade Consultas serão acionadas aqui.
        consultaExistente.setPaciente(dadosParaAtualizar.getPaciente());
        consultaExistente.setMedico(dadosParaAtualizar.getMedico());
        consultaExistente.setData(dadosParaAtualizar.getData());
        consultaExistente.setHora(dadosParaAtualizar.getHora());

        return consultasRepository.salvar(consultaExistente);
    }


    public void deletarConsulta(int id) {
        // Verifica se a consulta existe antes de tentar deletar.
        Consulta consultaParaDeletar = buscarConsultaPorId(id);
        consultasRepository.deletarPorId(consultaParaDeletar.getId());
    }
}