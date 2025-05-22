public class Paciente {
    private String nome;
    private String cpf;
    private String endereco;
    private String prontuario;
    private int id;

    public Paciente(String nome, String cpf, String endereco, String prontuario, int id) {
        setNome(nome);
        setCpf(cpf);
        setEndereco(endereco);
        setProntuario(prontuario);
        setId(id);
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

    //getters e setters
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getProntuario() {
        return prontuario;
    }
    public void setProntuario(String prontuario) {
        this.prontuario = prontuario;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
}