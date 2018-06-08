package HBMs;

import java.util.Date;

public class Excepcion  implements java.io.Serializable {

     private int id;
     private String mensaje;
     private Date fecha;

    public Excepcion() {
    }

    public Excepcion(int id, String mensaje, Date fecha) {
       this.id = id;
       this.mensaje = mensaje;
       this.fecha = fecha;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public String getMensaje() {
        return this.mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}


