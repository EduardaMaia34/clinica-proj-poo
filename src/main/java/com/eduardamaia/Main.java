package com.eduardamaia;

import com.eduardamaia.entities.Medico;
import com.eduardamaia.entities.Paciente;
import com.eduardamaia.service.MedicoService;
import com.eduardamaia.service.PacienteService;

public class Main {
    public static void main(String[] args) {
        // Serviços
        MedicoService medicoService = new MedicoService();
        PacienteService pacienteService = new PacienteService();

        // Criando médicos
        Medico medico1 = new Medico("Dr. João", "111.111.111-11", "Rua A", 300.0, "CRM123", 1);
        Medico medico2 = new Medico("Dra. Maria", "222.222.222-22", "Rua B", 400.0, "CRM456", 2);

        // Cadastrando médicos
        medicoService.cadastrarMedico(medico1);
        medicoService.cadastrarMedico(medico2);

        // Criando pacientes
        Paciente paciente1 = new Paciente("Carlos", "333.333.333-33", "Rua C", "PRONT001", 1);
        Paciente paciente2 = new Paciente("Ana", "444.444.444-44", "Rua D", "PRONT002", 2);

        // Cadastrando pacientes
        pacienteService.cadastrarPaciente(paciente1);
        pacienteService.cadastrarPaciente(paciente2);

        /*// Listando médicos
        System.out.println("Médicos cadastrados:");
        medicoService.listarMedicos().forEach(System.out::println);

        // Listando pacientes
        System.out.println("\nPacientes cadastrados:");
        pacienteService.listarPacientes().forEach(System.out::println);

        // Buscar médico por ID
        System.out.println("\nBuscando médico ID 1:");
        System.out.println(medicoService.buscarMedico(1));

        // Excluir paciente ID 2
        pacienteService.excluirPaciente(2);
        System.out.println("\nPacientes após exclusão:");
        pacienteService.listarPacientes().forEach(System.out::println);*/
    }
}
