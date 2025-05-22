public class Medico {
    private String nome;
    private String cpf;
    private String endereco;
    private double valorConsulta;
    private int codigoConselho;
    private int id;

    public Medico(String nome, String cpf, String endereco, double valorConsulta, int codigoConselho, int id) {
        setNome(nome);
        setCpf(cpf);
        setEndereco(endereco);
        setValorConsulta(valorConsulta);
        setCodigoConselho(codigoConselho);
        setId(id);
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

    public double getValorConsulta() {
        return valorConsulta;
    }
    public void setValorConsulta(double valorConsulta) {
        if (valorConsulta > 0.00){
            this.valorConsulta = valorConsulta;
        } else {
            this.valorConsulta = 0.00;
        }
    }

    public int getCodigoConselho() {
        return codigoConselho;
    }
    public void setCodigoConselho(int codigoConselho) {
        this.codigoConselho = codigoConselho;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
}