package com.eduardamaia.clinica.projetopooclinica.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "relatorios") // Good practice: explicitly name your table
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // <-- CHANGE: Use Integer for consistency and nullable ID

    @OneToOne(fetch = FetchType.LAZY) // Added FetchType.LAZY for performance
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @OneToOne(fetch = FetchType.LAZY) // Added FetchType.LAZY for performance
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    // Make sure "mappedBy" refers to the field name in Consultas (relatorio)
    @OneToMany(mappedBy = "relatorio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Consultas> consulta = new HashSet<>();

    private LocalDate periodo1;
    private LocalDate periodo2;
    private String conteudo;

    public Relatorio(){
    }

    // Adjusted constructor to receive a Set of Consultas and allow for new reports
    public Relatorio(Medico medico, Paciente paciente, Set<Consultas> consultas, String conteudo, LocalDate periodo1, LocalDate periodo2) {
        setMedico(medico);
        setPaciente(paciente);
        setConteudo(conteudo);
        setPeriodo1(periodo1);
        setPeriodo2(periodo2);
        if (consultas != null) {
            consultas.forEach(this::addConsulta); // Use the addConsulta method to maintain bidirectional link
        }
    }
    // Also consider a constructor for existing entities with an ID
    public Relatorio(Integer id, Medico medico, Paciente paciente, Set<Consultas> consultas, String conteudo, LocalDate periodo1, LocalDate periodo2) {
        this(medico, paciente, consultas, conteudo, periodo1, periodo2); // Call the other constructor
        this.id = id;
    }


    public void setPaciente(Paciente paciente){
        if(paciente == null){
            throw new IllegalArgumentException("Paciente não pode ser nulo");}
        this.paciente = paciente;
    }

    public void setMedico(Medico medico) {
        if (medico == null){
            throw new IllegalArgumentException("O medico do relatorio nao pode ser nulo");
        }
        this.medico = medico;
    }

    public void setConteudo(String conteudo){
        // Changed logic to always set content, but you can keep your validation if preferred
        // if(conteudo !=null && !conteudo.isEmpty()){
        this.conteudo = conteudo;
        // }
    }

    public Set<Consultas> getConsultas() {
        return consulta;
    }

    public void addConsulta(Consultas consulta) {
        if (consulta == null) {
            throw new IllegalArgumentException("A consulta a ser adicionada não pode ser nula.");
        }
        if (!this.consulta.contains(consulta)) {
            this.consulta.add(consulta);
            if (consulta.getRelatorio() != this) {
                consulta.setRelatorio(this);
            }
        }
    }

    public void removeConsulta(Consultas consulta) {
        if (consulta == null) {
            throw new IllegalArgumentException("A consulta a ser removida não pode ser nula.");
        }
        if (this.consulta.contains(consulta)) {
            this.consulta.remove(consulta);
            if (consulta.getRelatorio() == this) {
                consulta.setRelatorio(null);
            }
        }
    }

    public void setPeriodo1(LocalDate periodo1){
        if (periodo1 == null){
            throw new IllegalArgumentException("Periodo 1 não pode ser nulo");
        }
        if(this.periodo2 != null && periodo1.isAfter(this.periodo2)) {
            throw new IllegalArgumentException("Periodo 1 não pode ser após periodo 2");
        }
        this.periodo1 = periodo1;
    }

    public void setPeriodo2(LocalDate periodo2){
        if(periodo2 == null){
            throw new IllegalArgumentException("Periodo 2 não pode ser nulo");
        }
        if (this.periodo1 != null && periodo2.isBefore(this.periodo1)){
            throw new IllegalArgumentException("Periodo 2 não pode ser anterior a periodo 1");
        }
        this.periodo2 = periodo2;
    }

    public void setId(Integer id){ // <-- CHANGE: Accepts Integer
        this.id = id;
    }

    public Paciente getPaciente(){
        return paciente;
    }

    public Medico getMedico(){
        return medico;
    }

    public String getConteudo(){
        return conteudo;
    }

    public LocalDate getPeriodo1(){
        return periodo1;
    }

    public LocalDate getPeriodo2(){
        return periodo2;
    }

    public Integer getId(){ // <-- CHANGE: Returns Integer
        return id;
    }

    @Override
    public String toString() {
        return "Relatorio{" +
                "id=" + id +
                ", medico=" + (medico != null ? medico.getNome() : "N/A") +
                ", paciente=" + (paciente != null ? paciente.getNome() : "N/A") +
                ", periodo1=" + periodo1 +
                ", periodo2=" + periodo2 +
                ", conteudo='" + conteudo + '\'' +
                ", consultasCount=" + (consulta != null ? consulta.size() : 0) +
                '}';
    }
}