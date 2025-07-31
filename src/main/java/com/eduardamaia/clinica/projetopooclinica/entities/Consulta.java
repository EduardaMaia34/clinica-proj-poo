package com.eduardamaia.clinica.projetopooclinica.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER) // Relacionamento Muitos-Para-Um com Paciente
    @JoinColumn(name = "paciente_id", nullable = false) // Coluna FK na tabela 'consultas'
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.EAGER) // Relacionamento Muitos-Para-Um com Medico
    @JoinColumn(name = "medico_id", nullable = false) // Coluna FK na tabela 'consultas'
    private Medico medico;

    private LocalDate data;
    private String hora;

    @ManyToOne(fetch = FetchType.LAZY) // Relatorio relationship
    @JoinColumn(name = "relatorio_id")
    private Relatorio relatorio;

    public Consulta() {
    }

    public Consulta(Paciente paciente, Medico medico, LocalDate data, String hora) {
        this.paciente = paciente;
        this.medico = medico;
        this.data = data;
        this.hora = hora;
    }

    public Consulta(Integer id, Paciente paciente, Medico medico, LocalDate data, String hora) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.data = data;
        this.hora = hora;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) {
        if (id != null && id <= 0) { // Adicione null check
            throw new IllegalArgumentException("ID não pode ser menor ou igual a 0");
        }
        this.id = id;
    }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { // Agora aceita um objeto Paciente
        this.paciente = paciente;
    }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { // Agora aceita um objeto Medico
        this.medico = medico;
    }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public Relatorio getRelatorio() { return relatorio; }

    public void setRelatorio(Relatorio relatorio) {
        if (this.relatorio != null && this.relatorio.getConsultas().contains(this)) {
            this.relatorio.removeConsulta(this);
        }
        this.relatorio = relatorio;
        if (relatorio != null) {
            relatorio.addConsulta(this);
        }
    }
}