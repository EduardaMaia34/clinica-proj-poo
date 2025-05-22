public class Usuario {
    private String user;
    private String senha;
    private boolean admin;
    private int userId;

    public void setUser(){
        if (user != null && !user.isEmpty()) {
            this.user = user;
        }
    }
    public void setSenha(){
        if (senha != null && !senha.isEmpty()){
            this.senha = senha;
        }
    }

    public Usuario (String user, String senha, boolean admin, int userId){
        setUser(user);
        setSenha(senha);
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