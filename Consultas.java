import java.util.Scanner;

public class Consultas{
private int consultaid;
private int pacienteid;
private int medicoid;
private String data;
private String hora;

Scanner read = new Scanner(System.in);

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

public Consultas(int consultasid, int pacienteid, int medicoid, String data, String hora){
    setConsulta(consultaid);
    setPaciente(pacienteid);
    setMedico(medicoid);
    setData(data);
    setHora(hora);
}

public void marcar(int pacienteid, int medicoid, String data, String hora){
    System.out.println("digite o id do paciente, o id do médico, a data e a hora que deseja agendar: ");
    this.pacienteid = read.nextInt();
    this.medicoid = read.nextInt();
    this.data = read.nextLine();
    this.hora = read.nextLine();
    System.out.println("consulta marcada! ");
    System.out.println("ID do médico: " + medicoid);
    System.out.println("Data: " + data + "\nHora: " + hora);
}
public void cancelar(int consultaid){
    System.out.println("Digite o ID da consulta para realizar o cancelamento: ");
    this.consultaid = read.nextInt();
    // if consultaid esta no banco de dados
    System.out.println("Consulta " + consultaid + " cancelada!");
    // caso contrario, System.out.println("ID inválido, verifique se foi realizado o agendamento");
}
public void buscar(int pacienteid, int medicoid, String data, String hora){
    System.out.println("para buscar uma consulta, digite o id do paciente, id do médico, data e hora agendada: ");
    this.pacienteid = read.nextInt();
    this.medicoid = read.nextInt();
    this.data = read.nextLine();
    this.hora = read.nextLine();
    // se o id do paciente, id do médico, data e hora estiverem no banco de dados, a consulta aparece na tela
    // caso contrario, aparece "dados errados ou inexistentes"
}

}
