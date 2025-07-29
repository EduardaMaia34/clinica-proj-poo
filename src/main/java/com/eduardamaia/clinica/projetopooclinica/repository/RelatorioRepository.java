package com.eduardamaia.clinica.projetopooclinica.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.eduardamaia.clinica.projetopooclinica.entities.Medico;
import com.eduardamaia.clinica.projetopooclinica.entities.Paciente;
import com.eduardamaia.clinica.projetopooclinica.entities.Relatorio;

public class RelatorioRepository {
    private List <Relatorio> relatorios = new ArrayList<>();
    private int nextId = 1;

    public void Salvar(Relatorio relatorio) {
        if(relatorio.getId() == 0) {
            relatorio.setId(nextId++);
            relatorios.add(relatorio);
        }
    }
    public Relatorio buscarPorId(int id){
        return relatorios.stream()
                .filter(r->r.getId() == id)
                .findFirst()
                .orElse(null);
    }
    public void excluir(int id) {
        relatorios.removeIf(r -> r.getId() == id);
    }

    public List <Relatorio> listartodos(){
        return new ArrayList<>(relatorios);
    }

    public List <Relatorio> buscarPorMedico(Medico medico){
        if (medico == null){
            return new ArrayList<>();
        }
        return relatorios.stream()
                .filter(r-> r.getMedico() != null && r.getMedico().getId() == medico.getId())
                .collect(Collectors.toList());
    }

    public List<Relatorio> buscarPorPaciente(Paciente paciente){
        if(paciente == null){
            return new ArrayList<>();
        }
        return relatorios.stream()
                .filter(r->r.getPaciente() != null && r.getPaciente().getId() == paciente.getId())
                .collect(Collectors.toList());
    }

    public List<Relatorio> buscarPorPeriodo(LocalDate periodo1, LocalDate periodo2) {
        if (periodo1 == null || periodo2 == null) {
            throw new IllegalArgumentException("Ambas as datas de período devem ser fornecidas.");
        }
        return relatorios.stream()
                .filter(r -> {
                    LocalDate relatorioInicio = r.getPeriodo1();
                    LocalDate relatorioFim = r.getPeriodo2();

                    return (relatorioInicio != null && relatorioFim != null &&
                            !relatorioInicio.isAfter(periodo1) && !relatorioFim.isBefore(periodo2));
                })
                .collect(Collectors.toList());
    }
}
