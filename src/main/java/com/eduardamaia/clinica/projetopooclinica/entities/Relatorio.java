package com.eduardamaia.clinica.projetopooclinica.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    private Consultas consulta;
    private LocalDate periodo1;
    private LocalDate periodo2;
    private String conteudo;

    public Relatorio(){
    }
    public Relatorio(Medico medico, Paciente paciente, Consultas consulta, String conteudo, LocalDate periodo1, LocalDate periodo2) {
        setMedico(medico);
        setPaciente(paciente);
        setConsulta(consulta);
        setConteudo(conteudo);
        setPeriodo1(periodo1);
        setPeriodo2(periodo2);
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
        if(conteudo !=null && !conteudo.isEmpty()){
            this.conteudo = conteudo;
        }
    }
    public void setConsulta(Consultas consulta) {
        if (consulta == null) {
            throw new IllegalArgumentException("A consulta do relatório não pode ser nula.");
        }
        this.consulta = consulta;
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
    public void setId(int id){
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
    public Consultas getConsulta() {
        return consulta;
    }
    public LocalDate getPeriodo1(){
        return periodo1;
    }
    public LocalDate getPeriodo2(){
        return periodo2;
    }
    public int getId(){
        return id;
    }

}
