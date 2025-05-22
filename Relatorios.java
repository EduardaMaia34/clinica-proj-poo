import java.util.Scanner;

public class Relatorios {
    
    private int pacienteid;
    private int medicoid;
    private int consultaid;
    private String periodo1;
    private String periodo2;

    Scanner read = new Scanner(System.in);

    public void setPaciente(int pacienteid){
        this.pacienteid = pacienteid;
    }
    public void setMedico(int medicoid){
        this.medicoid = medicoid;
    }
    public void setConsulta(int consultaid){
        this.consultaid = consultaid;
    }
    public void setPeriodo1(String periodo1){
        this.periodo1 = periodo1;
    }
    public void setPeriodo2(String periodo2){
        this.periodo2 = periodo2;
    }
    
    public int getPaciente(){
        return pacienteid;
    }
    public int getMedico(){
        return medicoid;
    }
    public int getConsulta(){
        return consultaid;
    }
    public String getPeriodo1(){
        return periodo1;
    }
    public String getPeriodo2(){
        return periodo2;
    }

    public Relatorios(int pacienteid, int medicoid, int consultaid, String periodo1, String periodo2){
         setPaciente(pacienteid);
         setMedico(medicoid);
         setConsulta(consultaid);
         setPeriodo1(periodo1);
         setPeriodo2(periodo2);
    }
    public void porMedico(){
        System.out.print("digite o ID do médico para ver seu relatório: ");
        this.medicoid = read.nextInt();
        read.nextLine();
        // banco de dados mostra o relatório do médico
    }
    public void porPaciente(){
        System.out.print("digite o ID do paciente para ver seu relatório: ");
        this.pacienteid = read.nextInt();
        read.nextLine();
        // banco de dados mostra o relatório do paciente
    }
    public void porPeriodo(){
        System.out.print("digite a data inicial e a data final para ver os relatórios desse período: ");
        this.periodo1 = read.nextLine();
        this.periodo2 = read.nextLine();
        // BD mostra todos os relátorios nesse espaço de tempo
    }

}
