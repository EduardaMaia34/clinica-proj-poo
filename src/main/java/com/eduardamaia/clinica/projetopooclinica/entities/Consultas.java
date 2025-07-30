package com.eduardamaia.clinica.projetopooclinica.entities;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Consultas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private int pacienteid;
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private int medicoid;
    private LocalDate data;
    private String hora;

    @ManyToOne
    @JoinColumn(name = "relatorio_id") // Esta coluna FK estará na tabela 'consultas'
    private Relatorio relatorio;

    public void setRelatorio(Relatorio relatorio) {
        // Primeiro, verifique se a consulta já estava associada a outro relatório.
        // Se sim, remove a associação anterior para manter a integridade.
        if (this.relatorio != null && this.relatorio.getConsultas().contains(this)) {
            this.relatorio.removeConsulta(this); // Remove a consulta do relatório antigo
        }

        this.relatorio = relatorio; // Define o novo relatório para esta consulta

        // Agora, se o novo relatório não for nulo, adicione esta consulta a ele.
        // O método addConsulta() no Relatorio já se encarrega de verificar duplicatas
        // e de não criar loops infinitos na chamada reversa.
        if (relatorio != null) {
            relatorio.addConsulta(this);
        }
    }
    public void setId(int id){
        if (id<=0)
            throw new IllegalArgumentException("ID não pode ser menor ou igual a 0");
        this.id = id;
    }
    public void setPaciente(int pacienteid) {
        if (pacienteid<=0)
        throw new IllegalArgumentException("ID do paciente nao pode ser menor ou igual a 0");
        this.pacienteid = pacienteid;
    }

    public void setMedico(int medicoid) {
        if (medicoid<=0)
            throw new IllegalArgumentException("ID do médico nao pode ser menor ou igual a 0");
        this.medicoid = medicoid;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Relatorio getRelatorio() {
        return relatorio;
    }

    public int getId(){
        return id;
    }
    public int getPaciente() {
        return pacienteid;
    }

    public int getMedico() {
        return medicoid;
    }

    public LocalDate getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }


    public Consultas() {
    }

    public Consultas(int consultaid, int pacienteid, int medicoid, LocalDate data, String hora) {
        setPaciente(pacienteid);
        setMedico(medicoid);
        setData(data);
        setHora(hora);
    }

}
