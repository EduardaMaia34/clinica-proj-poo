public class Usuario {
    private int id;
    private String usuario;
    private String senha;
    private boolean admin;
    // Construtor
    public Usuario(int id, String usuario, String senha, boolean admin) {
        this.id = id;
        this.usuario = usuario;
        this.senha = senha;
        this.admin = admin;
    }
    // --- MÉTODOS PÚBLICOS PARA ACESSO CONTROLADO ---
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        if (usuario != null && !usuario.isEmpty()) {
            this.usuario = usuario;
        }
    }
    public String getSenha() {
        return "********";
    }
    public void setSenha(String novaSenha) {
        if (novaSenha != null && novaSenha.length() >= 8) {
            this.senha = novaSenha;
        }
    }
    public boolean isAdmin() {
        return admin;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    public void login(String usuario, String senha) {
    }
}
