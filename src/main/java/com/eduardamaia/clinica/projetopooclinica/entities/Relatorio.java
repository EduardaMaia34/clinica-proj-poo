package com.eduardamaia.clinica.projetopooclinica.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "relatorios")
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // CORREÇÃO: Mude para EAGER para evitar LazyInitializationException na tabela
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    // CORREÇÃO: Mude para EAGER para evitar LazyInitializationException na tabela
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @OneToMany(mappedBy = "relatorio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Consulta> consultas = new HashSet<>(); // Renomeado para plural

    @Column(name = "periodo_inicial") // Nomes de coluna mais explícitos
    private LocalDate periodo1;

    @Column(name = "periodo_final")
    private LocalDate periodo2;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    public Relatorio() {}
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Set<Consulta> getConsultas() { return consultas; }
    public void setConsultas(Set<Consulta> consultas) { this.consultas = consultas; }
    public LocalDate getPeriodo1() { return periodo1; }
    public void setPeriodo1(LocalDate periodo1) { this.periodo1 = periodo1; }
    public LocalDate getPeriodo2() { return periodo2; }
    public void setPeriodo2(LocalDate periodo2) { this.periodo2 = periodo2; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
}