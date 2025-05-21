public class Consultas{
private int consultaid;
private int pacienteid;
private int medicoid;
private String data;
private String hora;

public void setConsulta(int consultaid){
    this.consultaid = consultaid;
}
public void setPaciente(int pacienteid){
    this.pacienteid = pacienteid;
}
public void setMedico(int medicoid){
    this.medicoid = medicoid;
}
public void setData(String data){
    this.data = data;
}
public void setHora(String hora){
this.hora = hora;
}

public int getConsulta(){
    return consultaid;
}
public int getPaciente(){
    return pacienteid;
}
public int getMedico(){
    return medicoid;
}
public String getData(){
    return data;
}
public String getHora(){
    return hora;
}

public Consultas(int consultaid, int pacienteid, int medicoid, String data, String hora){
    setConsulta(consultaid);
    setPaciente(pacienteid);
    setMedico(medicoid);
    setData(data);
    setHora(hora);
}

public void marcar(int pacienteid, int medicoid, String data, String hora){
    //fazer codigo
}
public void cancelar(int consultaid){
    // fazer codigo
}
public int buscar(int pacienteid, int medicoid, String data, String hora){
    // fazer codigo
}

}
