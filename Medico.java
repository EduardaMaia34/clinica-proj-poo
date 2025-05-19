public class Medico {
    private String nome;
    private String cpf;
    private String endereco;
    private double valorConsulta;
    private int codigoConselho;
    private int id;

    public Medico(String nome, String cpf, String endereco, double valorConsulta, int codigoConselho, int id) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.valorConsulta = valorConsulta;
        this.codigoConselho = codigoConselho;
        this.id = id;
    }

    public void cadastro(String nome, String cpf, double valorConsulta, int codigoConselho) {
        //fazer cadastro
    }
    public void editar(String nome, String cpf, String endereco, double valorConsulta, int codigoConselho) {
        //editar informacoes
    }
    public void excluir(int id) {
        //excluir no bd
    }
    public int buscar(int codigoConselho, String nome){
        //buscar no bd
        return 0;
    }
    
}