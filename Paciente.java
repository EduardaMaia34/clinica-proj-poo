public class Paciente {
    private String nome;
    private String cpf;
    private String endereco;
    private String prontuario;
    private int id;

    public Paciente(String nome, String cpf, String endereco, String prontuario, int id) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.prontuario = prontuario;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void cadastro(String nome, String cpf, String endereco, String prontuario) {
        //fazer cadastro
    }
    public void editar(String nome, String cpf, String endereco, String prontuario) {
        //editar informacoes
    }
    public void excluir(int id) {
        //excluir no bd
    }
    public int buscar(String nome, String cpf){
        //buscar no bd
        return 0;
    }
    
}