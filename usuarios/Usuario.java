package usuarios;
public class Usuario {
    private String nombre;
    private String password;
    private boolean admin;

    public Usuario(String nombre, String password, boolean admin) {
        this.nombre = nombre;
        this.password = password;
        this.admin = admin;
    }

    public String getNombre() { return nombre; }
    public String getPassword() { return password; }
    public boolean isAdmin() { return admin; }
    @Override
    public String toString() { return nombre + (admin ? " (admin)" : ""); }
}
