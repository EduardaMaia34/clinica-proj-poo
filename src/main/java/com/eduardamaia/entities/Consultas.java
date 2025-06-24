package com.eduardamaia.entities;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Consultas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private int pacienteid;
    private int medicoid;
    private LocalDate data;
    private LocalDate hora;

    @OneToOne(mappedBy = "consulta")
    private Relatorio relatorio;


    public void setRelatorio(Relatorio relatorio){
        if(relatorio !=null){
            relatorio.setConsulta(this);
        }
        this.relatorio = relatorio;
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
      if(data == null) {
          throw new IllegalArgumentException("Data não pode ser nula");
      }
      this.data = data;
    }
    public void setHora(LocalDate hora) {
        if(hora == null){
            throw new IllegalArgumentException("Hora não pode ser nula");
        }
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
    public LocalDate getHora() {
        return hora;
    }
    
    public Consultas() {
    }

    public Consultas(int consultaid, int pacienteid, int medicoid, LocalDate data, LocalDate hora) {
        setPaciente(pacienteid);
        setMedico(medicoid);
        setData(data);
        setHora(hora);
    }
}
