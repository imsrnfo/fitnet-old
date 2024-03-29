package HBMs;
// Generated 21/02/2017 08:16:46 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Permiso generated by hbm2java
 */
public class Permiso  implements java.io.Serializable {


     private int id;
     private String nombre;
     private Set permisoUsuarios = new HashSet(0);

    public Permiso() {
    }

	
    public Permiso(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public Permiso(int id, String nombre, Set permisoUsuarios) {
       this.id = id;
       this.nombre = nombre;
       this.permisoUsuarios = permisoUsuarios;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Set getPermisoUsuarios() {
        return this.permisoUsuarios;
    }
    
    public void setPermisoUsuarios(Set permisoUsuarios) {
        this.permisoUsuarios = permisoUsuarios;
    }




}


