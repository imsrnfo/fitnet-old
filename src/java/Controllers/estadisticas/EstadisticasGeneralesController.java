
package Controllers.estadisticas;

import DAOs.EstadisticasDAO;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;


@Named(value = "estadisticasGeneralesController")
@ViewScoped
public class EstadisticasGeneralesController implements Serializable{
    
    
    String fInicio;
    String fFin;
    
    public String getfInicio() {
        return fInicio;
    }
    
    public void setfInicio(String fInicio) {
        this.fInicio = fInicio;
    }
    
    public String getfFin() {
        return fFin;
    }
    
    public void setfFin(String fFin) {
        this.fFin = fFin;
    }
    
    @PostConstruct
    public void init(){
        if (!ManejadorSession.getInstance().tienePermiso("ESTADISTICAS_GENERALES")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        fInicio = df.format( new Date());
        fFin = df.format( new Date());
        calcularDatos();
    }
    
    
    
    public void calcularDatos(){
        cantAccessos = obtenerAccesos();
        gananciasActividades = obtenerGananciasActividades();
        gananciasArticulos = obtenerGananciasArticulos();
        totalDeudas = obtenerDeudas();
        clientesNuevos = obtenerClientesNuevosRegistrados();
        
        gananciasTotales = String.valueOf(Integer.valueOf(gananciasActividades)+Integer.valueOf(gananciasArticulos));
        gananciasLiquidas = String.valueOf(Integer.valueOf(gananciasTotales)-Integer.valueOf(totalDeudas));
        
        List <Object[]> accesosPorHora = infoAccesosHora();
        horaDeAccessos ="";
        cantidadDeAccessosHora ="";
        for (Object[] item : accesosPorHora){
            Long cantidad = (Long) item[0];
            Integer hora = (Integer) item[1];
            horaDeAccessos = horaDeAccessos + "'" + String.valueOf(hora) + ":00hs' , ";
            cantidadDeAccessosHora = cantidadDeAccessosHora + String.valueOf(cantidad) + ", ";
        }
        
        List <Object[]> cobrosPorDia = infoCobrosDia();
        diasDeCobros ="";
        cobrosPorDias ="";
        for (Object[] item : cobrosPorDia){
            Long plata = (Long) item[0];
            Integer dia = (Integer) item[1];
            Integer mes = (Integer) item[2];
            Integer anio = (Integer) item[3];
            diasDeCobros = diasDeCobros + "'" + String.valueOf(dia) + "/" +  String.valueOf(mes)+ "/" +  String.valueOf(anio).substring(2) + "' , ";
            cobrosPorDias = cobrosPorDias + String.valueOf(plata) + ", ";
        }
        
    }
    
    //------------------------CALCULOS PARA LAS ESTADISTICAS-------------------------
    
    private String obtenerAccesos(){
        try{
        return String.valueOf(EstadisticasDAO.getInstance().obtenerAccesos(fInicio, fFin));
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    private List <Object[]> infoAccesosHora(){
        try{
        return EstadisticasDAO.getInstance().infoAccesosHora(fInicio, fFin);
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    private List <Object[]> infoCobrosDia(){
        try{
        return EstadisticasDAO.getInstance().infoCobrosDia(fInicio, fFin);
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    private String obtenerGananciasActividades(){
        try{
        return String.valueOf(EstadisticasDAO.getInstance().obtenerGananciasSupuestasActividades(fInicio, fFin));
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    private String obtenerGananciasArticulos(){
        try{
        return String.valueOf(EstadisticasDAO.getInstance().obtenerGananciasSupuestasArticulos(fInicio, fFin));
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    private String obtenerDeudas(){
        try{
        return String.valueOf(EstadisticasDAO.getInstance().obtenerDeudas(fInicio, fFin));
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    private String obtenerClientesNuevosRegistrados(){
        try{
        return String.valueOf(EstadisticasDAO.getInstance().obtenerCantClientesRegistradosRango(fInicio, fFin));
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    //-------------------------
    
    String cantAccessos = "";
    String gananciasActividades = "";
    String gananciasArticulos = "";
    String totalDeudas = "";
    String clientesNuevos = "";
    String horaDeAccessos = "";
    String cantidadDeAccessosHora= "";
    String diasDeCobros = "";
    String cobrosPorDias= "";
    String gananciasTotales = "";
    String gananciasLiquidas = "";

    public String getGananciasTotales() {
        return gananciasTotales;
    }

    public void setGananciasTotales(String gananciasTotales) {
        this.gananciasTotales = gananciasTotales;
    }

    public String getGananciasLiquidas() {
        return gananciasLiquidas;
    }

    public void setGananciasLiquidas(String gananciasLiquidas) {
        this.gananciasLiquidas = gananciasLiquidas;
    }

    
    
    public String getDiasDeCobros() {
        return diasDeCobros;
    }

    public void setDiasDeCobros(String diasDeCobros) {
        this.diasDeCobros = diasDeCobros;
    }

    public String getCobrosPorDias() {
        return cobrosPorDias;
    }

    public void setCobrosPorDias(String cobrosPorDias) {
        this.cobrosPorDias = cobrosPorDias;
    }

    public String getCantAccessos() {
        return cantAccessos;
    }

    public void setCantAccessos(String cantAccessos) {
        this.cantAccessos = cantAccessos;
    }

    public String getGananciasActividades() {
        return gananciasActividades;
    }

    public void setGananciasActividades(String gananciasActividades) {
        this.gananciasActividades = gananciasActividades;
    }

    public String getGananciasArticulos() {
        return gananciasArticulos;
    }

    public void setGananciasArticulos(String gananciasArticulos) {
        this.gananciasArticulos = gananciasArticulos;
    }

    public String getTotalDeudas() {
        return totalDeudas;
    }

    public void setTotalDeudas(String totalDeudas) {
        this.totalDeudas = totalDeudas;
    }

    public String getClientesNuevos() {
        return clientesNuevos;
    }

    public void setClientesNuevos(String clientesNuevos) {
        this.clientesNuevos = clientesNuevos;
    }

    public String getHoraDeAccessos() {
        return horaDeAccessos;
    }

    public void setHoraDeAccessos(String horaDeAccessos) {
        this.horaDeAccessos = horaDeAccessos;
    }

    public String getCantidadDeAccessosHora() {
        return cantidadDeAccessosHora;
    }

    public void setCantidadDeAccessosHora(String cantidadDeAccessosHora) {
        this.cantidadDeAccessosHora = cantidadDeAccessosHora;
    }
}
