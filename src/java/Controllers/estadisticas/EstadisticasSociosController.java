
package Controllers.estadisticas;

import DAOs.EstadisticasDAO;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;


@Named(value = "estadisticasSociosController")
@ViewScoped
public class EstadisticasSociosController implements Serializable{
    
    public String obtenerCantClientesActivos(){
        try{
            return String.valueOf(EstadisticasDAO.getInstance().obtenerCantClientesActivos());
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    public String obtenerCantClientesInactivos(){
        try{
            return String.valueOf(EstadisticasDAO.getInstance().obtenerCantClientesInactivos());
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    public String obtenerCantClientesMujeres(){
        try{
            return String.valueOf(EstadisticasDAO.getInstance().obtenerCantClientesMujeres());
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    public String obtenerCantClientesHombres(){
        try{
            return String.valueOf(EstadisticasDAO.getInstance().obtenerCantClientesHombres());
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    public String obtenerCantClientesMenoresIgnaules24Anios(){
        try{
            return String.valueOf(EstadisticasDAO.getInstance().obtenerCantClientesPorRangoEdad(0,25));
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    public String obtenerCantClientesEntre25y34Anios(){
        try{
            return String.valueOf(EstadisticasDAO.getInstance().obtenerCantClientesPorRangoEdad(25,35));
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    public String obtenerCantClientesEntre35y44Anios(){
        try{
            return String.valueOf(EstadisticasDAO.getInstance().obtenerCantClientesPorRangoEdad(35,45));
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    public String obtenerCantClientesMayoresDe44Anios(){
        try{
            return String.valueOf(EstadisticasDAO.getInstance().obtenerCantClientesPorRangoEdad(45,99));
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    @PostConstruct
    public void init(){
        try{
            if (!ManejadorSession.getInstance().tienePermiso("ESTADISTICAS_SOCIOS")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
    }
}
