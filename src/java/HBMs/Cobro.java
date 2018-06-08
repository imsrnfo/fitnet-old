package HBMs;
// Generated 28/05/2017 09:57:26 AM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Cobro generated by hbm2java
 */
public class Cobro  implements java.io.Serializable {


     private int id;
     private Actividad actividad;
     private Bitacora bitacora;
     private Cliente cliente;
     private Date fecha;
     private int importeTotal;
     private int entrega;
     private int debe;
     private int descuento;
     private String motivoDescuento;
     private Date fdesde;
     private Date fhasta;
     private Set cobroArticulos = new HashSet(0);

    public Cobro() {
    }

	
    public Cobro(int id, Bitacora bitacora, Cliente cliente, Date fecha, int importeTotal, int entrega, int debe, int descuento, String motivoDescuento) {
        this.id = id;
        this.bitacora = bitacora;
        this.cliente = cliente;
        this.fecha = fecha;
        this.importeTotal = importeTotal;
        this.entrega = entrega;
        this.debe = debe;
        this.descuento = descuento;
        this.motivoDescuento = motivoDescuento;
    }
    public Cobro(int id, Actividad actividad, Bitacora bitacora, Cliente cliente, Date fecha, int importeTotal, int entrega, int debe, int descuento, String motivoDescuento, Date fdesde, Date fhasta, Set cobroArticulos) {
       this.id = id;
       this.actividad = actividad;
       this.bitacora = bitacora;
       this.cliente = cliente;
       this.fecha = fecha;
       this.importeTotal = importeTotal;
       this.entrega = entrega;
       this.debe = debe;
       this.descuento = descuento;
       this.motivoDescuento = motivoDescuento;
       this.fdesde = fdesde;
       this.fhasta = fhasta;
       this.cobroArticulos = cobroArticulos;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public Actividad getActividad() {
        return this.actividad;
    }
    
    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
    public Bitacora getBitacora() {
        return this.bitacora;
    }
    
    public void setBitacora(Bitacora bitacora) {
        this.bitacora = bitacora;
    }
    public Cliente getCliente() {
        return this.cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public int getImporteTotal() {
        return this.importeTotal;
    }
    
    public void setImporteTotal(int importeTotal) {
        this.importeTotal = importeTotal;
    }
    public int getEntrega() {
        return this.entrega;
    }
    
    public void setEntrega(int entrega) {
        this.entrega = entrega;
    }
    public int getDebe() {
        return this.debe;
    }
    
    public void setDebe(int debe) {
        this.debe = debe;
    }
    public int getDescuento() {
        return this.descuento;
    }
    
    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }
    public String getMotivoDescuento() {
        return this.motivoDescuento;
    }
    
    public void setMotivoDescuento(String motivoDescuento) {
        this.motivoDescuento = motivoDescuento;
    }
    public Date getFdesde() {
        return this.fdesde;
    }
    
    public void setFdesde(Date fdesde) {
        this.fdesde = fdesde;
    }
    public Date getFhasta() {
        return this.fhasta;
    }
    
    public void setFhasta(Date fhasta) {
        this.fhasta = fhasta;
    }
    public Set getCobroArticulos() {
        return this.cobroArticulos;
    }
    
    public void setCobroArticulos(Set cobroArticulos) {
        this.cobroArticulos = cobroArticulos;
    }




}


