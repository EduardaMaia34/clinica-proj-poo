package com.eduardamaia.clinica.projetopooclinica.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @OneToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @OneToMany(mappedBy = "relatorio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Consultas> consulta = new HashSet<>();

    private LocalDate periodo1;
    private LocalDate periodo2;
    private String conteudo;

    public Relatorio(){
    }
    public Relatorio(Medico medico, Paciente paciente, Consultas consulta, String conteudo, LocalDate periodo1, LocalDate periodo2) {
        setMedico(medico);
        setPaciente(paciente);
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
    public Set<Consultas> getConsultas() {
        return consulta;
    }
    public void addConsulta(Consultas consulta) {
        if (consulta == null) {
            throw new IllegalArgumentException("A consulta a ser adicionada não pode ser nula.");
        }
        // Evita adicionar a mesma consulta múltiplas vezes se já estiver na coleção
        if (!this.consulta.contains(consulta)) {
            this.consulta.add(consulta);
            // Garante a consistência bidirecional: a consulta também deve referenciar este relatório
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
            // Remove a associação no lado da Consulta se ela ainda apontar para este relatório
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
