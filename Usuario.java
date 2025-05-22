public class Usuario {
    private String user;
    private String senha;
    private boolean admin;
    private int userId;

    public Usuario (String user, String senha, boolean admin, int userId){
        this.user = user;
        this.senha = senha;
        this.admin = admin;
        this.userId = userId;
    }

    public void cadastro(String user, String senha, boolean admin){
        //fazer cadastro
    }
    public void login(String user, String senha){
        //fazer autenticação
    }
}
