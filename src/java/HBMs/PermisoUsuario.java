package HBMs;
// Generated 21/02/2017 08:16:46 PM by Hibernate Tools 4.3.1



/**
 * PermisoUsuario generated by hbm2java
 */
public class PermisoUsuario  implements java.io.Serializable {


     private int id;
     private Permiso permiso;
     private Usuario usuario;

    public PermisoUsuario() {
    }

    public PermisoUsuario(int id, Permiso permiso, Usuario usuario) {
       this.id = id;
       this.permiso = permiso;
       this.usuario = usuario;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public Permiso getPermiso() {
        return this.permiso;
    }
    
    public void setPermiso(Permiso permiso) {
        this.permiso = permiso;
    }
    public Usuario getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }




}


