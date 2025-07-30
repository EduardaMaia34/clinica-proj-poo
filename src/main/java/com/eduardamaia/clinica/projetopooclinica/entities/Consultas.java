package com.eduardamaia.clinica.projetopooclinica.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "consultas") // It's good practice to explicitly name your table
public class Consultas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Use Long for auto-generated IDs, it's generally safer than int

    @ManyToOne(fetch = FetchType.LAZY) // Relacionamento Muitos-Para-Um com Paciente
    @JoinColumn(name = "paciente_id", nullable = false) // Coluna FK na tabela 'consultas'
    private Paciente paciente; // <--- CORREÇÃO PRINCIPAL: Referência ao objeto Paciente

    @ManyToOne(fetch = FetchType.LAZY) // Relacionamento Muitos-Para-Um com Medico
    @JoinColumn(name = "medico_id", nullable = false) // Coluna FK na tabela 'consultas'
    private Medico medico;

    private LocalDate data;
    private String hora;

    @ManyToOne(fetch = FetchType.LAZY) // Relatorio relationship
    @JoinColumn(name = "relatorio_id")
    private Relatorio relatorio;

    // --- Constructors ---
    public Consultas() {
    }

    // Adjusted constructor to use entity objects
    public Consultas(Paciente paciente, Medico medico, LocalDate data, String hora) {
        this.paciente = paciente;
        this.medico = medico;
        this.data = data;
        this.hora = hora;
    }

    public Consultas(Integer id, Paciente paciente, Medico medico, LocalDate data, String hora) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.data = data;
        this.hora = hora;
    }

    // --- Getters e Setters ---
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

    // Lógica para setar relatório (já estava boa, mas certifique-se que Relatorio
    // tem a contraparte OneToMany e os métodos add/removeConsulta)
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