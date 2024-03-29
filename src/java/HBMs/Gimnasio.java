package HBMs;
// Generated 21/02/2017 08:16:46 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Gimnasio generated by hbm2java
 */
public class Gimnasio  implements java.io.Serializable {


     private int id;
     private String nombre;
     private String imagen;
     private Set usuarios = new HashSet(0);

    public Gimnasio() {
    }

	
    public Gimnasio(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public Gimnasio(int id, String nombre, String imagen, Set usuarios) {
       this.id = id;
       this.nombre = nombre;
       this.imagen = imagen;
       this.usuarios = usuarios;
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
    public String getImagen() {
        return this.imagen;
    }
    
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public Set getUsuarios() {
        return this.usuarios;
    }
    
    public void setUsuarios(Set usuarios) {
        this.usuarios = usuarios;
    }




}


