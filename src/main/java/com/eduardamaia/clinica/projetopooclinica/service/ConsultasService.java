package com.eduardamaia.clinica.projetopooclinica.service;

import com.eduardamaia.clinica.projetopooclinica.entities.Consultas;
import com.eduardamaia.clinica.projetopooclinica.repository.ConsultasRepository;
import com.eduardamaia.clinica.projetopooclinica.repository.PacienteRepository;
import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.service.PacienteService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;


public class ConsultasService {

    private final ConsultasRepository consultasRepository;

    public ConsultasService(ConsultasRepository consultasRepository) {
        this.consultasRepository = consultasRepository;
    }


    public Consultas criarConsulta(Consultas consulta) {

        // ⏰ Verifica se o médico já tem uma consulta no mesmo horário
        List<Consultas> consultasExistentes = consultasRepository.listarTodas();

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


    public Consultas buscarConsultaPorId(int id) {
        Consultas consulta = consultasRepository.buscarPorId(id);
        if (consulta == null) {
            throw new EntityNotFoundException("Consulta com ID " + id + " não encontrada.");
        }
        return consulta;
    }


    public List<Consultas> listarTodasConsultas() {
        return consultasRepository.listarTodas();
    }

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


    public void deletarConsulta(int id) {
        // Verifica se a consulta existe antes de tentar deletar.
        Consultas consultaParaDeletar = buscarConsultaPorId(id);
        consultasRepository.deletarPorId(consultaParaDeletar.getId());
    }
}