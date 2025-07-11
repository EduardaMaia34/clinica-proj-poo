package com.eduardamaia.clinica.projetopooclinica.entities;
import javax.persistence.*;

@Entity
public class Consultas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private int pacienteid;
    private int medicoid;
    private String data;
    private String hora;

    @OneToOne(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public void setData(String data) {
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

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }


    public Consultas() {
    }

    public Consultas(int consultaid, int pacienteid, int medicoid, String data, String hora) {
        setPaciente(pacienteid);
        setMedico(medicoid);
        setData(data);
        setHora(hora);
    }

}
