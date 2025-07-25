// Exemplo de PacienteService.java (seus métodos podem variar dependendo da sua implementação de repositório)
package com.eduardamaia.clinica.projetopooclinica.service;

import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.repository.PacienteRepository; // Supondo que você tem um repositório
import java.util.List;

public class PacienteService {

    private PacienteRepository pacienteRepository;

    public PacienteService() {
        this.pacienteRepository = new PacienteRepository(); // Inicialize seu repositório aqui
    }

    public void cadastrarPaciente(Paciente paciente) {
        // Adicione validações aqui antes de chamar o repositório
        if (paciente.getNome() == null || paciente.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do paciente é obrigatório.");
        }
        pacienteRepository.salvar(paciente);
    }

    public List<Paciente> listarPacientes() {
        return pacienteRepository.listarTodos(); // Supondo um método para listar todos no repositório
    }

    public void excluirPaciente(int id) {
        // Valide se o ID existe antes de excluir, se necessário
        pacienteRepository.excluir(id);
    }

    public void atualizarPaciente(Paciente paciente) {

        if (paciente.getId() == 0) { // Ou se for null, dependendo do seu modelo
            throw new IllegalArgumentException("ID do paciente é obrigatório para atualização.");
        }

        pacienteRepository.atualizar(paciente);
    }


    public List<Paciente> buscarPacientesPorNomeOuCpf(String termoBusca) {

        return pacienteRepository.listarTodos().stream()
                .filter(p -> p.getNome().toLowerCase().contains(termoBusca.toLowerCase()) ||
                        p.getCpf().contains(termoBusca))
                .toList();
    }
}